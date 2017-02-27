package com.cx.common.utils;


import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 支持图片缩放，剪裁，旋转等操作
 * 
 */

public class ImageUtils {
	private static final Logger logger = LoggerFactory
			.getLogger(ImageUtils.class);

	/**
	 * @param imageBytes
	 *            原图的字节数组
	 * @param width
	 *            需要缩放的宽度
	 * @param height
	 *            需要缩放的高度
	 * @param algor
	 *            缩放使用的算法，见Image的jdk文档，如，Image.SCALE_SMOOTH缩放的质量比较好，但效率比较低
	 * @param imageFormatName
	 *            图片格式，可以传null
	 * @return
	 */
	public static byte[] scaleImage(byte[] imageBytes, int width, int height,
			int algor, String imageFormatName) {
		if (imageBytes == null)
			return null;

		BufferedImage image = null;// 读取原图
		BufferedImage newImage = null;
		ByteArrayOutputStream out = null;
		try {
			image = ImageIO.read(new ByteArrayInputStream(imageBytes));// 读取原图
			if (StringUtils.isBlank(imageFormatName)) {
				imageFormatName = getImageFormatName(imageBytes);
			}
			// BufferedImage.TYPE_INT_RGB
			// BufferedImage.TYPE_4BYTE_ABGR
			// 绘制缩小后的图
			newImage = new BufferedImage(width, height, image.getType());
			newImage.getGraphics().drawImage(
					image.getScaledInstance(width, height, algor), 0, 0, null);
			out = new ByteArrayOutputStream();
			ImageIO.write(newImage, imageFormatName, out);
		} catch (Exception e) {
			logger.error("scaleImage faild!", e);
			throw new IllegalStateException("scaleImage faild!", e);
		} finally {
			try {
				if (image != null)
					image = null;
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
			}
		}
		return out.toByteArray();
	}

	/**
	 * 将图片以width为准进行等比缩放
	 * 
	 * @param imageBytes
	 * @param width
	 * @param imageFormatName
	 * @return
	 */
	public static byte[] scaleImageByWidth(byte[] imageBytes, int width,
			String imageFormatName) {
		Image img = null;
		int height = 0;
		try {
			img = ImageIO.read(new ByteArrayInputStream(imageBytes));
			int old_w = img.getWidth(null);
			int old_h = img.getHeight(null);
			if (old_h > 0 && old_w > 0) {
				// 以宽度为准，计算高度 等比缩放
				height = (int) ((float) width / old_w * old_h);
			}
		} catch (Exception e) {
			logger.error("scaleImageByWidth faild!", e);
		}
		return ImageUtils.scaleImage(imageBytes, width, height,
				Image.SCALE_SMOOTH, imageFormatName);
	}

	/**
	 * 将图片以height为准进行等比缩放。
	 * 
	 * @param imageBytes
	 * @param height
	 * @param imageFormatName
	 * @return
	 */
	public static byte[] scaleImageByHeight(byte[] imageBytes, int height,
			String imageFormatName) {
		Image img = null;
		int width = 0;
		try {
			img = ImageIO.read(new ByteArrayInputStream(imageBytes));
			int old_w = img.getWidth(null);
			int old_h = img.getHeight(null);
			if (old_h > 0 && old_w > 0) {
				// 以高度为准，计算宽度 等比缩放
				width = (int) ((float) height / old_h * old_w);
			}
		} catch (Exception e) {
			logger.error("scaleImageByHeight faild!", e);
		}
		return ImageUtils.scaleImage(imageBytes, width, height,
				Image.SCALE_SMOOTH, imageFormatName);
	}

