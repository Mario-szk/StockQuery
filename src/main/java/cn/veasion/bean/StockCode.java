package cn.veasion.bean;

import java.io.Serializable;

/**
 * 股票代码
 * 
 * @author zhuowei.luo
 */
public class StockCode implements Serializable {

	private static final long serialVersionUID = 1L;

	private String stockId; // 股票id
	private String name; // 公司名称
	private String code; // 股市代码
	private String url; // 详情链接

	public StockCode() {
	}

	public StockCode(String name, String code, String url) {
		this.name = name;
		this.code = code;
		this.url = url;
	}

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "StockCode [stockId=" + stockId + ", name=" + name + ", code=" + code + ", url=" + url + "]";
	}

}
