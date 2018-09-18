package cn.veasion.service;

/**
 * 爬虫工厂
 * 
 * @author zhuowei.luo
 */
public class StockCrawlerFactory {

	public static Integer CRAWLER_EAST_MONEY = 1;

	public static StockCrawler defaultCrawler() {
		return new EastMoneyCrawler();
	}

	public static StockCrawler stockCrawler(Integer crawler) {
		if (CRAWLER_EAST_MONEY.equals(crawler)) {
			return new EastMoneyCrawler();
		} else {
			return defaultCrawler();
		}
	}

}
