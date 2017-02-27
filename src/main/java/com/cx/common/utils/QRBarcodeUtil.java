package com.cx.common.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 生成二维码
 * 
 */
public class QRBarcodeUtil {
	public static final int BLACK = 0xFF000000;
	public static final int WHITE = 0xFFFFFFFF;
	private static final String DEFAULT_IMAGE_FORMAT = "PNG";
	private static final int FRAME_WIDTH = 2;

	public static byte[] generateQRBarcode(String contents, int size,
			byte[] logodata) throws WriterException, IOException {
		if (size < 100) {
			size = 100;
		}
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");// 设置字符编码
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);// 指定纠错等级
		hints.put(EncodeHintType.MARGIN, 1); // 二维码边界空白大小 ，如：1、2、3、4 默认是4

		MultiFormatWriter barcodeWriter = new MultiFormatWriter();
		BitMatrix matrix = barcodeWriter.encode(contents,
				BarcodeFormat.QR_CODE, size, size, hints);

		int width = matrix.getWidth();
		int height = matrix.getHeight();
		boolean haslogo = false;
		BufferedImage logoimage = null;
		int logowidth = width / 5;
		int logoheight = height / 5;
		if (logodata != null) {
			try {
				logoimage = javax.imageio.ImageIO
						.read(new ByteArrayInputStream(logodata));
				if (logoimage.getWidth() >= logoimage.getHeight()) {
					if (logowidth >= logoimage.getWidth()) {
						logowidth = logoimage.getWidth();
						logoheight = logoimage.getHeight();
					} else {
						// 按宽度压缩
						logoheight = logowidth * logoimage.getHeight()
								/ logoimage.getWidth();
						boolean ispng = false;
						if (logoimage.getType() == BufferedImage.TYPE_4BYTE_ABGR)
							ispng = true;
						byte[] newlogodata = ImageUtils
								.scaleImage(logodata, logowidth, logoheight,
										Image.SCALE_SMOOTH, null);
						logoimage = javax.imageio.ImageIO
								.read(new ByteArrayInputStream(newlogodata));
					}
				} else {
					if (logoheight >= logoimage.getHeight()) {
						logoheight = logoimage.getHeight();
						logowidth = logoimage.getWidth();
					} else {
						// 按高度压缩
						logowidth = logoheight * logoimage.getWidth()
								/ logoimage.getHeight();
						boolean ispng = false;
						if (logoimage.getType() == BufferedImage.TYPE_4BYTE_ABGR)
							ispng = true;
						byte[] newlogodata = ImageUtils
								.scaleImage(logodata, logowidth, logoheight,
										Image.SCALE_SMOOTH, null);
						logoimage = javax.imageio.ImageIO
								.read(new ByteArrayInputStream(newlogodata));
					}
				}
				haslogo = true;
			} catch (Exception e) {
			}
		}

		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		if (!haslogo) {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
				}
			}
		} else {
			int halfwidth = width / 2;
			int halfheight = height / 2;
			int logohalfwidth = logowidth / 2;
			int logohalfheight = logoheight / 2;

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					// 读取logo
					if (x > halfwidth - logohalfwidth
							&& x < halfwidth + logohalfwidth
							&& y > halfheight - logohalfheight
							&& y < halfheight + logohalfheight) {
						image.setRGB(
								x,
								y,
								logoimage.getRGB(x
										- (halfwidth - logohalfwidth), y
										- (halfheight - logohalfheight)));
					} // 在logo四周形成边框
					else if ((x > halfwidth - logohalfwidth - FRAME_WIDTH
							&& x < halfwidth - logohalfwidth + FRAME_WIDTH
							&& y > halfheight - logohalfheight - FRAME_WIDTH && y < halfheight
							+ logohalfheight + FRAME_WIDTH)
							|| (x > halfwidth + logohalfwidth - FRAME_WIDTH
									&& x < halfwidth + logohalfwidth
											+ FRAME_WIDTH
									&& y > halfheight - logohalfheight
											- FRAME_WIDTH && y < halfheight
									+ logohalfheight + FRAME_WIDTH)
							|| (x > halfwidth - logohalfwidth - FRAME_WIDTH
									&& x < halfwidth + logohalfwidth
											+ FRAME_WIDTH
									&& y > halfheight - logohalfheight
											- FRAME_WIDTH && y < halfheight
									- logohalfheight + FRAME_WIDTH)
							|| (x > halfwidth - logohalfwidth - FRAME_WIDTH
									&& x < halfwidth + logohalfwidth
											+ FRAME_WIDTH
									&& y > halfheight + logohalfheight
											- FRAME_WIDTH && y < halfheight
									+ logohalfheight + FRAME_WIDTH)) {
						image.setRGB(x, y, WHITE);
					}// 二维码
					else {
						image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
					}
				}
			}
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(image, DEFAULT_IMAGE_FORMAT, bos);
		return bos.toByteArray();
	}

	public static void main(String[] args) {
		try {
			JsonObject content_json = new JsonObject();
			content_json.addProperty("ty", 1);
			content_json.addProperty("vnum", "nhcxx");
			content_json.addProperty("vname", "cxx");

			FileUtils.writeByteArrayToFile(
					new File("d:/test300.png"),
					generateQRBarcode(content_json.toString(), 300, FileUtils
							.readFileToByteArray(new File("d:/40452561.jpg"))));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriterException e) {
			e.printStackTrace();
		}

	}

}
