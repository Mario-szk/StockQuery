package cn.veasion.bean;

/**
 * 股市枚举
 * 
 * @author zhuowei.luo
 */
public enum StockMarketEnum {

	/**
	 * 沪深：上海股票、深圳股票
	 */
	沪深("hs"),

	/**
	 * 上海股票
	 */
	上海("sh"),

	/**
	 * 深圳股票
	 */
	深圳("sz");

	private String code;

	private StockMarketEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
