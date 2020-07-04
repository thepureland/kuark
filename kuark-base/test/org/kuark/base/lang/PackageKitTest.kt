package org.kuark.base.lang

import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.StringUtils
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.kuark.base.lang.string.EncodeKit

/**
 * PackageKit测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class PackageKitTest {

    @Test
    fun testGetClassesInPackage() {
        // in file
        val packageName = PackageKit::class.java.getPackage().name
        var classes = PackageKit.getClassesInPackage(packageName, true)
        assertTrue(classes.contains(PackageKit::class.java))
        assertTrue(classes.contains(EncodeKit::class.java))
        assertTrue(classes.contains(SystemKit::class.java))

        // in jar
        classes = PackageKit.getClassesInPackage("org.apache.commons.lang3", true)
        assertTrue(classes.contains(StringUtils::class.java))
        assertTrue(classes.contains(BooleanUtils::class.java))
    }

    @Test
    fun testGetPackages() {
        // in file
        var packages = PackageKit.getPackages("org.kuark.base.*", true)
        assertTrue(packages.contains("org.kuark.base.lang"))
        assertTrue(packages.contains("org.kuark.base.log"))

        // in jar using package pattern
        packages = PackageKit.getPackages("org.apache.**.lang3", true)
        assertTrue(packages.contains("org.apache.commons.lang3"))
    }
    
}