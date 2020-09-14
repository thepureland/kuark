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
package io.kuark.base.scanner.filesystem

import io.kuark.base.log.LogFactory
import io.kuark.base.scanner.support.Resource
import java.io.File
import java.io.IOException
import java.util.*

/**
 * FileSystem scanner.
 *
 * @since 1.0.0
 */
object FileSystemScanner {

    private val logger = LogFactory.getLog(FileSystemScanner::class)

    /**
     * Scans the FileSystem for resources under the specified location, starting
     * with the specified prefix and ending with the specified suffix.
     *
     * @param path The path in the filesystem to start searching. Subdirectories
     * are also searched.
     * @param prefix The prefix of the resource names to match.
     * @param suffix The suffix of the resource names to match.
     * @return The resources that were found.
     * @throws java.io.IOException when the location could not be scanned.
     * @since 1.0.0
     */
    fun scanForResources(path: String, prefix: String, suffix: String): Array<Resource> {
        logger.debug("Scanning for filesystem resources at '$path' (Prefix: '$prefix', Suffix: '$suffix')")
        if (!File(path).isDirectory) {
            throw Exception("Invalid filesystem path: $path")
        }
        val resources: MutableSet<Resource> = TreeSet()
        val resourceNames = findResourceNames(path, prefix, suffix)
        for (resourceName in resourceNames) {
            resources.add(FileSystemResource(resourceName))
            logger.debug("Found filesystem resource: $resourceName")
        }
        return resources.toTypedArray()
    }

    /**
     * Finds the resources names present at this location and below on the
     * classpath starting with this prefix and ending with this suffix.
     *
     * @param path The path on the classpath to scan.
     * @param prefix The filename prefix to match.
     * @param suffix The filename suffix to match.
     * @return The resource names.
     * @throws java.io.IOException when scanning this location failed.
     */
    private fun findResourceNames(path: String, prefix: String, suffix: String): Set<String> {
        val resourceNames = findResourceNamesFromFileSystem(path, File(path))
        return filterResourceNames(resourceNames, prefix, suffix)
    }

    /**
     * Finds all the resource names contained in this file system folder.
     *
     * @param scanRootLocation The root location of the scan on disk.
     * @param folder The folder to look for resources under on disk.
     * @return The resource names;
     * @throws IOException when the folder could not be read.
     */
    private fun findResourceNamesFromFileSystem(scanRootLocation: String, folder: File): Set<String> {
        logger.debug("Scanning for resources in path: " + folder.path + " (" + scanRootLocation + ")")
        val resourceNames: MutableSet<String> = TreeSet()
        val files = folder.listFiles()
        if (files != null) {
            for (file in files) {
                if (file.canRead()) {
                    if (file.isDirectory) {
                        resourceNames.addAll(findResourceNamesFromFileSystem(scanRootLocation, file))
                    } else {
                        resourceNames.add(file.path)
                    }
                }
            }
        }
        return resourceNames
    }

    /**
     * Filters this list of resource names to only include the ones whose
     * filename matches this prefix and this suffix.
     *
     * @param resourceNames The names to filter.
     * @param prefix The prefix to match.
     * @param suffix The suffix to match.
     * @return The filtered names set.
     */
    private fun filterResourceNames(resourceNames: Set<String>, prefix: String, suffix: String): Set<String> {
        val filteredResourceNames: MutableSet<String> = TreeSet()
        for (resourceName in resourceNames) {
            val fileName = resourceName.substring(resourceName.lastIndexOf(File.separator) + 1)
            if (fileName.startsWith(prefix) && fileName.endsWith(suffix)
                    && fileName.length > (prefix + suffix).length) {
                filteredResourceNames.add(resourceName)
            } else {
                logger.debug("Filtering out resource: $resourceName (filename: $fileName)")
            }
        }
        return filteredResourceNames
    }

}