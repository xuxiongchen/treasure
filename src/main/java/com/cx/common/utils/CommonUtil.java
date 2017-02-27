package com.cx.common.utils;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
/**
 * 转为Byte数组、压缩文档、分解字符串、获取IP等公共
 * @author chenxu
 *
 */
public class CommonUtil {

	public static byte[] toByteArray(InputStream in, boolean close)
			throws Exception {
		try {
			return IOUtils.toByteArray(in);
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (close)
					in.close();
			} catch (IOException e) {
			}
		}
	}

	public static byte[] gzip(byte[] bs) throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream(1024 * 4);
		GZIPOutputStream gzout = null;
		try {
			gzout = new GZIPOutputStream(bout);
			gzout.write(bs);
			gzout.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			gzout.close();
			bout.close();
		}
		return bout.toByteArray();

	}

	public static byte[] ungzip(byte[] bs) throws Exception {
		GZIPInputStream gzin = null;
		ByteArrayInputStream bin = null;
		try {
			bin = new ByteArrayInputStream(bs);
			gzin = new GZIPInputStream(bin);
			return IOUtils.toByteArray(gzin);
		} catch (Exception e) {
			throw e;
		} finally {
			gzin.close();
			bin.close();
		}
	}

	public static byte[] inflate(byte[] bs, boolean nowrap) throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream(1024 * 4);
		try {
			Inflater decompressor = new Inflater(nowrap);
			decompressor.setInput(bs);

			byte[] buf = new byte[1024];
			while (!decompressor.finished()) {
				int count = decompressor.inflate(buf);
				bout.write(buf, 0, count);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			bout.close();
		}
		return bout.toByteArray();
	}

	public static byte[] deflate(byte[] bs, boolean nowrap) throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream(1024 * 4);
		try {
			Deflater df = new Deflater(5, nowrap);
			// df.setLevel(Deflater.BEST_COMPRESSION);
			df.setInput(bs);
			df.finish();
			byte[] buff = new byte[1024];
			while (!df.finished()) {
				int count = df.deflate(buff);
				bout.write(buff, 0, count);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			bout.close();
		}
		return bout.toByteArray();
	}

	public static int[] split(String value, String separator) {
		if (StringUtils.isBlank(value)) {
			return new int[0];
		}
		String[] tmps = value.split(separator);
		int[] intvalues = new int[0];
		if (tmps != null & tmps.length > 0) {
			intvalues = new int[tmps.length];
			for (int i = 0; i < tmps.length; i++) {
				intvalues[i] = Integer.parseInt(tmps[i]);
			}
		}
		return intvalues;
	}

	/**
	 * 拼接多个字段信息
	 * 
	 * @param separator
	 * @param values
	 * @return
	 */
	public static String jointValue(String separator, String... values) {
		StringBuffer resultValue = new StringBuffer();
		for (int i = 0; i < values.length; i++) {
			if (values[i] != null) {
				resultValue.append(values[i]);
			}
			if (i < values.length - 1)
				resultValue.append(separator);
		}
		return resultValue.toString();
	}

	/**
	 * 获取本机的ip地址
	 */
	public static String getLocalIP() {
		String ip = "";
		try {
			// 获取网卡
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface
					.getNetworkInterfaces();
			// 遍历网卡
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
						.nextElement();
				// 网卡名
				// System.out.println(netInterface.getName());
				// 获取该网卡绑定的InetAddresses
				Enumeration<InetAddress> addresses = netInterface
						.getInetAddresses();
				// 遍历该网卡绑定的InetAddresses
				while (addresses.hasMoreElements()) {
					// 选择不为127.0.0.1的ip地址作为该机器的ip地址
					InetAddress addres = (InetAddress) addresses.nextElement();
					if (addres != null && addres instanceof Inet4Address) {
						// if (!"127.0.0.1".equals(addres.getHostAddress())
						// && (addres.getHostAddress().startsWith("10.") ||
						// addres
						// .getHostAddress().startsWith("192."))) {
						// ip = addres.getHostAddress();
						// break;
						// }
						// start add by yxw 2013-07-08
						// 修复获取内网地址的bug，国外的ip：10.18.18.
						if (!addres.isLoopbackAddress()
								&& addres.isSiteLocalAddress()) {
							ip = addres.getHostAddress();
							break;
						}
						// end add by yxw 2013-07-08
					}
				}
				if (ip != null && ip.length() > 5) {
					break;
				}
			}
		} catch (Exception e) {
		}
		return ip;
	}

	/**
	 * 获取异常的堆栈信息
	 */
	public static String getExceptionStackStr(Throwable e) {
		if (e != null) {
			StringBuilder logStrBuilder = new StringBuilder();
			logStrBuilder.append(" message:");
			logStrBuilder.append(e.getMessage());
			logStrBuilder.append(" stack:");
			// 堆栈信息
			StackTraceElement[] traces = e.getStackTrace();
			// if (traces != null && traces.length > 0) {
			// for (int i = 0; i < traces.length && i < 2; i++) {
			// logStrBuilder.append(traces[i].toString());
			// if (i < traces.length - 1 && i < 1) {
			// logStrBuilder.append(" ");
			// }
			// }
			// }
			for (int i = 0; null != traces && i < traces.length; i++) {
				logStrBuilder.append(traces[i].toString()).append(" ");
			}
			return logStrBuilder.toString();
		} else {
			return "";
		}
	}
}
