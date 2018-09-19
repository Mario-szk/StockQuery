package cn.veasion.filter;

import cn.veasion.bean.ChangeEnum;
import cn.veasion.bean.StockEnum;

/**
 * 过滤器
 * 
 * @author zhuowei.luo
 */
public class Filters {

	/**
	 * 条件范围过滤
	 */
	public static RangeFilter range(StockEnum stock, Number min, Number max) {
		return new RangeFilter(stock, min, max);
	}

	/**
	 * 上涨、下跌过滤
	 */
	public static ChangeFilter change(ChangeEnum change) {
		return new ChangeFilter(change);
	}

	/**
	 * 股票板块过滤
	 * 
	 * @param keywords 板块包含的关键字
	 */
	public static StockTypeFilter type(String... keywords) {
		return new StockTypeFilter(keywords);
	}
	
}
