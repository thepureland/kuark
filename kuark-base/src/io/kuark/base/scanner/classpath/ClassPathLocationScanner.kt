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

import java.io.IOException
import java.net.URL

/**
 * Scans for classpath resources in this location.
 */
interface ClassPathLocationScanner {
    /**
     * Finds the resource names below this location on the classpath under this locationUrl.
     *
     * @param location    The system-independent location on the classpath.
     * @param locationUrl The system-specific physical location URL.
     * @return The system-independent names of the resources on the classpath.
     * @throws IOException when the scanning failed.
     */
    fun findResourceNames(location: String, locationUrl: URL): Set<String>
}