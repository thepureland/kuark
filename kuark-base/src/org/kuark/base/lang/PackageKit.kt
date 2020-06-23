package org.kuark.base.lang

import org.kuark.base.log.LogFactory
import java.io.File
import java.io.IOException
import java.net.JarURLConnection
import java.net.URL
import java.net.URLDecoder
import java.util.*
import java.util.jar.JarFile

/**
 * java包工具类
 *
 * @since 1.0.0
 * @author admin
 * @time 2013-4-10 下午9:01:06
 */
object PackageKit {
    private val LOG = LogFactory.getLog(PackageKit::class)

    /**
     *
     *
     * 获取指定包名下的所有类
     *
     *
     * @param pkg 以"."分隔的java标准包名
     * @param recursive 是否循环迭代
     * @return Set<类>
     * @since 1.0.0
     * @author admin
     * @time 2013-4-10 下午9:23:38
    </类> */
    fun getClassesInPackage(pkg: String, recursive: Boolean): Set<Class<*>> {
        val action = Action(true)
        find(pkg, recursive, action)
        return action.getClasses()
    }

    /**
     *
     *
     * 根据正则表达式获取匹配的所有包 <br></br>
     * 包的开头部分必须明确指定
     *
     *
     * @param pkgPattern 包正则表达式
     * @param recursive 是否递归地获取子包
     * @return Set<包名>
     * @since 1.0.0
     * @author admin
     * @time 2013-4-11 下午11:06:40
    </包名> */
    fun getPackages(pkgPattern: String, recursive: Boolean): Set<String> {
        val action = Action(false)
        val packagePrefix = getPackagePrefix(pkgPattern)
        find(packagePrefix, recursive, action)
        val pkgs = action.getPkgs()
        val packs: MutableSet<String> = LinkedHashSet()
        val regExp = pkgPattern.replace("\\*".toRegex(), ".*")
        for (pack in pkgs) {
            if (pack.matches(Regex(regExp))) {
                packs.add(pack)
            }
        }
        return packs
    }

    private fun getPackagePrefix(pkgPattern: String): String {
        val pkgPrefix = StringBuilder()
        val pkgElems = pkgPattern.split("\\.").toTypedArray()
        for (pkgElem in pkgElems) {
            if (pkgElem.contains("*")) {
                break
            } else {
                pkgPrefix.append(pkgElem).append(".")
            }
        }
        return pkgPrefix.toString().replaceFirst("\\.$".toRegex(), "")
    }

    private fun find(packagePrefix: String, recursive: Boolean, action: Action) {
        // 获取包的名字 并进行替换
        val packageDirName = packagePrefix.replace('.', '/')
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        val dirs: Enumeration<URL>
        try {
            dirs = Thread.currentThread().contextClassLoader.getResources(packageDirName)
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                val url = dirs.nextElement()
                // 得到协议的名称
                val protocol = url.protocol
                // 如果是以文件的形式保存在服务器上
                if ("file" == protocol) {
                    // 获取包的物理路径
                    val filePath = URLDecoder.decode(url.file, "UTF-8")
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packagePrefix, filePath, recursive, action)
                } else if ("jar" == protocol) {
                    // 如果是jar包文件
                    // 定义一个JarFile
                    findAndAddClassesInPackageByJar(packagePrefix, packageDirName, url, recursive, action)
                }
            }
        } catch (e: IOException) {
            LOG.error(e)
        }
    }

    private fun findAndAddClassesInPackageByJar(
        packageName: String, packageDirName: String, url: URL,
        recursive: Boolean, action: Action
    ) {
        var packageName = packageName
        val jar: JarFile
        try {
            // 获取jar
            jar = (url.openConnection() as JarURLConnection).jarFile
            // 从此jar包 得到一个枚举类
            val entries = jar.entries()
            // 同样的进行循环迭代
            while (entries.hasMoreElements()) {
                // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                val entry = entries.nextElement()
                var name = entry.name
                // 如果是以/开头的
                if (name[0] == '/') {
                    // 获取后面的字符串
                    name = name.substring(1)
                }
                // 如果前半部分和定义的包名相同
                if (name.startsWith(packageDirName)) {
                    val idx = name.lastIndexOf('/')
                    // 如果以"/"结尾 是一个包
                    if (idx != -1) {
                        // 获取包名 把"/"替换成"."
                        packageName = name.substring(0, idx).replace('/', '.')
                    }
                    // 如果可以迭代下去 并且是一个包
                    if (idx != -1 || recursive) {
                        if (action.isRetrieveClass) {
                            // 如果是一个.class文件 而且不是目录
                            if (name.endsWith(".class") && !entry.isDirectory) {
                                // 去掉后面的".class" 获取真正的类名
                                val className = name.substring(packageName.length + 1, name.length - 6)
                                try {
                                    // 添加到classes
                                    action.addClass(Class.forName("$packageName.$className"))
                                } catch (e: ClassNotFoundException) {
                                    LOG.error(e)
                                }
                            }
                        } else { // 获取包名
                            if (entry.isDirectory) {
                                action.addPackage(packageName)
                            }
                        }
                    }
                }
            }
        } catch (e: IOException) {
            // log.error("在扫描用户定义视图时从jar包获取文件出错");
            LOG.error(e)
        }
    }

    /**
     * 以文件的形式来获取包下的所有Class
     */
    private fun findAndAddClassesInPackageByFile(
        packageName: String, packagePath: String,
        recursive: Boolean, action: Action
    ) {
        // 获取此包的目录 建立一个File
        val dir = File(packagePath)
        // 如果不存在或者 也不是目录就直接返回
        if (dir.exists() && dir.isDirectory) {
            if (action.isRetrieveClass == false) {
                action.addPackage(packageName)
            }
        } else {
//			 LOG.warn("用户定义包名 " + packageName + " 下没有任何文件");
            return
        }
        // 如果存在 就获取包下的所有文件 包括目录
        val dirfiles = dir.listFiles { file ->
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            recursive && file.isDirectory || file.name.endsWith(".class")
        }
        // 循环所有文件
        for (file in dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory) {
                findAndAddClassesInPackageByFile(
                    packageName + "." + file.name,
                    file.absolutePath,
                    recursive,
                    action
                )
            } else {
                if (action.isRetrieveClass) {
                    // 如果是java类文件 去掉后面的.class 只留下类名
                    val className = file.name.substring(0, file.name.length - 6)
                    try {
                        // 添加到集合中去
                        action.addClass(
                            Thread.currentThread().contextClassLoader
                                .loadClass("$packageName.$className")
                        )
                    } catch (e: ClassNotFoundException) {
                        LOG.error(e)
                    }
                }
            }
        }
    }

    private class Action(retrieveClass: Boolean) {
        var isRetrieveClass = true
        private val classes: MutableSet<Class<*>> = LinkedHashSet()
        private val pkgs: MutableSet<String> = LinkedHashSet()
        fun addClass(clazz: Class<*>) {
            classes.add(clazz)
        }

        fun addPackage(pkg: String) {
            pkgs.add(pkg)
        }

        fun getClasses(): Set<Class<*>> {
            return classes
        }

        fun getPkgs(): Set<String> {
            return pkgs
        }

        init {
            isRetrieveClass = retrieveClass
        }
    }
}