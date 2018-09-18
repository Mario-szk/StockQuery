package cn.veasion.bean;

import java.io.Serializable;

/**
 * 交易信息
 * 
 * @author zhuowei.luo
 */
public class StockTrade implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String SELL = "sell";
	public static final String BUY = "buy";

	private double price;
	private Long handCount;
	private String tradeType;

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Long getHandCount() {
		return handCount;
	}

	public void setHandCount(Long handCount) {
		this.handCount = handCount;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	@Override
	public String toString() {
		return "StockTrade [price=" + price + ", handCount=" + handCount + ", tradeType=" + tradeType + "]";
	}

}
