package cn.veasion.util;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 爬虫工具类
 * 
 * @author zhuowei.luo
 */
public class CrawlerUtil {

	public static Document get(String url) throws IOException {
		Connection conn = Jsoup.connect(url);
		HttpClientUtils.baseHeader().forEach((k, v) -> {
			conn.header(k, v);
		});
		Document doc = conn.timeout(HttpClientUtils.CONN_TIMEOUT).get();
		// Response resp = conn.response();
		// System.out.println(resp.cookies());
		return doc;
	}

	public static String get(String url, Map<String, String> headers) throws Exception {
		Map<String, String> map = HttpClientUtils.baseHeader();
		if (headers != null && !headers.isEmpty()) {
			map.putAll(headers);
		}
		return HttpClientUtils.get(url, map);
	}

}
