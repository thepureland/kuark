package io.kuark.base.lang

import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.StringUtils
import org.junit.jupiter.api.Test
import io.kuark.base.lang.string.EncodeKit

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
        assert(classes.contains(PackageKit::class))
        assert(classes.contains(EncodeKit::class))
        assert(classes.contains(SystemKit::class))

        // in jar
        classes = PackageKit.getClassesInPackage("org.apache.commons.lang3", true)
        assert(classes.contains(StringUtils::class))
        assert(classes.contains(BooleanUtils::class))
    }

    @Test
    fun testGetPackages() {
        // in file
        var packages = PackageKit.getPackages("io.kuark.base.*", true)
        assert(packages.contains("io.kuark.base.lang"))
        assert(packages.contains("io.kuark.base.log"))

        // in jar using package pattern
        packages = PackageKit.getPackages("org.apache.**.lang3", true)
        assert(packages.contains("org.apache.commons.lang3"))
    }
    
}