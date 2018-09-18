package cn.veasion.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.filechooser.FileSystemView;

/**
 * 文本文件工具类
 * 
 * @author zhuowei.luo
 */
public class TxtFileUtil {

	public static final String HOME_PATH = FileSystemView.getFileSystemView().getHomeDirectory().getPath();

	/**
	 * 读取文本文件，以<code>\r\n</code>换行
	 */
	public static String readTxtFile(File file) {
		StringBuilder sb = new StringBuilder();
		readTxtFile(file, (line, lineNumber) -> {
			sb.append(line).append("\r\n");
		});
		return sb.toString();
	}

	/**
	 * 读取文本文件
	 */
	public static void readTxtFile(File file, LineHandle lineHandle) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int lineNumber = 0;
			while ((tempString = reader.readLine()) != null) {
				lineHandle.handle(tempString, ++lineNumber);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * 写文本文件
	 */
	public static boolean writeTxtFile(File file, String content, boolean append) {
		FileWriter writer = null;
		boolean isSuccess = false;
		try {
			writer = new FileWriter(file, append);
			writer.write(content);
			writer.close();
			isSuccess = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return isSuccess;
	}

	@FunctionalInterface
	public interface LineHandle {

		/**
		 * 文本行处理
		 * 
		 * @param line
		 *            当前行数据
		 * @param lineNumber
		 *            当前行数
		 */
		void handle(String line, int lineNumber);

	}
}
