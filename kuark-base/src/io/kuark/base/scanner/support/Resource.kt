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
package io.kuark.base.scanner.support

/**
 * A loadable resource.
 */
interface Resource {
    /**
     * @return The location of the resource on the classpath.
     */
    val location: String?

    /**
     * Retrieves the location of this resource on disk.
     *
     * @return The location of this resource on disk.
     */
    val locationOnDisk: String?

    /**
     * Loads this resource as a string.
     *
     * @param encoding The encoding to use.
     * @return The string contents of the resource.
     */
    fun loadAsString(encoding: String): String?

    /**
     * Loads this resource as a byte array.
     *
     * @return The contents of the resource.
     */
    fun loadAsBytes(): ByteArray?

    /**
     * @return The filename of this resource, without the path.
     */
    val filename: String

    /**
     * Checks whether this resource exists.
     *
     * @return `true` if it exists, `false` if not.
     */
    fun exists(): Boolean
}