	/**
	 * 裁剪图片
	 * 
	 * @param imageBytes
	 * @param left
	 * @param top
	 * @param width
	 * @param height
	 * @param imageFormatName
	 * @return
	 */
	public static byte[] sliceImage(byte[] imageBytes, int left, int top,
			int width, int height, String imageFormatName) {
		if (imageBytes == null)
			return null;

		BufferedImage image = null;// 读取原图
		BufferedImage newImage = null;
		ByteArrayOutputStream out = null;
		try {
			// 区域有效性处理
			image = ImageIO.read(new ByteArrayInputStream(imageBytes));
			if (StringUtils.isBlank(imageFormatName)) {
				imageFormatName = getImageFormatName(imageBytes);
			}
			int old_width = image.getWidth(null);
			int old_height = image.getHeight(null);
			if (top > old_height)
				top = 0;
			if (left > old_width)
				left = 0;
			if (top + height > old_height)
				height = old_height - top;
			if (left + width > old_width)
				width = old_width - left;

			newImage = image.getSubimage(left, top, width, height);
			out = new ByteArrayOutputStream();
			ImageIO.write(newImage, imageFormatName, out);
		} catch (Exception e) {
			logger.error("sliceImage faild!", e);
			throw new IllegalStateException("sliceImage faild!", e);
		} finally {
			try {
				if (image != null)
					image = null;
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
			}
		}
		return out.toByteArray();
	}

	/**
	 * 截取图片
	 * 
	 * @param imgdata
	 *            原图
	 * @param left
	 *            起点左坐标
	 * @param top
	 *            起点上坐标
	 * @param width
	 *            截取的宽度
	 * @param height
	 *            截取的高度
	 * @return
	 */
	// public static byte[] sliceImage(byte[] imgdata, int left, int top,
	// int width, int height) {
	// if (imgdata == null)
	// return null;
	//
	// Image image = null;// 读取原图
	// ByteArrayOutputStream out = null;
	// BufferedImage newImage = null;
	// ImageReader reader = null;
	// try {
	// // 区域有效性处理
	// ByteArrayInputStream bin = new ByteArrayInputStream(imgdata);
	// image = ImageIO.read(bin);
	// int old_width = image.getWidth(null);
	// int old_height = image.getHeight(null);
	// if (top > old_height)
	// top = 0;
	// if (left > old_width)
	// left = 0;
	// if (top + height > old_height)
	// height = old_height - top;
	// if (left + width > old_width)
	// width = old_width - left;
	//
	// ImageInputStream temp = ImageIO
	// .createImageInputStream(new ByteArrayInputStream(imgdata));//
	// 创建图片流读取器对象列表，用于读取某一类图片
	// reader = ImageIO.getImageReaders(temp).next();//
	// 因为只处理一张图，所以只获取第一个ImageReader
	// reader.setInput(temp, true);// 设置图片数据流
	// ImageReadParam param = reader.getDefaultReadParam();//
	// Rectangle rect = new Rectangle(left, top, width, height);
	// param.setSourceRegion(rect);// 设置reader参数
	//
	// newImage = reader.read(0, param);// 读取部分图片数据
	// out = new ByteArrayOutputStream();
	// ImageIO.write(newImage, "jpeg", out);
	// } catch (Exception e) {
	// // log.error("图片截取！",e);
	// throw new IllegalStateException("图片截取！", e);
	// } finally {
	// try {
	// if (image != null)
	// image = null;
	// if (out != null) {
	// out.flush();
	// out.close();
	// }
	// } catch (Exception e) {
	// }
	// }
	// return out.toByteArray();
	// }

