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
package org.kuark.base.scanner.filesystem

import org.kuark.base.io.IoKit
import org.kuark.base.scanner.support.Resource
import java.io.*
import java.nio.charset.Charset

/**
 * A resource on the filesystem.
 */
class FileSystemResource(location: String?) : Resource, Comparable<FileSystemResource> {
    /**
     * The location of the resource on the filesystem.
     */
    private val loc: File

    /**
     * @return The location of the resource on the classpath.
     */
    override val location: String?
        get() {
            var path = loc.path.replace("\\", "/")
            if (!path.startsWith("/")) {
                path = "/$path"
            }
            return path
        }

    /**
     * Retrieves the location of this resource on disk.
     *
     * @return The location of this resource on disk.
     */
    override val locationOnDisk: String?
        get() = loc.absolutePath

    /**
     * Loads this resource as a string.
     *
     * @param encoding The encoding to use.
     * @return The string contents of the resource.
     */
    override fun loadAsString(encoding: String): String? {
        return try {
            val inputStream: InputStream = FileInputStream(loc)
            val reader: Reader = InputStreamReader(inputStream, Charset.forName(encoding))
            IoKit.toString(reader)
        } catch (e: IOException) {
            throw Exception("Unable to load filesystem resource: " + loc.path + " (encoding: " + encoding + ")", e)
        }
    }

    /**
     * Loads this resource as a byte array.
     *
     * @return The contents of the resource.
     */
    override fun loadAsBytes(): ByteArray? {
        return try {
            val inputStream: InputStream = FileInputStream(loc)
            IoKit.toByteArray(inputStream)
        } catch (e: IOException) {
            throw Exception("Unable to load filesystem resource: " + loc.path, e)
        }
    }

    /**
     * @return The filename of this resource, without the path.
     */
    override val filename: String
        get() = loc.name

    /**
     * Checks whether this resource exists.
     *
     * @return `true` if it exists, `false` if not.
     */
    override fun exists(): Boolean {
        return loc.exists()
    }

    override fun compareTo(other: FileSystemResource): Int {
        return loc.compareTo(other.loc)
    }

    /**
     * Creates a new ClassPathResource.
     *
     * @param location The location of the resource on the filesystem.
     */
    init {
        this.loc = File(location)
    }
}