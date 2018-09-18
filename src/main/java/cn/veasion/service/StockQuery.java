package cn.veasion.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cn.veasion.bean.StockData;
import cn.veasion.bean.StockMarketEnum;
import cn.veasion.filter.StockFilter;
import cn.veasion.util.CacheUtil;
import cn.veasion.util.LogUtil;

/**
 * 股票查询
 * 
 * @author zhuowei.luo
 */
public class StockQuery {

	/**
	 * 开启缓存（小于15点一个小时有效，否则已经收盘为当天有效） 
	 */
	public static boolean DATA_CACHE = false;
	
	private Stream<StockData> stream;

	public StockQuery(StockMarketEnum stockMarket) throws Exception {
		this(StockCrawlerFactory.defaultCrawler(), stockMarket);
	}

	public StockQuery(StockCrawler stockCrawler, StockMarketEnum stockMarket) throws Exception {
		List<StockData> list = null;
		if (DATA_CACHE) {
			list = CacheUtil.get(stockMarket);
			if (list == null) {
				LogUtil.info("未命中缓存，正在加载数据，请稍后...");
				list = stockCrawler.findData(stockMarket);
				CacheUtil.set(stockMarket, list);
			}
			stream = list.stream();
		} else {
			LogUtil.info("正在加载数据，请稍后...");
			list = stockCrawler.findData(stockMarket);
			stream = list.stream();
		}
	}

	public StockQuery filter(StockFilter stockFilter) {
		stream = stream.filter(data -> stockFilter.filter(data));
		return this;
	}

	public List<StockData> get() {
		return stream.collect(Collectors.toList());
	}

	public static StockQuery query(StockMarketEnum stockMarket) throws Exception {
		return new StockQuery(stockMarket);
	}

	public static StockQuery query(StockCrawler stockCrawler, StockMarketEnum stockMarket) throws Exception {
		return new StockQuery(stockCrawler, stockMarket);
	}

	public static List<StockData> query(StockMarketEnum stockMarket, StockFilter... stockFilters) throws Exception {
		List<StockData> list = StockCrawlerFactory.defaultCrawler().findData(stockMarket);
		Stream<StockData> stream = list.stream();
		if (stockFilters != null && stockFilters.length > 0) {
			for (StockFilter stockFilter : stockFilters) {
				stream = stream.filter(data -> stockFilter.filter(data));
			}
		}
		return stream.collect(Collectors.toList());
	}

}