	/**
	 * 旋转图片为指定角度
	 * 
	 * @param bufferedimage
	 *            目标图像
	 * @param degree
	 *            旋转角度 只支持90,180，270三种角度
	 * @return
	 */
	public static byte[] rotateImage(byte[] imageBytes, final int degree,
			String imageFormatName) {
		BufferedImage image = null;// 读取原图
		BufferedImage newImage = null;
		ByteArrayOutputStream out = null;

		try {
			image = ImageIO.read(new ByteArrayInputStream(imageBytes));
			if (StringUtils.isBlank(imageFormatName)) {
				imageFormatName = getImageFormatName(imageBytes);
			}

			int w = image.getWidth();
			int h = image.getHeight();
			// int type = bufferedimage.getColorModel().getTransparency();//
			// 图片模式
			// BufferedImage img=new BufferedImage(h, w, type);
			// Graphics2D graphics2d=null;
			// // (graphics2d = (img = new BufferedImage(w, h, type))
			// // .createGraphics()).setRenderingHint(
			// // RenderingHints.KEY_INTERPOLATION,
			// // RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			// (graphics2d = (bufferedimage.createGraphics())).setRenderingHint(
			// RenderingHints.KEY_INTERPOLATION,
			// RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			//
			// graphics2d.rotate(Math.toRadians(degree), w/2,
			// h/2);//将坐标原点移至中心，然后旋转指定角度，最后恢复原点
			// graphics2d.drawImage(img, 0, 0, null);//将旋转后的图片填充到bufferimage
			// graphics2d.dispose();

			AffineTransform affineTransform = new AffineTransform();
			if (degree == 180) {
				affineTransform.translate(w, h);
				newImage = new BufferedImage(w, h, image.getType());
			} else if (degree == 90) {
				affineTransform.translate(h, 0);
				newImage = new BufferedImage(h, w, image.getType());
			} else if (degree == 270) {
				affineTransform.translate(0, w);
				newImage = new BufferedImage(h, w, image.getType());
			}
			RenderingHints hints = new RenderingHints(
					RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			affineTransform.rotate(java.lang.Math.toRadians(degree));
			AffineTransformOp affineTransformOp = new AffineTransformOp(
					affineTransform, hints);
			affineTransformOp.filter(image, newImage);

			out = new ByteArrayOutputStream();
			ImageIO.write(newImage, imageFormatName, out);
		} catch (Exception e) {
			logger.error("rotateImage faild!", e);
			throw new IllegalStateException("rotateImage faild!", e);
		} finally {
			try {
				if (image != null)
					image = null;
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
			}
		}
		return out.toByteArray();
	}

	private static String getImageFormatName(final byte[] imageBytes)
			throws IOException {
		ByteArrayInputStream input = new ByteArrayInputStream(imageBytes);
		ImageInputStream imageInput = ImageIO.createImageInputStream(input);
		Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInput);
		String imageFormatName = null;
		if (iterator.hasNext()) {
			ImageReader reader = iterator.next();
			imageFormatName = reader.getFormatName().toUpperCase();
		}

		try {
			return imageFormatName;
		} finally {
			if (imageInput != null) {
				imageInput.close();
			}
		}
	}

