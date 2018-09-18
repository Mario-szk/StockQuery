package cn.veasion.util;

import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

/**
 * Http Client Utils
 * 
 * @author zhuowei.luo
 */
public class HttpClientUtils {

	public static final int CONN_TIMEOUT = 8000;
	public static final int READ_TIMEOUT = 8000;
	public static final String CHARSET_DEFAULT = "UTF-8";
	private static HttpClient client = null;

	static {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(128);
		cm.setDefaultMaxPerRoute(128);
		client = HttpClients.custom().setConnectionManager(cm).build();
	}

	/**
	 * post请求
	 * 
	 * @param url url
	 * @param body body
	 */
	public static String post(String url, String body)
			throws ConnectTimeoutException, SocketTimeoutException, Exception {
		return post(url, body, "application/x-www-form-urlencoded", CHARSET_DEFAULT, CONN_TIMEOUT, READ_TIMEOUT);
	}

	/**
	 * post请求
	 * 
	 * @param url url
	 * @param body body
	 * @param charset 编码，默认UTF-8
	 * @param connTimeout 连接超时时间(毫秒)，可空
	 * @param readTimeout 读取超时时间(毫秒)，可空
	 */
	public static String post(String url, String body, String charset, Integer connTimeout, Integer readTimeout)
			throws ConnectTimeoutException, SocketTimeoutException, Exception {
		return post(url, body, "application/x-www-form-urlencoded", charset, connTimeout, readTimeout);
	}

	/**
	 * post form请求
	 * 
	 * @param url url
	 * @param params 参数
	 */
	public static String postForm(String url, Map<String, String> params)
			throws ConnectTimeoutException, SocketTimeoutException, Exception {
		return postForm(url, params, null, CONN_TIMEOUT, READ_TIMEOUT);
	}

	/**
	 * post form请求
	 * 
	 * @param url url
	 * @param params 参数
	 * @param connTimeout 连接超时时间(毫秒)，可空
	 * @param readTimeout 读取超时时间(毫秒)，可空
	 */
	public static String postForm(String url, Map<String, String> params, Integer connTimeout, Integer readTimeout)
			throws ConnectTimeoutException, SocketTimeoutException, Exception {
		return postForm(url, params, null, connTimeout, readTimeout);
	}

	/**
	 * get请求
	 * 
	 * @param url url
	 */
	public static String get(String url) throws Exception {
		return get(url, CHARSET_DEFAULT, baseHeader(), null, null);
	}
	
	/**
	 * get请求
	 * 
	 * @param url url
	 * @param headers header
	 */
	public static String get(String url, Map<String, String> headers) throws Exception {
		return get(url, CHARSET_DEFAULT, headers, null, null);
	}

	/**
	 * get请求
	 * 
	 * @param url url
	 * @param charset 编码，默认UTF-8
	 * @param headers header
	 */
	public static String get(String url, String charset, Map<String, String> headers) throws Exception {
		return get(url, charset, headers, CONN_TIMEOUT, READ_TIMEOUT);
	}

	/**
	 * 发送一个 Post 请求, 使用指定的字符集编码.
	 * 
	 * @param url
	 * @param body
	 * @param contentType 例如application/xml，application/x-www-form-urlencoded，application/json，text/html
	 * @param charset 编码，默认UTF-8
	 * @param connTimeout 连接超时时间(毫秒)，可空
	 * @param readTimeout 读取超时时间(毫秒)，可空
	 */
	public static String post(String url, String body, String contentType, String charset, Integer connTimeout,
			Integer readTimeout) throws ConnectTimeoutException, SocketTimeoutException, Exception {
		HttpClient client = null;
		HttpPost post = new HttpPost(url);
		String result = "";
		try {
			if (StringUtils.isNotBlank(body)) {
				if (contentType != null && charset != null) {
					post.setEntity(new StringEntity(body, ContentType.create(contentType, charset)));
				} else if (contentType == null && charset != null) {
					post.setEntity(new StringEntity(body, charset));
				} else if (charset == null && contentType != null) {
					post.setEntity(new StringEntity(body, ContentType.create(contentType)));
				} else {
					post.setEntity(new StringEntity(body));
				}
			}
			// 设置参数
			Builder customReqConf = RequestConfig.custom();
			if (connTimeout != null) {
				customReqConf.setConnectTimeout(connTimeout);
			}
			if (readTimeout != null) {
				customReqConf.setSocketTimeout(readTimeout);
			}
			post.setConfig(customReqConf.build());

			HttpResponse res;
			if (url.startsWith("https")) {
				// 执行 Https 请求.
				client = createSSLClientDefault();
				res = client.execute(post);
			} else {
				// 执行 Http 请求.
				client = HttpClientUtils.client;
				res = client.execute(post);
			}
			result = IOUtils.toString(res.getEntity().getContent(), charset);
		} finally {
			post.releaseConnection();
			if (url.startsWith("https") && client != null && client instanceof CloseableHttpClient) {
				((CloseableHttpClient) client).close();
			}
		}
		return result;
	}

