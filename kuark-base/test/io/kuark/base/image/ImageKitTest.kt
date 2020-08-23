package io.kuark.base.image

import io.kuark.base.io.PathKit
import org.junit.jupiter.api.Test
import java.io.File

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