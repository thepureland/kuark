package org.kuark.base.image.barcode

import org.junit.jupiter.api.Test
import org.kuark.base.image.ImageKit
import org.kuark.base.io.PathKit

internal class QrCodeKitTest {

    @Test
    fun genQrCode() {
        val logoImagePath = "${PathKit.getProjectRootPath()}/resources/logo.png"
        val bufferedImage = QrCodeKit.genQrCode("http://www.baidu.com", logoImagePath)
        ImageKit.showImage(bufferedImage)
        Thread.sleep(3000)
    }

}