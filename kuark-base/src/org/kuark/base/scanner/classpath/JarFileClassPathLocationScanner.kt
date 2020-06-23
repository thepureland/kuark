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
package org.kuark.base.scanner.classpath

import java.io.IOException
import java.net.JarURLConnection
import java.net.URISyntaxException
import java.net.URL
import java.util.*
import java.util.jar.JarFile

/**
 * ClassPathLocationScanner for jar files.
 */
class JarFileClassPathLocationScanner : ClassPathLocationScanner {
    @Throws(IOException::class)
    override fun findResourceNames(location: String, locationUrl: URL): Set<String> {
        val jarFile = getJarFromUrl(locationUrl)
        return try {
            findResourceNamesFromJarFile(jarFile, location)
        } finally {
            jarFile.close()
        }
    }

    /**
     * Retrieves the Jar file represented by this URL.
     *
     * @param locationUrl The URL of the jar.
     * @return The jar file.
     * @throws IOException when the jar could not be resolved.
     */
    @Throws(IOException::class)
    private fun getJarFromUrl(locationUrl: URL): JarFile {
        val con = locationUrl.openConnection()
        if (con is JarURLConnection) {
            // Should usually be the case for traditional JAR files.
            val jarCon = con
            jarCon.useCaches = false
            return jarCon.jarFile
        }

        // No JarURLConnection -> need to resort to URL file parsing.
        // We'll assume URLs of the format "jar:path!/entry", with the protocol
        // being arbitrary as long as following the entry format.
        // We'll also handle paths with and without leading "file:" prefix.
        val urlFile = locationUrl.file
        val separatorIndex = urlFile.indexOf("!/")
        if (separatorIndex != -1) {
            val jarFileUrl = urlFile.substring(0, separatorIndex)
            return if (jarFileUrl.startsWith("file:")) {
                try {
                    JarFile(URL(jarFileUrl).toURI().schemeSpecificPart)
                } catch (ex: URISyntaxException) {
                    // Fallback for URLs that are not valid URIs (should hardly ever happen).
                    JarFile(jarFileUrl.substring("file:".length))
                }
            } else JarFile(jarFileUrl)
        }
        return JarFile(urlFile)
    }

    /**
     * Finds all the resource names contained in this directory within this jar file.
     *
     * @param jarFile  The jar file.
     * @param location The location to look under.
     * @return The resource names.
     * @throws java.io.IOException when reading the jar file failed.
     */
    private fun findResourceNamesFromJarFile(jarFile: JarFile, location: String): Set<String> {
        val resourceNames: MutableSet<String> = TreeSet()
        val entries = jarFile.entries()
        while (entries.hasMoreElements()) {
            val entryName = entries.nextElement().name
            if (entryName.startsWith(location)) {
                resourceNames.add(entryName)
            }
        }
        return resourceNames
    }
}