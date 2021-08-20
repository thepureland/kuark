package io.kuark.base.image

import io.kuark.base.io.PathKit
import org.apache.batik.anim.dom.SAXSVGDocumentFactory
import org.apache.batik.bridge.BridgeContext
import org.apache.batik.bridge.GVTBuilder
import org.apache.batik.bridge.UserAgentAdapter
import org.apache.batik.gvt.renderer.ConcreteImageRendererFactory
import org.apache.batik.gvt.renderer.ImageRendererFactory
import org.w3c.dom.svg.SVGDocument
import org.w3c.dom.svg.SVGElement
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Rectangle
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import java.io.*
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.imageio.ImageIO
import javax.imageio.stream.MemoryCacheImageInputStream
import javax.swing.JFrame
import javax.swing.JPanel


/**
 * 图片处理工具类
 *
 * @author K
 * @since 1.0.0
 */
object ImageKit {

    /**
     * 从文件读取图片
     *
     * @param imageFile 图片文件
     * @return BufferedImage对象
     * @author K
     * @since 1.0.0
     */
    fun readImageFromFile(imageFile: File): BufferedImage = readImageFromFile(imageFile.path)

    /**
     * 从文件读取图片
     *
     * @param imagePath 图片路径
     * @return BufferedImage对象
     * @author K
     * @since 1.0.0
     */
    fun readImageFromFile(imagePath: String): BufferedImage = ImageIO.read(Files.newInputStream(Paths.get(imagePath)))

    /**
     * 从URI读取文件
     *
     * @param imageUri 图片uri
     * @return BufferedImage对象
     * @author K
     * @since 1.0.0
     */
    fun readImageFromUri(imageUri: URI): BufferedImage = ImageIO.read(imageUri.toURL().openStream())

    /**
     * 从URI读取文件
     *
     * @param imageUriStr 图片网络地址
     * @return BufferedImage对象
     * @author K
     * @since 1.0.0
     */
    fun readImageFromUri(imageUriStr: String): BufferedImage = readImageFromUri(URI.create(imageUriStr))

    /**
     * 写图片到文件
     *
     * @param bufferedImage BufferedImage对象
     * @param imageFormat 图片类型(如png,jpg,gif等)
     * @param imagePath 图片目标文件路径
     * @author K
     * @since 1.0.0
     */
    fun writeImage(bufferedImage: BufferedImage, imageFormat: String, imagePath: String) =
        ImageIO.write(bufferedImage, imageFormat, Files.newOutputStream(Paths.get(imagePath)))

    /**
     * 将图片转换为字符串表示
     *
     * @param imageFile 图片文件
     * @param imageFormat 图片类型(如png,jpg,gif等)
     * @return 图片的字符串表示
     * @author K
     * @since 1.0.0
     */
    fun imageToString(imageFile: File, imageFormat: String): String {
        val bufferedImage = readImageFromFile(imageFile)
        return imageToString(bufferedImage, imageFormat)
    }

    /**
     * 将图片转换为字符串表示
     *
     * @param imageUri 图片uri
     * @param imageFormat 图片类型(如png,jpg,gif等)
     * @return 图片的字符串表示
     * @author K
     * @since 1.0.0
     */
    fun imageToString(imageUri: URI, imageFormat: String): String {
        val bufferedImage = readImageFromUri(imageUri)
        return imageToString(bufferedImage, imageFormat)
    }

    /**
     * 将图片转换为字符串表示
     *
     * @param bufferedImage BufferedImage对象
     * @param imageFormat 图片类型(如png,jpg,gif等)
     * @return 图片的字符串表示(Base64编码)
     * @author K
     * @since 1.0.0
     */
    fun imageToString(bufferedImage: BufferedImage, imageFormat: String): String {
        return ByteArrayOutputStream().use {
            ImageIO.write(bufferedImage, imageFormat, it)
            val byteArray = it.toByteArray()
            String(Base64.getEncoder().encode(byteArray))
        }
    }

