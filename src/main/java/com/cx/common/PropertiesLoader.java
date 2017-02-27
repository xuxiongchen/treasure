package com.cx.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置文件的加载器
 * 
 */
public class PropertiesLoader {
	private static final Logger logger = LoggerFactory
			.getLogger(PropertiesLoader.class);

	/**
	 * 加载classPath下subDir目录下的所有配置文件
	 * 
	 * @param subDir
	 * @return
	 */
	public static Properties loadProperties(String dirORfile) {
		Properties props = new Properties();
		try {
			URL resoucePath = PropertiesLoader.class.getClassLoader()
					.getResource(dirORfile);
			File file = new File(resoucePath.getPath());
			// 文件夹
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					File f = files[i];
					InputStream in = null;
					try {
						in = new FileInputStream(f);
						props.load(in);
					} catch (Exception e) {
						logger.error(
								String.format("loadProperties：%sfaild!",
										file.getAbsolutePath()), e);
					} finally {
						try {
							in.close();
						} catch (Exception e1) {
							logger.error(
									String.format("close file:%s faild!",
											file.getAbsolutePath()), e1);
						}
					}
				}
			}
			// 文件
			else if (file.isFile()) {
				InputStream in = null;
				try {
					in = new FileInputStream(file);
					props.load(in);
				} catch (Exception e) {
					logger.error(
							String.format("loadProperties：%sfaild!",
									file.getAbsolutePath()), e);
				} finally {
					try {
						in.close();
					} catch (Exception e1) {
						logger.error(
								String.format("关闭配置文件：%s输入流faild!",
										file.getAbsolutePath()), e1);
					}
				}
			}
		} catch (Exception e) {
			logger.error("PropertiesLoader.loadProperties() faild!dirORfile:"
					+ dirORfile, e);
		}
		return props;
	}
}
