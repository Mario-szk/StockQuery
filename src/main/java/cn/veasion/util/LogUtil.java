package cn.veasion.util;

public class LogUtil {

	public static void info(String msg) {
		System.out.println(msg);
	}

	public static void error(String msg) {
		error(msg, null);
	}

	public static void error(String msg, Exception e) {
		System.err.println(msg);
		if (e != null) {
			// e.printStackTrace();
		}
	}
	
}
