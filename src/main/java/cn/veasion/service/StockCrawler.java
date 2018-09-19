package cn.veasion.service;

import java.util.List;

import cn.veasion.bean.StockData;
import cn.veasion.bean.StockMarketEnum;

/**
 * 股票数据爬虫接口
 * 
 * @author zhuowei.luo
 */
@FunctionalInterface
public interface StockCrawler {

	/**
	 * 最大线程数
	 */
	public static final Integer THREAD_POOL_SIZE = 20;

	/**
	 * 线程最大加载股票数量
	 */
	public static final Integer THREAD_MAX_LOAD_CODE_COUNT = 50;

	/**
	 * 获取数据
	 * 
	 * @param stockMarket 股市类型
	 * @param filterEp 是否过滤创业板块
	 */
	List<StockData> findData(StockMarketEnum stockMarket, boolean filterEp) throws Exception;

}
