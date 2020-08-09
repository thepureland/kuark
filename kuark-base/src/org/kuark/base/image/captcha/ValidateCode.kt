package org.kuark.base.image.captcha

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import javax.imageio.ImageIO

class ValidateCode {
    private var width = 160
    private var height = 40
    private var codeCount = 5
    private var lineCount = 150
    var code: String? = null
        private set
    var buffImg: BufferedImage? = null
        private set
    private val codeSequence = charArrayOf(
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
        'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
        'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    )

    constructor() {
        createCode()
    }

    constructor(width: Int, height: Int) {
        this.width = width
        this.height = height
        createCode()
    }

    constructor(width: Int, height: Int, codeCount: Int, lineCount: Int) {
        this.width = width
        this.height = height
        this.codeCount = codeCount
        this.lineCount = lineCount
        createCode()
    }

    fun createCode(): String? {
        var x = 0
        var fontHeight = 0
        var codeY = 0
        var red = 0
        var green = 0
        var blue = 0
        x = width / (codeCount + 2)
        fontHeight = height - 2
        codeY = height - 4
        buffImg = BufferedImage(width, height, 1)
        val g = buffImg!!.createGraphics()
        val random = Random()
        g.color = Color.WHITE
        g.fillRect(0, 0, width, height)
        val imgFont = ImgFontByte()
        val font = imgFont.getFont(fontHeight)
        g.font = font
        for (i in 0 until lineCount) {
            val xs = random.nextInt(width)
            val ys = random.nextInt(height)
            val xe = xs + random.nextInt(width / 8)
            val ye = ys + random.nextInt(height / 8)
            red = random.nextInt(255)
            green = random.nextInt(255)
            blue = random.nextInt(255)
            g.color = Color(red, green, blue)
            g.drawLine(xs, ys, xe, ye)
        }
        val randomCode = StringBuffer()
        for (i in 0 until codeCount) {
            val strRand = codeSequence[random.nextInt(codeSequence.size)].toString()
            red = random.nextInt(255)
            green = random.nextInt(255)
            blue = random.nextInt(255)
            g.color = Color(red, green, blue)
            g.drawString(strRand, (i + 1) * x, codeY)
            randomCode.append(strRand)
        }
        code = randomCode.toString()
        return code
    }

    @Throws(IOException::class)
    fun write(path: String?) {
        val sos: OutputStream = FileOutputStream(path)
        write(sos)
    }

    @Throws(IOException::class)
    fun write(sos: OutputStream) {
        ImageIO.write(buffImg, "png", sos)
        sos.close()
    }

}