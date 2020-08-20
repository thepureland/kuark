package io.kuark.data.redis

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate

/**
 * redis测试用例
 *
 * @author K
 * @since 1.0.0
 */
@SpringBootTest
@SpringBootApplication
open class RedisTest {

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any?>

    @Test
    fun test() {
        val pair = Pair("1st", "2nd")
        redisTemplate.opsForValue().set("test", pair)
        assertTrue(redisTemplate.opsForValue().get("test") == pair)
    }

}