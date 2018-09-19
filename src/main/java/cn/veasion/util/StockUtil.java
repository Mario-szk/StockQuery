package cn.veasion.util;

import java.io.File;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.veasion.bean.StockCode;
import cn.veasion.bean.StockData;
import cn.veasion.bean.StockEnum;

/**
 * 股票工具类
 * 
 * @author zhuowei.luo
 */
public class StockUtil {

	/**
	 * 保存文本
	 * 
	 * @param txt 内容
	 * @param path 保存路径
	 */
	public static File save(String txt, String path) {
		// 保存到桌面
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
		TxtFileUtil.writeTxtFile(file, txt, false);
		return file;
	}
	
	/**
	 * 保存股票代码
	 * 
	 * @param stockCodes 股票代码
	 * @param path 保存路径
	 */
	public static File saveStockCode(List<StockCode> stockCodes, String path) {
		// 保存到桌面
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
		for (StockCode stockCode : stockCodes) {
			TxtFileUtil.writeTxtFile(file, JSONObject.toJSONString(stockCode) + "\r\n", true);
		}
		return file;
	}

	/**
	 * 保存股票数据
	 * 
	 * @param stockCodes 股票代码
	 * @param path 保存路径
	 */
	public static File saveStockData(List<StockData> stockDatas, String path) {
		// 保存到桌面
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
		for (StockData stockData : stockDatas) {
			TxtFileUtil.writeTxtFile(file, JSONObject.toJSONString(stockData) + "\r\n", true);
		}
		return file;
	}

	public static String toString(List<StockData> stockDatas) {
		StringBuilder sb = new StringBuilder();
		for (StockData stockData : stockDatas) {
			toString(sb, stockData);
			sb.append("\r\n");
		}
		return sb.toString();
	}

	public static String toString(StockData stockData) {
		StringBuilder sb = new StringBuilder();
		toString(new StringBuilder(), stockData);
		return sb.toString();
	}

	private static void toString(StringBuilder sb, StockData stockData) {
		StockCode code = stockData.getStockCode();
		sb.append(code.getName()).append("(").append(code.getCode()).append(")：");
		sb.append("最新 ").append(stockData.get(StockEnum.当前价格)).append(", ");
		sb.append("涨幅 ").append(stockData.get(StockEnum.涨幅)).append("%, ");
		sb.append("均价 ").append(stockData.get(StockEnum.均价)).append(", ");
		sb.append("市净率 ").append(stockData.get(StockEnum.市净率)).append(", ");
		sb.append("市盈率 ").append(stockData.get(StockEnum.市盈率)).append(", ");
		sb.append("换手率 ").append(stockData.get(StockEnum.换手率)).append("%, ");
		sb.append("总手 ").append(stockData.get(StockEnum.总手)).append(", ");
		sb.append("外盘 ").append(stockData.get(StockEnum.外盘)).append(", ");
		sb.append("内盘 ").append(stockData.get(StockEnum.内盘)).append(", ");
		sb.append("量比 ").append(stockData.get(StockEnum.量比)).append(", ");
		sb.append("金额 ").append(stockData.getTotalMoney()).append(", ");
		sb.append("板块 ").append(stockData.getTypes());
	}
}
