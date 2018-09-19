package cn.veasion.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.veasion.bean.StockCode;
import cn.veasion.bean.StockData;
import cn.veasion.bean.StockEnum;
import cn.veasion.bean.StockMarketEnum;
import cn.veasion.bean.StockTrade;
import cn.veasion.util.CrawlerUtil;
import cn.veasion.util.LogUtil;

/**
 * 东方财富股票爬虫
 * 
 * @author zhuowei.luo
 */
public class EastMoneyCrawler implements StockCrawler {

	private static final String INDEX_URL = "http://quote.eastmoney.com/stocklist.html";
	private static final String STOCK_INFO_URL = "http://nuff.eastmoney.com/EM_Finance2015TradeInterface/JS.ashx?cb=callback&id={id}";
	private static final String STOCK_TYPE_INFO_URL = "http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?type=CT&cmd=E.{id}&sty=DCRRBKCPALTB&cb=callback&token={token}";

	private static String token;

	@Override
	public List<StockData> findData(StockMarketEnum stockMarket, boolean filterEp) throws Exception {
		// 获取所有股票代码
		List<StockCode> codes = findStockCodes(stockMarket, filterEp);
		// 初始化同步集合
		List<StockData> datas = Collections.synchronizedList(new ArrayList<StockData>(codes.size()));
		// 初始化线程池
		ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		for (int i = 0; i < codes.size(); i += THREAD_MAX_LOAD_CODE_COUNT) {
			// 任务分割
			executorService.execute(new EastMoneyCrawlerThread(datas, division(codes, i, THREAD_MAX_LOAD_CODE_COUNT)));
		}
		executorService.shutdown();
		// 守护线程全部执行完
		while (!executorService.isTerminated()) {
			Thread.sleep(200);
		}
		return datas;
	}

	private List<StockCode> findStockCodes(StockMarketEnum stockMarket, boolean filterEp) throws Exception {
		Document doc = CrawlerUtil.get(INDEX_URL);
		int initArraySize = 5000;
		String cssQuery;
		if (StockMarketEnum.上海.equals(stockMarket)) {
			cssQuery = "div.sltit a[name='sh']";
			initArraySize = 2000;
		} else if (StockMarketEnum.深圳.equals(stockMarket)) {
			cssQuery = "div.sltit a[name='sz']";
			initArraySize = 3000;
		} else {
			cssQuery = "div.sltit a[name^='s']";
		}
		Elements es = doc.select(cssQuery);
		List<StockCode> stockCodes = new ArrayList<>(initArraySize);
		for (Element e : es) {
			Elements codes = e.parent().nextElementSibling().select("li a");
			StockCode stockCode;
			for (Element code : codes) {
				stockCode = parse(code, filterEp);
				if (stockCode != null) {
					stockCodes.add(stockCode);
				}
			}
		}
		return stockCodes;
	}

	private StockCode parse(Element code, boolean filterEp) {
		String url = code.absUrl("href");
		String text = code.text();
		if (url == null || "".equals(url) || text == null || "".equals(text)) {
			return null;
		} else {
			int sIndex = text.lastIndexOf("(");
			int eIndex = text.lastIndexOf(")");
			if (sIndex == -1 || eIndex == -1) {
				System.err.println("异常代码：" + text + "，详情链接：" + url);
				return null;
			} else {
				String stockCode = text.substring(sIndex + 1, eIndex);
				if (filterEp && stockCode.startsWith("0")) {
					// 过滤创业板
					return null;
				}
				return new StockCode(text.substring(0, sIndex), stockCode, url);
			}
		}
	}

	/**
	 * 集合分割
	 * 
	 * @param list
	 *            原集合数据
	 * @param startIndex
	 *            分割开始下标
	 * @param size
	 *            分割长度
	 */
	private <T> List<T> division(List<T> list, int startIndex, int size) {
		List<T> result = new ArrayList<>(size);
		for (int i = startIndex; i < startIndex + size && i < list.size(); i++) {
			result.add(list.get(i));
		}
		return result;
	}

	/**
	 * 东方财富爬虫线程
	 */
	class EastMoneyCrawlerThread implements Runnable {

		private List<StockCode> codes;
		private List<StockData> datas;

		public EastMoneyCrawlerThread(List<StockData> datas, List<StockCode> codes) {
			this.datas = datas;
			this.codes = codes;
		}

		@Override
		public void run() {
			List<StockData> result = findStockData(codes);
			if (result != null && !result.isEmpty()) {
				datas.addAll(result);
			}
		}

