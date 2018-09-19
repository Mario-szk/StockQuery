package cn.veasion;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import cn.veasion.bean.ChangeEnum;
import cn.veasion.bean.StockCode;
import cn.veasion.bean.StockData;
import cn.veasion.bean.StockEnum;
import cn.veasion.bean.StockMarketEnum;
import cn.veasion.filter.Filters;
import cn.veasion.service.StockQuery;
import cn.veasion.util.StockUtil;
import cn.veasion.util.TxtFileUtil;

/**
 * 股票数据爬虫
 * 
 * @author zhuowei.luo
 */
public class Application {

	public static void main(String[] args) throws Exception {
		// 开启缓存
		StockQuery.DATA_CACHE = true;
		// 搜索过滤股票
		List<StockData> result = StockQuery.query(StockMarketEnum.沪深, true)
				.filter(Filters.range(StockEnum.当前价格, 3, 30))
				.filter(Filters.range(StockEnum.市净率, 0.1, 3))
				.filter(Filters.range(StockEnum.市盈率, 0, 60))
				.filter(Filters.type("计算机", "互联网", "人工智能", "量子通信", "机器人", "医药", "石墨烯", "军工", "食品", "饮料", "白马股"))
				.filter(Filters.change(ChangeEnum.下跌))
				.get();
		// 股票数据保存到桌面
		File stockDataFile = StockUtil.saveStockData(result, TxtFileUtil.HOME_PATH + "\\StockData.json");
		// 股票代码
		List<StockCode> stockCodes = result.stream().map(sd -> sd.getStockCode()).collect(Collectors.toList());
		String stockInfo = StockUtil.toString(result);
		// 股票代码保存到桌面
		File stockCodeFile = StockUtil.save(stockInfo, TxtFileUtil.HOME_PATH + "\\StockInfo.txt");
		System.out.println("代码：" + stockCodes.size());
		System.out.println(stockInfo);
		System.out.println("股票结果路径：" + stockCodeFile.getPath());
		System.out.println("股票数据结果路径：" + stockDataFile.getPath());
	}

}
