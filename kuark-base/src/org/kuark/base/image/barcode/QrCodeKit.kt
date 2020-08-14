package org.kuark.base.image.barcode

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import org.kuark.base.image.ImageKit
import org.kuark.base.io.FilenameKit
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


/**
 * 二维码工具类
 *
 * @author https://www.cnblogs.com/demon7715/p/10984160.html
 * @author K
 * @since 1.0.0
 */
object QrCodeKit {

    // 二维码写码器
    private val mutiWriter = MultiFormatWriter()

    /**
     * 生成二维码(正方形)，并保存到指定文件
     *
     * @param content 二维码所表示的内容，一般为URL
     * @param destImagePath 目标文件路径
     * @param logoImagePath logo图片路径
     * @param sideLength 二维码图片(正文形)边长，缺省为300像素
     * @param logoSideLength logo图片(正方形)的边长，缺省为二维码图片边长的1/5
     * @param margin 页边空白宽度(像素)，缺省为2像素
     */
    fun genQrCode(
        content: String, destImagePath: String, logoImagePath: String, sideLength: Int = 300,
        logoSideLength: Int = sideLength / 5, margin: Int = 2
    ) {
        val formatName = FilenameKit.getExtension(destImagePath)
        ImageIO.write(
            genQrCode(content, logoImagePath, sideLength, logoSideLength, margin),
            formatName, File(destImagePath)
        )
    }


    /**
     * 生成二维码(正方形)
     *
     * @param content 二维码所表示的内容，一般为URL
     * @param logoImagePath logo图片路径
     * @param sideLength 二维码图片(正文形)边长，缺省为300像素
     * @param logoSideLength logo图片(正方形)的边长，缺省为二维码图片边长的1/5
     * @param margin 页边空白宽度(像素)，缺省为2像素
     * @return 二维码BufferedImage对象
     */
    fun genQrCode(
        content: String, logoImagePath: String,
        sideLength: Int = 300, logoSideLength: Int = sideLength / 5, margin: Int = 2
    ): BufferedImage {
        val logoImage = ImageKit.scale(logoImagePath, logoSideLength, logoSideLength, true)
        return genQrCode(content, logoImage, sideLength)
    }

    /**
     * 生成二维码(正方形)
     *
     * @param content 二维码所表示的内容，一般为URL
     * @param logoImage logo图片对象
     * @param sideLength 二维码图片(正文形)边长，缺省为300像素
     * @param margin 页边空白宽度(像素)，缺省为2像素
     * @return 二维码BufferedImage对象
     */
    fun genQrCode(content: String, logoImage: BufferedImage, sideLength: Int = 300, margin: Int = 2): BufferedImage {
        // 读取源图像
        val srcPixels = Array(sideLength) { IntArray(sideLength) }
        for (i in 0 until logoImage.width) {
            for (j in 0 until logoImage.height) {
                srcPixels[i][j] = logoImage.getRGB(i, j)
            }
        }

        //设置二维码的纠错级别 编码
        val hint =
            mapOf(EncodeHintType.CHARACTER_SET to "utf-8", EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H)
        // 生成二维码
        val matrix = mutiWriter.encode(content, BarcodeFormat.QR_CODE, sideLength, sideLength, hint)

        // 二维矩阵转为一维像素数组
        val halfW = matrix.width / 2
        val halfH = matrix.height / 2
        val imageHalfWidth = sideLength / 2
        val value1 = halfW - imageHalfWidth - margin
        val value2 = halfW - imageHalfWidth + margin
        val value3 = halfW + imageHalfWidth + margin
        val value4 = halfW + imageHalfWidth - margin
        val value5 = halfH - imageHalfWidth - margin
        val value6 = halfH - imageHalfWidth + margin
        val value7 = halfH + imageHalfWidth + margin
        val value8 = halfH + imageHalfWidth - margin
        val pixels = IntArray(sideLength * sideLength)
        for (y in 0 until matrix.height) {
            for (x in 0 until matrix.width) {
                // 读取图片
                if (x > halfW - imageHalfWidth && x < halfW + imageHalfWidth && y > halfH - imageHalfWidth && y < halfH + imageHalfWidth) {
                    pixels[y * sideLength + x] = srcPixels[x - halfW + imageHalfWidth][y - halfH + imageHalfWidth]
                } else if (x in (value1 + 1) until value2 && y > value5 && y < value7
                    || x in (value4 + 1) until value3 && y > value5 && y < value7
                    || x in (value1 + 1) until value3 && y > value5 && y < value6
                    || x in (value1 + 1) until value3 && y > value8 && y < value7
                ) {
                    pixels[y * sideLength + x] = 0xfffffff
                } else {
                    // 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；
                    pixels[y * sideLength + x] = if (matrix[x, y]) -0x1000000 else 0xfffffff
                }
            }
        }
        val image = BufferedImage(sideLength, sideLength, BufferedImage.TYPE_INT_RGB)
        image.raster.setDataElements(0, 0, sideLength, sideLength, pixels)
        return image
    }

}

fun main() {
    //将1.png作为logo放在2.png中间   生成扫码图片2.png变成二维码
    QrCodeKit.genQrCode("http://www.baidu.com", "D:/1.png", "D:/2.png")
    println("成功！！！")
}