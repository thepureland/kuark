package org.kuark.base.validation.validator

import org.kuark.base.validation.annotaions.Remote
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * Create by (admin) on 2015/2/12.
 */
class RemoteValidator : ConstraintValidator<Remote, Any?> {

    private lateinit var remote: Remote

    override fun initialize(remote: Remote) {
        this.remote = remote
    }

    override fun isValid(`object`: Any?, context: ConstraintValidatorContext): Boolean {
//        if (`object` == null) {
//            return true
//        }
//        val body = KuarkContext.get().clientInfo.requestContentString
//        val bodyParams: JSONObject = JSON.parseObject(body)
//        val pathImpl = (context as ConstraintValidatorContextImpl).constraintViolationCreationContexts[0].path
//        return isValid(pathImpl, bodyParams)

        return true
    }

//    private fun isValid(pathImpl: PathImpl, bodyParams: JSONObject): Boolean {
//        val (m, anno) = fetchValidMethod()
//        val paramTypes = m.parameterTypes
//        val params = arrayOfNulls<Any>(paramTypes.size)
//        if (anno.containsKey("batch")) {
//            return validByNestedCollection(paramTypes, anno, m, pathImpl, bodyParams)
//        }
//        val annoParams = anno["params"]
//        var parentObject: JSONObject = bodyParams
//        for (node in pathImpl) {
//            val paramData: Any = parentObject[node.name]!!
//            if (paramData is JSONObject) {
//                parentObject = paramData as JSONObject
//            } else {
//                //paramValue = (String) paramData;
//            }
//        }
//        for (i in paramTypes.indices) {
//            val paramType = paramTypes[i]
//            if (paramType == HttpServletRequest::class.java) {
//                params[i] = request
//            } else {
//                val paramName = annoParams!![i]
//                if (paramName != null) {
//                    val paramValue: String = parentObject.getString(paramName)
//                    params[i] = TypeKit.valueOfStr(paramValue, paramType)
//                }
//            }
//        }
//        val validateInstance = validateInstance
//        var pass: Any? = null
//        try {
//            pass = m.invoke(validateInstance, *params)
//            if (pass is String) {
//                return "true" == pass.toString()
//            }
//            if (pass is Boolean) {
//                return pass
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return "true" == pass
//    }
//
//    /**
//     * 用与集合类的异步校验
//     * function parameter annotation 需包含 @ValidBatch
//     *
//     * @param paramTypes
//     * @param anno
//     * @param m
//     * @param pathImpl
//     * @return
//     */
//    private fun validByNestedCollection(
//        paramTypes: Array<Class<*>>,
//        anno: Map<String, Map<Any, String>?>,
//        m: Method,
//        pathImpl: PathImpl,
//        bodyParams: JSONObject
//    ): Boolean {
//        var valid = true
//        val validType = anno["batch"]
//        val annoParams = anno["params"]
//        if (validType!!["type"] == "list") {
//            var currentIndex: Int? = null
//            if (pathImpl.asString().contains("[")) {
//                currentIndex = pathImpl.leafNode.index
//            }
//            var parentObject: JSONObject = bodyParams
//            if (currentIndex == null) {
//                for (node in pathImpl) {
//                    val paramData: Any = parentObject[node.name] as Any
//                    if (paramData is JSONObject) {
//                        parentObject = paramData as JSONObject
//                    }
//                }
//            }
//            val key = validType["key"]
//            val excludes =
//                validType["exclude"]!!.replace("(\\[)|(\\])".toRegex(), "").split(",").toTypedArray()
//            val params = arrayOfNulls<Any>(paramTypes.size)
//            for (i in paramTypes.indices) {
//                val paramType = paramTypes[i]
//                if (paramType == HttpServletRequest::class.java) {
//                    params[i] = request
//                } else {
//                    val paramName = annoParams!![i]
//                    if (paramName != null) {
//                        var paramValue: String? = null
//                        if (excludes.contains(paramName)) {
//                            paramValue = bodyParams.getString(paramName)
//                        } else if (currentIndex == null) {
//                            paramValue = parentObject.getString(paramName)
//                        } else {
//                            paramValue =
//                                (bodyParams.getJSONArray(key).get(currentIndex) as JSONObject).getString(paramName)
//                            if (paramValue == null) {
//                                return true
//                            }
//                        }
//                        params[i] = TypeKit.valueOfStr(paramValue, paramType)
//                    }
//                }
//            }
//            val validateInstance = validateInstance
//            var pass: Any? = null
//            try {
//                pass = m.invoke(validateInstance, *params)
//                if (pass is String) {
//                    if ("true" != pass.toString()) {
//                        valid = false
//                    }
//                }
//                if (pass is Boolean) {
//                    if (!(pass as Boolean?)!!) {
//                        valid = false
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//        return valid
//    }
//
//    //        Controller annotation = validateClass.getAnnotation(Controller.class);
////        if (annotation == null) {
////            return ConstructorTool.invokeConstructor(validateClass);
////        } else {
////            return SpringBeanTool.getBean(validateClass);
////        }
//
//    private val validateInstance: Any
//        private get() {
//            var validateClass: KClass<*> = remote.checkClass
//            if (validateClass.isAbstract) {
//                validateClass = try {
//                    SpringKit.getBean(validateClass)::class
//                } catch (e: Exception) {
//                    throw Exception("没有找到【${validateClass}】的实现类!!!")
//                }
//            }
//            //        Controller annotation = validateClass.getAnnotation(Controller.class);
////        if (annotation == null) {
////            return ConstructorTool.invokeConstructor(validateClass);
////        } else {
////            return SpringBeanTool.getBean(validateClass);
////        }
//            return ConstructorKit.invokeConstructor(validateClass)
//        }
//
//    private fun fetchValidMethod(): Pair<Method, Map<String, Map<Any, String>?>> {
//        lateinit var key: Method
//        lateinit var value: Map<String, Map<Any, String>>
//        var clazz = remote.checkClass
//        if (clazz.isAbstract) {
//            clazz = try {
//                SpringKit.getBean(clazz)::class.java.superclass::class
//            } catch (e: Exception) {
//                throw Exception("没有找到【${clazz}】的实现类!!!")
//            }
//        }
//        val method: String = remote.checkMethod
//        val methods = clazz.java.methods
//        for (m in methods) {
//            if (m.name == method && (m.returnType == String::class.java || m.returnType == Boolean::class.javaPrimitiveType || m.returnType == Boolean::class.java)) {
//                val paramTypes = m.parameterTypes
//                val params = fetchAnnoParams(m)
//                for (paramType in paramTypes) {
//                    if (paramType == HttpServletRequest::class.java) {
//                        key = m
//                    } else {
//                        key = m
//                        value = params
//                    }
//                }
//            }
//
//        }
//        return if (key == null) {
//            throw Exception(
//                "在类【${clazz}】中找不到名称为【${method}】，返回值类型为String，且参数为以下情况的方法：" + "带有HttpServletRequest类型或用RequestParam注解标注参数名！"
//            )
//        } else {
//            Pair(key, value)
//        }
//    }
//
//    private fun fetchAnnoParams(m: Method): Map<String, Map<Any, String>> {
//        val returnMap: MutableMap<String, Map<Any, String>> = HashMap(2)
//        val params: MutableMap<Any, String> = HashMap(2)
//        val parameterAnnotations = m.parameterAnnotations
//        var paramIndex = 0
//        for (i in parameterAnnotations.indices) {
//            val paramAnno = parameterAnnotations[i]
//            if (paramAnno.isNotEmpty()) {
//                if (paramAnno[0] is RequestParam) {
//                    params[paramIndex] = (paramAnno[0] as RequestParam).value()
//                    paramIndex++
//                }
//                if (paramAnno[0] is ValidInNestedCollection) {
//                    val validBatchMap: MutableMap<Any, String> =
//                        HashMap(2)
//                    validBatchMap["key"] = (paramAnno[0] as ValidInNestedCollection).key
//                    validBatchMap["type"] = (paramAnno[0] as ValidInNestedCollection).type
//                    validBatchMap["exclude"] = (paramAnno[0] as ValidInNestedCollection).exclude.contentToString()
//                    returnMap["batch"] = validBatchMap
//                    if (paramAnno.size > 1 && paramAnno[1] is RequestParam) {
//                        params[paramIndex] = (paramAnno[1] as RequestParam).value()
//                        paramIndex++
//                    }
//                }
//            }
//        }
//        returnMap["params"] = params
//        return returnMap
//    }
}