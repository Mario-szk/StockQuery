package cn.veasion.filter;

import cn.veasion.bean.StockData;

/**
 * 过滤接口
 * 
 * @author zhuowei.luo
 */
@FunctionalInterface
public interface StockFilter {

	boolean filter(StockData stockData);
	
}
