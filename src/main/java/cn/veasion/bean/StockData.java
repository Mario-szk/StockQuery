package cn.veasion.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 股票数据
 * 
 * @author zhuowei.luo
 */
public class StockData implements Serializable {

	private static final long serialVersionUID = 1L;

	private StockCode stockCode; // 股票代码

	private double firstPrice; // 开盘价格
	private double yesterdayPrice; // 昨收价格
	private double price; // 当前价格
	private double disparity; // 涨跌
	private double changePriceRatio; // 涨幅 %
	private double avgPrice; // 均价
	private double higPrice; // 最高价格
	private double lowPrice; // 最低价格
	private Long totalHand; // 总手
	private double pbRatio; // 市净率
	private double peRatio; // 市盈率
	private double turnoverRatio; // 换手率 %
	private double volumeRatio; // 量比
	private Long outerDisc; // 外盘
	private Long insideDisc; // 内盘

	private String totalMoney; // 金额

	private List<StockTrade> sellTrade; // 卖：交易
	private List<StockTrade> buyTrade; // 买：交易

	private List<String> types; // 板块

	public StockData() {
	}

	public StockData(StockCode stockCode) {
		this.stockCode = stockCode;
	}

	/**
	 * 股票代码
	 */
	public StockCode getStockCode() {
		return stockCode;
	}

	/**
	 * 股票代码
	 */
	public void setStockCode(StockCode stockCode) {
		this.stockCode = stockCode;
	}

	/**
	 * 开盘价格
	 */
	public double getFirstPrice() {
		return firstPrice;
	}

	/**
	 * 开盘价格
	 */
	public void setFirstPrice(double firstPrice) {
		this.firstPrice = firstPrice;
	}

	/**
	 * 昨收价格
	 */
	public double getYesterdayPrice() {
		return yesterdayPrice;
	}

	/**
	 * 昨收价格
	 */
	public void setYesterdayPrice(double yesterdayPrice) {
		this.yesterdayPrice = yesterdayPrice;
	}

	/**
	 * 当前价格
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * 当前价格
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * 涨跌
	 */
	public double getDisparity() {
		return disparity;
	}

	/**
	 * 涨跌
	 */
	public void setDisparity(double disparity) {
		this.disparity = disparity;
	}

	/**
	 * 涨幅 %
	 */
	public double getChangePriceRatio() {
		return changePriceRatio;
	}

	/**
	 * 涨幅 %
	 */
	public void setChangePriceRatio(double changePriceRatio) {
		this.changePriceRatio = changePriceRatio;
	}

	/**
	 * 均价
	 */
	public double getAvgPrice() {
		return avgPrice;
	}

	/**
	 * 均价
	 */
	public void setAvgPrice(double avgPrice) {
		this.avgPrice = avgPrice;
	}

	/**
	 * 最高价格
	 */
	public double getHigPrice() {
		return higPrice;
	}

	/**
	 * 最高价格
	 */
	public void setHigPrice(double higPrice) {
		this.higPrice = higPrice;
	}

	/**
	 * 最低价格
	 */
	public double getLowPrice() {
		return lowPrice;
	}

	/**
	 * 最低价格
	 */
	public void setLowPrice(double lowPrice) {
		this.lowPrice = lowPrice;
	}

	/**
	 * 总手
	 */
	public Long getTotalHand() {
		return totalHand;
	}

	/**
	 * 总手
	 */
	public void setTotalHand(Long totalHand) {
		this.totalHand = totalHand;
	}

	/**
	 * 市净率
	 */
	public double getPbRatio() {
		return pbRatio;
	}

	/**
	 * 市净率
	 */
	public void setPbRatio(double pbRatio) {
		this.pbRatio = pbRatio;
	}

	/**
	 * 市盈率
	 */
	public double getPeRatio() {
		return peRatio;
	}

	/**
	 * 市盈率
	 */
	public void setPeRatio(double peRatio) {
		this.peRatio = peRatio;
	}

	/**
	 * 换手率 %
	 */
	public double getTurnoverRatio() {
		return turnoverRatio;
	}

	/**
	 * 换手率 %
	 */
	public void setTurnoverRatio(double turnoverRatio) {
		this.turnoverRatio = turnoverRatio;
	}

	/**
	 * 量比
	 */
	public double getVolumeRatio() {
		return volumeRatio;
	}

	/**
	 * 量比
	 */
	public void setVolumeRatio(double volumeRatio) {
		this.volumeRatio = volumeRatio;
	}

