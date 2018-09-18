package cn.veasion.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.veasion.bean.StockData;
import cn.veasion.bean.StockMarketEnum;

/**
 * 缓存数据工具类
 * 
 * @author zhuowei.luo
 */
public class CacheUtil {

	private static final String FORMAT = "yyyyMMddHH";
	private static final String FORMAT_OVER = "yyyyMMdd15";

	/**
	 * 本地缓存获取数据 
	 */
	public static List<StockData> get(StockMarketEnum stockMarket) {
		Calendar c = Calendar.getInstance();
		File f = cachefile(stockMarket, c);
		LogUtil.info(f.getPath());
		if (!f.exists() || !f.isFile()) {
			c.add(Calendar.HOUR, -1);
			f = cachefile(stockMarket, c);
		}
		if (f.exists() && f.isFile()) {
			List<StockData> result = new ArrayList<>();
			TxtFileUtil.readTxtFile(f, (line, count) -> {
				if (line != null && line.length() > 0) {
					StockData data = JSONObject.parseObject(line, StockData.class);
					result.add(data);
				}
			});
			LogUtil.info("读取缓存成功，数量：" + result.size());
			return result;
		} else {
			return null;
		}
	}

	/**
	 * 数据缓存到本地 
	 */
	public static boolean set(StockMarketEnum stockMarket, List<StockData> datas) {
		File f = cachefile(stockMarket, Calendar.getInstance());
		LogUtil.info(f.getPath());
		if (f.exists()) {
			f.delete();
		}
		if (datas != null && !datas.isEmpty()) {
			for (StockData stockData : datas) {
				String line = JSONObject.toJSONString(stockData) + "\r\n";
				boolean suc = TxtFileUtil.writeTxtFile(f, line, true);
				if (!suc) {
					LogUtil.error("缓存数据失败！" + line);
				}
			}
		}
		return true;
	}

	private static File cachefile(StockMarketEnum stockMarket, Calendar c) {
		StringBuilder sb = new StringBuilder();
		sb.append(CacheUtil.class.getClassLoader().getResource("").getPath());
		SimpleDateFormat sdf;
		if (c.get(Calendar.HOUR_OF_DAY) >= 15) {
			sdf = new SimpleDateFormat(FORMAT_OVER);
		} else {
			sdf = new SimpleDateFormat(FORMAT);
		}
		sb.append("stock_").append(stockMarket.getCode()).append("_");
		sb.append(sdf.format(c.getTime())).append(".data");
		return new File(sb.toString());
	}

}
