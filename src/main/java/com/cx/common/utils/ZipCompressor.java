package com.cx.common.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;

/**
 * 打包解压工具
 * @author chenxu
 *
 */
public class ZipCompressor {

	public static void DataZip(String filefrom, String fileto, String pathName)
			throws Exception {
		ZipCompressor tozip = new ZipCompressor(fileto);
		tozip.createZipOut();
		tozip.packToolFiles(filefrom, pathName);
		// 关闭流
		tozip.closeZipStream();
	}

	private OutputStream outputStream = null;
	private BufferedOutputStream bufferedOutputStream = null;
	private ZipArchiveOutputStream zipArchiveOutputStream = null;
	private String zipFileName = null;

	public ZipCompressor(String zipname) {
		this.zipFileName = zipname;
	}

	/**
	 * UTF-8编码格式解决中文文件乱码
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void createZipOut() throws FileNotFoundException, IOException {
		File file = new File(zipFileName);
		outputStream = new FileOutputStream(file);
		bufferedOutputStream = new BufferedOutputStream(outputStream);
		zipArchiveOutputStream = new ZipArchiveOutputStream(
				bufferedOutputStream);
		zipArchiveOutputStream.setEncoding("UTF-8");
	}

	/**
	 * 
	 * 把一个目录打包到zip文件中的某目录
	 * 
	 * @param dirpath
	 *            目录绝对地址
	 * 
	 * @param pathName
	 *            zip中目录
	 */

	public void packToolFiles(String dirpath, String pathName)
			throws FileNotFoundException, IOException {
		packToolFiles(zipArchiveOutputStream, dirpath, pathName);

	}

	/**
	 * 
	 * 把一个目录打包到一个指定的zip文件中
	 * 
	 * @param dirpath
	 *            目录绝对地址
	 * @param pathName
	 *            zip文件抽象地址
	 */
	public void packToolFiles(ZipArchiveOutputStream zaos, String dirpath,
			String pathName) throws FileNotFoundException, IOException {
		ByteArrayOutputStream tempbaos = new ByteArrayOutputStream();
		BufferedOutputStream tempbos = new BufferedOutputStream(tempbaos);
		File dir = new File(dirpath);
		// 返回此绝对路径下的文件
		File[] files = dir.listFiles();
		if (files == null || files.length < 1) {
			return;
		}
		for (int i = 0; i < files.length; i++) {
			// 判断此文件是否是一个文件夹
			if (files[i].isDirectory()) {
				packToolFiles(zaos, files[i].getAbsolutePath(), pathName
						+ files[i].getName() + File.separator);
			} else {
				zaos.putArchiveEntry(new ZipArchiveEntry(pathName
						+ files[i].getName()));
				IOUtils.copy(new FileInputStream(files[i].getAbsolutePath()),
						zaos);
				zaos.closeArchiveEntry();
			}
		}
		tempbaos.flush();
		tempbaos.close();
		tempbos.flush();
		tempbos.close();
	}

	/**
	 * 清理缓存，关闭流
	 * 
	 * @throws Exception
	 */
	public void closeZipStream() throws Exception {
		zipArchiveOutputStream.flush();
		zipArchiveOutputStream.close();
		bufferedOutputStream.flush();
		bufferedOutputStream.close();
		outputStream.flush();
		outputStream.close();
	}

	/**
	 * 
	 * 把一个zip文件解压到一个指定的目录中
	 * 
	 * @param zipfilename
	 *            zip文件抽象地址
	 * 
	 * @param outputdir
	 *            目录绝对地址
	 */

	public static void unZipToFolder(String zipfilename, String outputdir)
			throws IOException {
		File zipfile = new File(zipfilename);
		if (zipfile.exists()) {
			outputdir = outputdir + File.separator;
			FileUtils.forceMkdir(new File(outputdir));
			ZipFile zf = new ZipFile(zipfile, "UTF-8");
			Enumeration<ZipArchiveEntry> zipArchiveEntrys = zf.getEntries();
			while (zipArchiveEntrys.hasMoreElements()) {
				ZipArchiveEntry zipArchiveEntry = (ZipArchiveEntry) zipArchiveEntrys
						.nextElement();
				if (zipArchiveEntry.isDirectory()) {
					FileUtils.forceMkdir(new File(outputdir
							+ zipArchiveEntry.getName() + File.separator));

				} else {
					IOUtils.copy(
							zf.getInputStream(zipArchiveEntry),
							FileUtils.openOutputStream(new File(outputdir
									+ zipArchiveEntry.getName())));
				}
			}

		} else {
			throw new IOException("指定的解压文件不存在：\t" + zipfilename);
		}

	}

	/**
	 * 判断文件名是否以.zip为后缀
	 * 
	 * @param fileName
	 *            需要判断的文件名
	 * @return 是zip文件返回true,否则返回false
	 */
	// private static boolean isEndsWithZip(String fileName) {
	// boolean flag = false;
	// if (fileName != null && !"".equals(fileName.trim())) {
	// if (fileName.endsWith(".ZIP") || fileName.endsWith(".zip")) {
	// flag = true;
	// }
	// }
	// return flag;
	// }

	public static void main(String[] args) {
		try {
			// 压缩
			// DataZip("E:/TDDOWNLOAD/10009", "E:/TDDOWNLOAD/10009.zip",
			// "10009/");
			// 解压
			unZipToFolder("E:/TDDOWNLOAD/10009.zip", "E:/TDDOWNLOAD/");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
