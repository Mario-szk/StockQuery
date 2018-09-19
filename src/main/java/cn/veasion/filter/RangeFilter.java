package cn.veasion.filter;

import cn.veasion.bean.StockData;
import cn.veasion.bean.StockEnum;

/**
 * 范围过滤 
 * 
 * @author zhuowei.luo
 */
public class RangeFilter implements StockFilter {

	private StockEnum stock;
	private double min;
	private double max;

	public RangeFilter(StockEnum stock, Number min, Number max) {
		this(stock, min != null ? min.doubleValue() : null, max != null ? max.doubleValue() : null);
	}

	private RangeFilter(StockEnum stock, Double min, Double max) {
		if (min == null) {
			min = 0d;
		}
		if (max == null) {
			max = 10000d;
		}
		this.min = min;
		this.max = max;
		this.stock = stock;
	}

	@Override
	public boolean filter(StockData stockData) {
		double value = stockData.get(stock).doubleValue();
		return value >= min && value <= max;
	}

}
