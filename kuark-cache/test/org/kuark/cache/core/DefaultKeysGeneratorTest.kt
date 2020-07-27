package org.kuark.cache.core

import org.junit.jupiter.api.Test

/**
 * 默认批量缓存key生成器测试用例
 *
 * @author K
 * @since 1.0.0
 */
internal class DefaultKeysGeneratorTest {

    @Test
    fun generate() {
        val keys = DefaultKeysGenerator().generate(null, null, "1", listOf(2, 3, 4), arrayOf("5", "6"), 7)
        assert(keys[0] == "1:2:5:7")
        assert(keys[1] == "1:3:6:7")
        assert(keys[2] == "1:4:5:7")
        assert(keys[3] == "1:2:6:7")
        assert(keys[4] == "1:3:5:7")
        assert(keys[5] == "1:4:6:7")
    }

}