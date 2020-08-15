package org.kuark.base.image

import org.junit.jupiter.api.Test
import org.kuark.base.io.PathKit
import java.io.File
import java.net.URI

internal class ImageKitTest {

    @Test
    fun imageToString() {
//        val url = "https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png"
//        val image = ImageKit.readImageFromUri(url)

        val image = File("${PathKit.getProjectRootPath()}/resources/logo.png")
        val imageStr = ImageKit.imageToString(image, "png")
        println(imageStr)
        ImageKit.showImage(ImageKit.stringToImage(imageStr))
        Thread.sleep(3000)
    }

}