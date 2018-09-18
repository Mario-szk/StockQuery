package cn.veasion.filter;

import cn.veasion.bean.ChangeEnum;
import cn.veasion.bean.StockData;

/**
 * 上涨/下跌过滤
 * 
 * @author zhuowei.luo
 */
public class ChangeFilter implements StockFilter {

	private ChangeEnum change;

	public ChangeFilter(ChangeEnum change) {
		this.change = change;
	}

	@Override
	public boolean filter(StockData data) {
		if (change == null) {
			return true;
		} else if (ChangeEnum.上涨.equals(change)) {
			return data.getFirstPrice() < data.getPrice();
		} else if (ChangeEnum.涨停.equals(change)) {
			return data.getChangePriceRatio() >= 10;
		} else if (ChangeEnum.下跌.equals(change)) {
			return data.getFirstPrice() > data.getPrice();
		} else if (ChangeEnum.跌停.equals(change)) {
			return data.getChangePriceRatio() <= -10;
		} else if (ChangeEnum.不变.equals(change)) {
			return data.getFirstPrice() == data.getPrice();
		}
		return true;
	}

}