	/**
	 * 金额
	 */
	public String getTotalMoney() {
		return totalMoney;
	}

	/**
	 * 金额
	 */
	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	/**
	 * 外盘
	 */
	public Long getOuterDisc() {
		return outerDisc;
	}

	/**
	 * 外盘
	 */
	public void setOuterDisc(Long outerDisc) {
		this.outerDisc = outerDisc;
	}

	/**
	 * 内盘
	 */
	public Long getInsideDisc() {
		return insideDisc;
	}

	/**
	 * 内盘
	 */
	public void setInsideDisc(Long insideDisc) {
		this.insideDisc = insideDisc;
	}

	/**
	 * 卖：交易
	 */
	public List<StockTrade> getSellTrade() {
		return sellTrade;
	}

	/**
	 * 卖：交易
	 */
	public void setSellTrade(List<StockTrade> sellTrade) {
		this.sellTrade = sellTrade;
	}

	/**
	 * 买：交易
	 */
	public List<StockTrade> getBuyTrade() {
		return buyTrade;
	}

	/**
	 * 买：交易
	 */
	public void setBuyTrade(List<StockTrade> buyTrade) {
		this.buyTrade = buyTrade;
	}

	/**
	 * 板块
	 */
	public List<String> getTypes() {
		return types;
	}

	/**
	 * 板块
	 */
	public void setTypes(List<String> types) {
		this.types = types;
	}

	public double get(StockEnum stockEnum) {
		switch (stockEnum) {
		case 开盘价格:
			return firstPrice;
		case 当前价格:
			return price;
		case 市净率:
			return pbRatio;
		case 市盈率:
			return peRatio;
		case 换手率:
			return turnoverRatio;
		case 量比:
			return volumeRatio;
		case 内盘:
			return insideDisc.doubleValue();
		case 外盘:
			return outerDisc.doubleValue();
		case 均价:
			return avgPrice;
		case 总手:
			return totalHand.doubleValue();
		case 昨收:
			return yesterdayPrice;
		case 最低价:
			return lowPrice;
		case 最高价:
			return higPrice;
		case 涨幅:
			return changePriceRatio;
		case 涨跌:
			return disparity;
		default:
			throw new RuntimeException("未定义：" + stockEnum);
		}
	}

	public void set(StockEnum stockEnum, Number value) {
		switch (stockEnum) {
		case 开盘价格:
			this.firstPrice = value.doubleValue();
			break;
		case 当前价格:
			this.price = value.doubleValue();
			break;
		case 市净率:
			this.pbRatio = value.doubleValue();
			break;
		case 市盈率:
			this.peRatio = value.doubleValue();
			break;
		case 换手率:
			this.turnoverRatio = value.doubleValue();
			break;
		case 量比:
			this.volumeRatio = value.doubleValue();
			break;
		case 内盘:
			this.insideDisc = value.longValue();
			break;
		case 外盘:
			this.outerDisc = value.longValue();
			break;
		case 均价:
			this.avgPrice = value.doubleValue();
			break;
		case 总手:
			this.totalHand = value.longValue();
			break;
		case 昨收:
			this.yesterdayPrice = value.doubleValue();
			break;
		case 最低价:
			this.lowPrice = value.doubleValue();
			break;
		case 最高价:
			this.higPrice = value.doubleValue();
			break;
		case 涨幅:
			this.changePriceRatio = value.doubleValue();
			break;
		case 涨跌:
			this.disparity = value.doubleValue();
			break;
		default:
			throw new RuntimeException("未定义：" + stockEnum);
		}
	}

	@Override
	public String toString() {
		return "StockData [stockCode=" + stockCode + ", firstPrice=" + firstPrice + ", yesterdayPrice=" + yesterdayPrice
				+ ", price=" + price + ", disparity=" + disparity + ", changePriceRatio=" + changePriceRatio
				+ ", avgPrice=" + avgPrice + ", higPrice=" + higPrice + ", lowPrice=" + lowPrice + ", totalHand="
				+ totalHand + ", pbRatio=" + pbRatio + ", peRatio=" + peRatio + ", turnoverRatio=" + turnoverRatio
				+ ", volumeRatio=" + volumeRatio + ", totalMoney=" + totalMoney + ", outerDisc=" + outerDisc
				+ ", insideDisc=" + insideDisc + ", sellTrade=" + sellTrade + ", buyTrade=" + buyTrade + ", types="
				+ types + "]";
	}

}
