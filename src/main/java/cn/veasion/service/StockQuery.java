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

	/**
	 * 股票查询
	 * 
	 * @param stockMarket 股市类型
	 */
	public StockQuery(StockMarketEnum stockMarket) throws Exception {
		this(stockMarket, false);
	}

	/**
	 * 股票查询
	 * 
	 * @param stockMarket 股市类型
	 * @param filterEp 是否过滤创业板
	 */
	public StockQuery(StockMarketEnum stockMarket, boolean filterEp) throws Exception {
		this(StockCrawlerFactory.defaultCrawler(), stockMarket, filterEp);
	}

	/**
	 * 股票查询
	 * 
	 * @param stockCrawler 股市爬虫
	 * @param stockMarket 股市类型
	 * @param filterEp 是否过滤创业板
	 */
	public StockQuery(StockCrawler stockCrawler, StockMarketEnum stockMarket, boolean filterEp) throws Exception {
		List<StockData> list = null;
		if (DATA_CACHE) {
			list = CacheUtil.get(stockMarket);
			if (list == null) {
				LogUtil.info("未命中缓存，正在加载数据，请稍后...");
				list = stockCrawler.findData(stockMarket, filterEp);
				CacheUtil.set(stockMarket, list);
			}
			stream = list.stream();
		} else {
			LogUtil.info("正在加载数据，请稍后...");
			list = stockCrawler.findData(stockMarket, filterEp);
			stream = list.stream();
		}
	}

	/**
	 * 股票过滤
	 * 
	 * @param stockFilter 条件过滤器
	 */
	public StockQuery filter(StockFilter stockFilter) {
		stream = stream.filter(data -> stockFilter.filter(data));
		return this;
	}

	/**
	 * 股票过滤（或关系，满足一个即放行）
	 * 
	 * @param stockFilters 条件过滤器（filter1 or filter2）
	 */
	public StockQuery orFilter(StockFilter... stockFilters) {
		stream = stream.filter(data -> {
			for (StockFilter stockFilter : stockFilters) {
				if (stockFilter.filter(data)) {
					return true;
				}
			}
			return false;
		});
		return this;
	}
	
	/**
	 * 获取过滤后的股票数据 
	 */
	public List<StockData> get() {
		return stream.collect(Collectors.toList());
	}

	/**
	 * 股票查询
	 * 
	 * @param stockMarket 股市类型
	 */
	public static StockQuery query(StockMarketEnum stockMarket) throws Exception {
		return new StockQuery(stockMarket);
	}
	
	/**
	 * 股票查询
	 * 
	 * @param stockMarket 股市类型
	 * @param filterEp 是否过滤创业板
	 */
	public static StockQuery query(StockMarketEnum stockMarket, boolean filterEp) throws Exception {
		return new StockQuery(stockMarket, filterEp);
	}

	/**
	 * 股票查询
	 * 
	 * @param stockCrawler 股市爬虫
	 * @param stockMarket 股市类型
	 * @param filterEp 是否过滤创业板
	 */
	public static StockQuery query(StockCrawler stockCrawler, StockMarketEnum stockMarket, boolean filterEp) throws Exception {
		return new StockQuery(stockCrawler, stockMarket, filterEp);
	}

	/**
	 * 股票查询
	 * 
	 * @param stockCrawler 股市爬虫
	 * @param stockMarket 股市类型
	 * @param filterEp 是否过滤创业板
	 * @param stockFilters 股票条件过滤器
	 */
	public static List<StockData> query(StockMarketEnum stockMarket, boolean filterEp, StockFilter... stockFilters) throws Exception {
		List<StockData> list = StockCrawlerFactory.defaultCrawler().findData(stockMarket, filterEp);
		Stream<StockData> stream = list.stream();
		if (stockFilters != null && stockFilters.length > 0) {
			for (StockFilter stockFilter : stockFilters) {
				stream = stream.filter(data -> stockFilter.filter(data));
			}
		}
		return stream.collect(Collectors.toList());
	}

}