	// /**
	// * 图片叠加
	// * @param imgobject 需要合并的图片数组，从底图往上依次排列
	// * @param width 合并后的图片宽度
	// * @param height 合并后的图片高度
	// * @return
	// */
	// public static byte[] mergeImg(Images imgobject[],int width,int height){
	// try {
	// BufferedImage tag = new BufferedImage(width, height,
	// BufferedImage.TYPE_4BYTE_ABGR_PRE);
	// ByteArrayOutputStream bout=new ByteArrayOutputStream();
	// Graphics2D g=null;
	// for (int z = 0; z < imgobject.length; z++) {
	// // g2d[z] = tag.createGraphics();
	// g = tag.createGraphics();
	// // System.out.println("创建第" + z + "个画笔！");
	// g.drawImage(imgobject[z].getBimg(), imgobject[z].getX(),
	// imgobject[z].getY(), imgobject[z].getBimg().getWidth(),
	// imgobject[z].getBimg().getHeight(), null);
	// }
	//
	// // System.out.println("合成完毕，准备输出，请稍候~~~~~~");
	// ImageIO.write(tag, "png", bout);
	// return bout.toByteArray();
	// } catch (Exception e) {
	// logger.error("", e);
	// }
	// return null;
	// }
	// /**
	// * 图片叠加
	// * @param imgobject 需要合并的图片数组，从底图往上依次排列
	// * @param width 合并后的图片宽度
	// * @param height 合并后的图片高度
	// * @param outfile 合并后的图片存储路径
	// * @return
	// */
	// public static void mergeImg(Images imgobject[],int width,int
	// height,String outfile){
	// byte[] imgs=mergeImg(imgobject, width, height);
	// if(imgs!=null&&!CommonUtil.isNull(outfile))
	// {
	// try {
	//
	// FileUtils.writeByteArrayToFile(new File(outfile), imgs);
	// } catch (Exception e) {
	// logger.error("", e);
	// }
	// }
	// }
	// public static void main(String[] args) {
	// try {
	// // byte[]dd= FileUtils.readBytes("/home/yuxiaowei/test/unnamed.png");
	// //
	// // byte[] data=scaleImg(dd, 70, 70, true);
	// //
	// // FileUtils.write("/home/yuxiaowei/test/unnamed_1.png", data);
	//
	// Long star = System.currentTimeMillis();
	// String src[] = new String[3];//图片的本地路径
	// src[0] = "/home/yuxiaowei/test/1.png";// 底边
	// src[1] = "/home/yuxiaowei/test/unnamed_1.png";// 正身
	// src[2] = "/home/yuxiaowei/test/download.png";// 图标
	// int x[] = new int[3];// 存放X轴坐标的数组
	// x[0] = 0;
	// x[1] = 7;
	// x[2] = 0;
	// int y[] = new int[3];// 存放Y轴坐标的数组
	// y[0] = 0;
	// y[1] = 7;
	// y[2] = 0;
	// Images[] imgo = new Images[src.length];// 批量生成图片对象
	// for (int z = 0; z < imgo.length; z++) { // 构造方法测参数是X,Y轴的位置和数据
	// imgo[z] = new Images(x[z], y[z], FileUtils.readFileToByteArray(new
	// File(src[z])));
	// }
	// mergeImg(imgo,84,84, "/home/yuxiaowei/test/out.png");// 合成图片的方法
	// Long end = System.currentTimeMillis();
	// System.out.println("图片合成耗时" + (end - star) + "毫秒");
	//
	//
	//
	//
	// // String sourceFileName="/home/yuxiaowei/download/unnamed.png";
	// // String destFile2="/home/yuxiaowei/download/unnamed_1.jpg";
	//
	// // byte[] dd=FileUtils.readBytes(sourceFileName);
	// // byte[] logodata=FileUtils.readBytes(destFile2);
	// // long start=System.currentTimeMillis();
	// // resize(sourceFileName, destFile, 480,800);
	// // byte[] data=resize(dd, 320,240,95,"jpeg");
	// // byte[] data=scaleImg(dd, 320,240);
	// // byte[] data=cutImg(dd, 96, 96, 150, 150,"png");
	// // Point pos=new Point();
	// // pos.setLocation(50,50);
	// //// byte[] data=createWaterPrintByText(dd, "哈哈test", pos, 20,
	// "png");//添加水印
	// // byte[] data=createWaterPrintByImg(dd, logodata, pos);
	// // FileUtils.write(destFile1, data);
	//
	// // byte[] data=resize(dd, 140,180,80,"jpeg");
	// // byte[] data=scaleImg(dd, 84, 84, false);
	// // byte[] data=scaleImg(dd, 120,180);
	// // FileUtils.write(destFile2, data);
	//
	//
	//
	// // data=resize(dd, 1024,500,95,"jpeg");
	// // data=scaleImg(dd, 1024,500);
	// // FileUtils.write(destFile3, data);
	// // byte[] dd=FileUtils.readBytes(sourceFileName);
	// // byte[] dd1=scaleImage(dd, 480,800,Image.SCALE_SMOOTH,true);
	// // FileUtils.write(destFile, dd1);
	//
	// // byte[]
	// d=HttpUtils.getBytes("http://gamecache.3g.cn/game/uploads/cutpic/201205/246416/fdc75891_s480.jpg");
	// // System.out.println(d.length);
	// // d=rotateImage("jpg", d, 90);
	// // FileUtils.write("d:/download/testrotate.jpg", d);
	// // long end=System.currentTimeMillis();
	// // System.out.println("times="+(end-start));
	// // HttpUtils.shutdown();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }

}