		private List<StockData> findStockData(List<StockCode> codes) {
			List<StockData> result = new ArrayList<>(codes.size());
			StockData data;
			for (StockCode stockCode : codes) {
				try {
					data = findStockData(stockCode);
					if (data != null) {
						result.add(data);
					}
				} catch (Exception e) {
					LogUtil.error("查询" + stockCode.getCode() + "发生错误！", e);
				}
			}
			return result;
		}

		private StockData findStockData(StockCode code) throws Exception {
			this.queryStockId(code);
			LogUtil.info("正在加载：" + code.toString());
			if (code.getStockId() == null) {
				return null;
			}
			StockData data = new StockData(code);
			// 查询详情数据
			String stockInfo = CrawlerUtil.get(STOCK_INFO_URL.replace("{id}", code.getStockId()), null);
			stockInfo = stockInfo.replace("callback(", "").replace(")", "");
			JSONObject json = JSONObject.parseObject(stockInfo);
			JSONArray array = json.getJSONArray("Value");
			StockEnum values[] = StockEnum.values();
			for (StockEnum stockEnum : values) {
				data.set(stockEnum, ofNumber(array.getString(index(stockEnum))));
			}
			data.setTotalMoney(array.getString(35));
			data.setBuyTrade(stockTrade(array, 3, 7, 13, StockTrade.BUY));
			data.setSellTrade(stockTrade(array, 8, 12, 18, StockTrade.SELL));
			// 查询板块信息
			data.setTypes(stockTypes(code));
			return data;
		}

		private List<String> stockTypes(StockCode code) {
			try {
				String stockInfo = CrawlerUtil
						.get(STOCK_TYPE_INFO_URL.replace("{token}", token).replace("{id}", code.getStockId()), null);
				stockInfo = stockInfo.replace("callback(", "").replace(")", "");
				JSONArray array = JSONArray.parseArray(stockInfo);
				List<String> result = new ArrayList<>(array.size());
				String[] arr;
				for (int i = 0; i < array.size(); i++) {
					arr = array.getString(i).split(",");
					result.add(arr[2]);
				}
				return result;
			} catch (Exception e) {
				LogUtil.error("获取板块信息错误！code = " + code, e);
			}
			return null;
		}

		private List<StockTrade> stockTrade(JSONArray array, int ps, int pe, int hs, String tradeType) {
			int he = hs - ps;
			List<StockTrade> result = new ArrayList<>(pe - ps + 1);
			StockTrade obj;
			for (int i = ps; i <= pe; i++) {
				obj = new StockTrade();
				obj.setPrice(ofNumber(array.getString(i)).doubleValue());
				obj.setHandCount(ofNumber(array.getString(i + he)).longValue());
				obj.setTradeType(tradeType);
				result.add(obj);
			}
			return result;
		}

		private void queryStockId(StockCode code) throws Exception {
			Document doc = CrawlerUtil.get(code.getUrl());
			if (token == null || "".equals(token)) {
				parseToken(doc);
			}
			Elements es = doc.select("meta[name='mobile-agent']");
			if (es == null || es.size() == 0) {
				return;
			}
			Element e = es.get(0);
			String content = e.attr("content");
			int sIndex = content.lastIndexOf("/");
			int eIndex = content.indexOf(".html", sIndex);
			code.setStockId(content.substring(sIndex + 1, eIndex));
		}

		private void parseToken(Document doc) {
			Elements es = doc.select("#picr");
			if (es != null && es.size() > 0) {
				Element e = es.get(0);
				String src = e.absUrl("src");
				int sIndex = src.indexOf("token=");
				int eIndex = src.indexOf("&", sIndex + 1);
				if (sIndex != -1) {
					if (eIndex == -1) {
						token = src.substring(sIndex).replace("token=", "");
					} else {
						token = src.substring(sIndex, eIndex).replace("token=", "");
					}
				}
			}
		}

		private Number ofNumber(String value) {
			if (value == null || "".equals(value) || "-".equals(value)) {
				return 0;
			} else if (value.length() > 5 && value.indexOf(".") == -1) {
				return Long.parseLong(value);
			} else {
				return Double.parseDouble(value);
			}
		}

		private int index(StockEnum stockEnum) {
			switch (stockEnum) {
			case 开盘价格:
				return 28;
			case 当前价格:
				return 25;
			case 市净率:
				return 43;
			case 市盈率:
				return 38;
			case 换手率:
				return 37;
			case 量比:
				return 36;
			case 内盘:
				return 40;
			case 外盘:
				return 39;
			case 均价:
				return 26;
			case 总手:
				return 31;
			case 昨收:
				return 34;
			case 最低价:
				return 32;
			case 最高价:
				return 30;
			case 涨幅:
				return 29;
			case 涨跌:
				return 27;
			default:
				throw new RuntimeException("未定义：" + stockEnum);
			}
		}
	}

}
