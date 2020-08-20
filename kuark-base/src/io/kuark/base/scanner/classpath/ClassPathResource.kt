/**
 * Copyright 2010-2013 Axel Fontaine and the many contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.kuark.base.scanner.classpath

import io.kuark.base.io.IoKit
import io.kuark.base.scanner.support.Resource
import java.io.InputStreamReader
import java.io.Reader
import java.io.UnsupportedEncodingException
import java.net.URL
import java.net.URLDecoder
import java.nio.charset.Charset

/**
 * A resource on the classpath.
 */
class ClassPathResource(override val location: String?) : Comparable<ClassPathResource>, Resource {

    override val locationOnDisk: String?
        get() {
            val url = url ?: throw Exception("Unable to location resource on disk: $location")
            return try {
                URLDecoder.decode(url.path, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                throw Exception("Unknown encoding: UTF-8", e)
            }
        }

    /**
     * @return The url of this resource.
     */
    private val url: URL?
        get() = classLoader.getResource(location)

    /**
     * @return The classloader to load the resource with.
     */
    private val classLoader: ClassLoader
        get() = Thread.currentThread().contextClassLoader

    override fun loadAsString(encoding: String): String? {
        val inputStream = classLoader.getResourceAsStream(location)
            ?: throw Exception("Unable to obtain inputstream for resource: $location")
        val reader: Reader = InputStreamReader(inputStream, Charset.forName(encoding))
        return IoKit.toString(reader)
    }

    override fun loadAsBytes(): ByteArray? {
        val inputStream = classLoader.getResourceAsStream(location)
            ?: throw Exception("Unable to obtain inputstream for resource: $location")
        return IoKit.toByteArray(inputStream)
    }

    override val filename: String
        get() = location!!.substring(location.lastIndexOf("/") + 1)

    override fun exists(): Boolean {
        return url != null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as ClassPathResource
        return location == that.location
    }

    override fun hashCode(): Int {
        return location!!.hashCode()
    }

    override fun compareTo(other: ClassPathResource): Int {
        return location!!.compareTo(other.location!!)
    }

}