    /**
     * 将字符串表示转换为图片对象
     *
     * @param imgStr 图片的字符串表示(Base64编码)
     * @return BufferedImage对象
     * @author K
     * @since 1.0.0
     */
    fun stringToImage(imgStr: String): BufferedImage {
        val decoder = Base64.getDecoder()
        val imageByte = decoder.decode(imgStr)
        return ByteArrayInputStream(imageByte).use {
            ImageIO.read(it)
        }
    }


    /**
     * 把传入的原始图像按高度和宽度进行缩放，生成符合要求的图标
     *
     * @param srcImageFile 源文件地址
     * @param height       目标高度
     * @param width        目标宽度
     * @param hasFiller    比例不对时是否需要补白：true为补白（缺省值）; false为不补白;
     * @author K
     * @since 1.0.0
     */
    fun scale(srcImageFile: String, height: Int, width: Int, hasFiller: Boolean = true): BufferedImage {
        var ratio = 0.0 // 缩放比例
        val file = File(srcImageFile)
        val srcImage: BufferedImage = ImageIO.read(file)
        var destImage = srcImage.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH)
        // 计算比例
        if (srcImage.height > height || srcImage.width > width) {
            ratio = if (srcImage.height > srcImage.width) {
                height.toDouble() / srcImage.height
            } else {
                width.toDouble() / srcImage.width
            }
            val op = AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null)
            destImage = op.filter(srcImage, null)
        }
        if (hasFiller) { // 补白
            val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
            val graphic = image.createGraphics()
            graphic.color = Color.white
            graphic.fillRect(0, 0, width, height)
            if (width == destImage.getWidth(null)) graphic.drawImage(
                destImage, 0, (height - destImage.getHeight(null)) / 2, destImage.getWidth(null),
                destImage.getHeight(null), Color.white, null
            ) else graphic.drawImage(
                destImage, (width - destImage.getWidth(null)) / 2, 0, destImage.getWidth(null),
                destImage.getHeight(null), Color.white, null
            )
            graphic.dispose()
            destImage = image
        }
        return destImage as BufferedImage
    }

    /**
     * 从内存字节数组中读取图像
     *
     * @param imgBytes 未解码的图像数据
     * @return 返回 [BufferedImage]
     * @throws IOException 当读写错误或不识别的格式时抛出
     * @author https://blog.csdn.net/johnwaychan/article/details/79106983
     * @author K
     * @since 1.0.0
     */
    fun readMemoryImage(imgBytes: ByteArray, format: String? = null): BufferedImage {
        // 将字节数组转为InputStream，再转为MemoryCacheImageInputStream
        val imageInputstream = MemoryCacheImageInputStream(ByteArrayInputStream(imgBytes))
        // 获取所有能识别数据流格式的ImageReader对象
        val it = if (format == null) {
            var imageReaders = ImageIO.getImageReaders(imageInputstream)
//            if (!imageReaders.hasNext()) {
//                imageReaders = ImageIO.getImageReaders(FileImageInputStream(File(srcFilePath)))
//            }
            imageReaders
        } else {
            ImageIO.getImageReadersByFormatName(format)
        }
        // 迭代器遍历尝试用ImageReader对象进行解码
        while (it.hasNext()) {
            val imageReader = it.next()
            // 设置解码器的输入流
            imageReader.setInput(imageInputstream, true, true)
            // 图像文件格式后缀
            val suffix: String = imageReader.formatName.trim().lowercase(Locale.getDefault())
            // 图像宽度
            val width: Int = imageReader.getWidth(0)
            // 图像高度
            val height: Int = imageReader.getHeight(0)
            System.out.printf("format %s,%dx%d\n", suffix, width, height)
            try {
                // 解码成功返回BufferedImage对象
                // 0即为对第0张图像解码(gif格式会有多张图像),前面获取宽度高度的方法中的参数0也是同样的意思
                return imageReader.read(0, imageReader.defaultReadParam)
            } catch (e: Exception) {
                imageReader.dispose()
                // 如果解码失败尝试用下一个ImageReader解码
            }
        }
        imageInputstream.close()
        throw IOException("unsupported image format")
    }

    /**
     * 图形化展现图片对象
     *
     * @param bufferedImage BufferedImage对象
     * @author K
     * @since 1.0.0
     */
    fun showImage(bufferedImage: BufferedImage) {
        class ImagePanel(var image: BufferedImage) : JPanel() {

            override fun paintComponent(g: Graphics) {
                val x: Int = (width - image.width) / 2
                val y: Int = (height - image.height) / 2
                g.drawImage(image, x, y, this)
            }

            override fun getPreferredSize(): Dimension {
                return Dimension(image.width, image.height)
            }
        }

        val panel = ImagePanel(bufferedImage)
        val f = JFrame()
        f.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        f.add(panel)
        f.setLocation(200, 200)
        f.isVisible = true
        f.pack()
    }

    /**
     * 将svg格式的xml渲染成图片
     *
     * @param xmlContent svg格式的xml
     * @param width 图片宽度
     * @param height 图片高度
     * @return BufferedImage
     * @author https://blog.csdn.net/do168/article/details/51564492
     * @author K
     * @since 1.0.0
     */
    fun renderSvgToImage(xmlContent: String, width: Int, height: Int): BufferedImage {
        return renderSvgToImage(xmlContent, width, height, false, null, null)
    }

    private fun renderSvgToImage(
        xmlContent: String,
        width: Int,
        height: Int,
        stretch: Boolean,
        idRegex: String?,
        replacementColor: Color?,
    ): BufferedImage {
        // the following is necessary so that batik knows how to resolve URI fragments
        // (#myLinearGradient). Otherwise the resolution fails and you cannot render.
        val uri = "${PathKit.getTempDirectoryPath()}/temp.svg"
        val df = SAXSVGDocumentFactory("org.apache.xerces.parsers.SAXParser")
        val document = df.createSVGDocument(uri, StringReader(xmlContent))
        if (idRegex != null && replacementColor != null) {
            replaceFill(document, idRegex, replacementColor)
        }
        return renderToImage(document, width, height, stretch)
    }

    private fun renderToImage(document: SVGDocument?, width: Int, height: Int, stretch: Boolean): BufferedImage {
        val rendererFactory = ConcreteImageRendererFactory()
        val renderer = rendererFactory.createStaticImageRenderer()
        val builder = GVTBuilder()
        val ctx = BridgeContext(UserAgentAdapter())
        ctx.setDynamicState(BridgeContext.STATIC)
        val rootNode = builder.build(ctx, document)
        renderer.tree = rootNode
        val docWidth = ctx.documentSize.width.toFloat()
        val docHeight = ctx.documentSize.height.toFloat()
        var xscale = width / docWidth
        var yscale = height / docHeight
        if (!stretch) {
            val scale = xscale.coerceAtMost(yscale)
            xscale = scale
            yscale = scale
        }
        val px = AffineTransform.getScaleInstance(xscale.toDouble(), yscale.toDouble())
        val tx = (-0 + (width / xscale - docWidth) / 2).toDouble()
        val ty = (-0 + (height / yscale - docHeight) / 2).toDouble()
        px.translate(tx, ty)
        //cgn.setViewingTransform(px);
        renderer.updateOffScreen(width, height)
        renderer.tree = rootNode
        renderer.transform = px
        //renderer.clearOffScreen();
        renderer.repaint(Rectangle(0, 0, width, height))
        return renderer.offScreen
    }

    private fun replaceFill(document: SVGDocument, idRegex: String, color: Color) {
        val colorCode = String.format("#%02x%02x%02x", color.red, color.green, color.blue)
        val children = document.getElementsByTagName("*")
        for (i in 0 until children.length) {
            if (children.item(i) is SVGElement) {
                val element = children.item(i) as SVGElement
                if (element.id.matches(Regex(idRegex))) {
                    var style = element.getAttributeNS(null, "style")
                    style = style.replaceFirst("fill:#[a-zA-z0-9]+".toRegex(), "fill:$colorCode")
                    element.setAttributeNS(null, "style", style)
                }
            }
        }
    }


}