	/**
	 * post form请求
	 * 
	 * @param url url
	 * @param params 参数
	 * @param headers header
	 * @param connTimeout 连接超时时间(毫秒)，可空
	 * @param readTimeout 读取超时时间(毫秒)，可空
	 */
	public static String postForm(String url, Map<String, String> params, Map<String, String> headers,
			Integer connTimeout, Integer readTimeout)
			throws ConnectTimeoutException, SocketTimeoutException, Exception {
		HttpClient client = null;
		HttpPost post = new HttpPost(url);
		try {
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> formParams = new ArrayList<org.apache.http.NameValuePair>();
				Set<Entry<String, String>> entrySet = params.entrySet();
				for (Entry<String, String> entry : entrySet) {
					formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
				post.setEntity(entity);
			}

			if (headers != null && !headers.isEmpty()) {
				for (Entry<String, String> entry : headers.entrySet()) {
					post.addHeader(entry.getKey(), entry.getValue());
				}
			}
			// 设置参数
			Builder customReqConf = RequestConfig.custom();
			if (connTimeout != null) {
				customReqConf.setConnectTimeout(connTimeout);
			}
			if (readTimeout != null) {
				customReqConf.setSocketTimeout(readTimeout);
			}
			post.setConfig(customReqConf.build());
			HttpResponse res = null;
			if (url.startsWith("https")) {
				// 执行 Https 请求.
				client = createSSLClientDefault();
				res = client.execute(post);
			} else {
				// 执行 Http 请求.
				client = HttpClientUtils.client;
				res = client.execute(post);
			}
			return IOUtils.toString(res.getEntity().getContent(), Charset.defaultCharset());
		} finally {
			post.releaseConnection();
			if (url.startsWith("https") && client != null && client instanceof CloseableHttpClient) {
				((CloseableHttpClient) client).close();
			}
		}
	}

	/**
	 * get请求
	 * 
	 * @param url url
	 * @param charset 编码，默认UTF-8
	 * @param connTimeout 连接超时时间(毫秒)，可空
	 * @param readTimeout 读取超时时间(毫秒)，可空
	 */
	public static String get(String url, String charset, Map<String, String> headers, Integer connTimeout, Integer readTimeout)
			throws ConnectTimeoutException, SocketTimeoutException, Exception {
		return IOUtils.toString(get(url, headers, connTimeout, readTimeout), charset != null ? charset : CHARSET_DEFAULT);
	}
	
	/**
	 * get请求
	 * 
	 * @param url url
	 * @param headers header
	 * @param connTimeout 连接超时时间(毫秒)，可空
	 * @param readTimeout 读取超时时间(毫秒)，可空
	 */
	public static byte[] get(String url, Map<String, String> headers, Integer connTimeout, Integer readTimeout)
			throws ConnectTimeoutException, SocketTimeoutException, Exception {
		HttpClient client = null;
		HttpGet get = new HttpGet(url);
		try {
			// 设置参数
			Builder customReqConf = RequestConfig.custom();
			if (connTimeout != null) {
				customReqConf.setConnectTimeout(connTimeout);
			}
			if (readTimeout != null) {
				customReqConf.setSocketTimeout(readTimeout);
			}
			get.setConfig(customReqConf.build());
			
			if (headers != null && !headers.isEmpty()) {
				headers.forEach((k, v) -> {
					get.setHeader(k, v);
				});
			}

			HttpResponse res = null;

			if (url.startsWith("https")) {
				// 执行 Https 请求.
				client = createSSLClientDefault();
				res = client.execute(get);
			} else {
				// 执行 Http 请求.
				client = HttpClientUtils.client;
				res = client.execute(get);
			}
			return IOUtils.toByteArray(res.getEntity().getContent());
		} finally {
			get.releaseConnection();
			if (url.startsWith("https") && client != null && client instanceof CloseableHttpClient) {
				((CloseableHttpClient) client).close();
			}
		}
	}

	/**
	 * 创建 SSL连接
	 */
	public static CloseableHttpClient createSSLClientDefault() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return HttpClients.createDefault();
	}

	public static Map<String, String> baseHeader() {
		Map<String, String> map = new HashMap<>();
		map.put("Accept", "*/*");
		map.put("Connection", "keep-alive");
		map.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		map.put("Accept-Language", "zh-CN,zh;q=0.8");
		return map;
	}
	
}