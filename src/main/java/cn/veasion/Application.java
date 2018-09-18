package cn.veasion;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;

import cn.veasion.bean.ChangeEnum;
import cn.veasion.bean.StockCode;
import cn.veasion.bean.StockData;
import cn.veasion.bean.StockEnum;
import cn.veasion.bean.StockMarketEnum;
import cn.veasion.filter.Filters;
import cn.veasion.service.StockQuery;
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
		List<StockData> result = StockQuery.query(StockMarketEnum.沪深)
				.filter(Filters.range(StockEnum.当前价格, 3, 30))
				.filter(Filters.range(StockEnum.市净率, 0.1, 3))
				.filter(Filters.range(StockEnum.市盈率, 0, 60))
				.filter(Filters.change(ChangeEnum.下跌))
				.get();
		// 股票代码
		List<StockCode> stockCodes = result.stream().map(sd -> sd.getStockCode()).collect(Collectors.toList());
		// 保存到桌面
		File file = new File(TxtFileUtil.HOME_PATH + "\\StockCode.json");
		for (StockCode stockCode : stockCodes) {
			TxtFileUtil.writeTxtFile(file, JSONObject.toJSONString(stockCode) + "\r\n", true);
		}
		System.out.println("代码：" + stockCodes.size());
		stockCodes.forEach(System.out::println);
		System.out.println("查询结果数据路径：" + file.getPath());
	}

}
