package cn.veasion.filter;

import java.util.List;

import cn.veasion.bean.StockData;

/**
 * 股票板块过滤
 * 
 * @author zhuowei.luo
 */
public class StockTypeFilter implements StockFilter {

	private String[] keywords;

	/**
	 * 股票板块过滤
	 * 
	 * @param keywords 板块包含的关键字
	 */
	public StockTypeFilter(String... keywords) {
		this.keywords = keywords;
	}

	@Override
	public boolean filter(StockData stockData) {
		if (keywords != null && keywords.length > 0) {
			List<String> types = stockData.getTypes();
			if (types != null) {
				String typeStr = types.toString();
				for (String key : keywords) {
					if (typeStr.contains(key)) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
