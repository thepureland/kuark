package org.kuark.base.image

import org.junit.jupiter.api.Test
import org.kuark.base.io.PathKit
import java.io.File

internal class ImageKitTest {

    @Test
    fun imageToString() {
        val file = File("${PathKit.getProjectRootPath()}/resources/logo.png")
        val imageStr = ImageKit.imageToString(file.absolutePath, "png")
        println(imageStr)
        val image = ImageKit.stringToImage(imageStr)
        ImageKit.showImage(image)
        Thread.sleep(2000)
    }

}