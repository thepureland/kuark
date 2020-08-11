package org.kuark.web.form.validation.bind

import org.soul.commons.lang.SystemTool
import org.soul.commons.lang.reflect.PropertyTool
import org.soul.commons.lang.string.StringTool
import org.soul.commons.validation.form.annotation.FormModel
import org.soul.commons.validation.form.support.FormPropertyConverter
import org.soul.web.shiro.support.IForm
import org.soul.web.validation.form.export.ValidateRuleExporter
import org.soul.web.validation.form.js.JsRuleCreator
import org.soul.web.validation.form.js.converter.IRuleConverter
import org.soul.web.validation.form.js.converter.RuleConvertContext
import org.springframework.beans.BeanUtils
import org.springframework.core.MethodParameter
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockMultipartHttpServletRequest
import org.springframework.util.StringUtils
import org.springframework.validation.BindException
import org.springframework.validation.Errors
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.ServletRequestDataBinder
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartRequest
import org.springframework.web.servlet.HandlerMapping
import org.springframework.web.util.WebUtils
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.*
import javax.servlet.ServletRequest
import javax.servlet.http.HttpServletRequest

/**
 * Create by (admin) on 6/19/15.
 */
class ValidFormMethodArgumentResolver : HandlerMethodArgumentResolver {
    fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(FormModel::class.java)
    }

    @Throws(Exception::class)
    fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer,
        webRequest: NativeWebRequest?,
        binderFactory: WebDataBinderFactory
    ): Any {
        val modelPrefixName: String = parameter.getParameterAnnotation(FormModel::class.java).value()
        val form = if (mavContainer.containsAttribute(modelPrefixName)) mavContainer.getModel()
            .get(modelPrefixName) else createAttribute(modelPrefixName, parameter, binderFactory, webRequest)
        val binder: WebDataBinder = binderFactory.createBinder(webRequest, form, modelPrefixName)
        if (binder.getTarget() != null) {
            val propertyMap =
                propertyMapping(form as IForm, modelPrefixName)
            bindRequestParameters(binder, webRequest, propertyMap)
            validateIfApplicable(binder, parameter)
            if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
                throw BindException(binder.getBindingResult())
            }
        }

        // Add resolved attribute and BindingResult at the end of the model
        val bindingResultModel: Map<String, Any> = binder.getBindingResult().getModel()
        mavContainer.removeAttributes(bindingResultModel)
        mavContainer.addAllAttributes(bindingResultModel)
        return binder.convertIfNecessary(binder.getTarget(), parameter.parameterType, parameter)
    }

    protected fun bindRequestParameters(
        binder: WebDataBinder,
        request: NativeWebRequest,
        propertyMap: Map<String, String>
    ) {
        val servletRequest = prepareServletRequest(request, propertyMap)
        (binder as ServletRequestDataBinder).bind(servletRequest)
    }

    protected fun propertyMapping(
        form: IForm,
        prefix: String
    ): Map<String, String> {
        val formClass: Class<out IForm?> = form.getClass()
        val className = formClass.name
        var map =
            propertyMapping[className] // Map<请求的属性名(数组的下标会被删掉), Form类中的属性名>
        if (map == null || SystemTool.isDebug()) {
            val allProperties: Set<String> = PropertyTool.getAllProperties(formClass)
            map = HashMap(allProperties.size, 1f)
            propertyMapping[className] = map
            for (formProperty in allProperties) {
                var requestProperty = formProperty
                if (StringTool.isNotBlank(prefix)) {
                    if (!formProperty.startsWith("$") && !formProperty.contains("_")) {
                        requestProperty = "$prefix.$formProperty"
                    }
                }
                requestProperty = FormPropertyConverter.toPot(requestProperty)
                map[requestProperty] = formProperty
            }
        }
        return map
    }

    private fun prepareServletRequest(
        request: NativeWebRequest,
        propertyMap: Map<String, String>
    ): ServletRequest {
        val nativeRequest = request.getNativeRequest() as HttpServletRequest
        val multipartRequest: MultipartRequest = WebUtils.getNativeRequest(nativeRequest, MultipartRequest::class.java)
        val mockRequest: MockHttpServletRequest
        if (multipartRequest != null) {
            val mockMultipartRequest = MockMultipartHttpServletRequest()
            for (file in multipartRequest.getFileMap().values()) {
                mockMultipartRequest.addFile(
                    MultipartFileWrapper(
                        getNewParameterName(file.getName(), propertyMap),
                        file
                    )
                )
            }
            mockRequest = mockMultipartRequest
        } else {
            mockRequest = MockHttpServletRequest()
        }
        for ((parameterName, value) in getUriTemplateVariables(request)) {
            //            if (isFormModelAttribute(parameterName, modelPrefixName)) {
            mockRequest.setParameter(getNewParameterName(parameterName, propertyMap), value)
            //            }
        }
        val arrayParams: MutableMap<String?, MutableList<String>> =
            HashMap()
        for (parameterEntry in nativeRequest.parameterMap.entries) {
            val entry: Entry<String, Array<String>> =
                parameterEntry as Entry<String, Array<String>>
            val parameterName: String = entry.key
            if (parameterName.contains("[]")) {
                continue
                //2016-06-08 wayne 如果是remote远程验证是数组时会把规则里面定义的data的key一起提交过来，会产生a[].value的这种数据，这个数据是不应该提交到后台的。
            }
            //            if (isFormModelAttribute(parameterName, modelPrefixName)) {
            val newParameterName = getNewParameterName(parameterName, propertyMap)
            if (FormPropertyConverter.isArrayProperty(parameterName)) {
                var objects = arrayParams[newParameterName]
                if (objects == null) {
                    objects = ArrayList()
                    arrayParams[newParameterName] = objects
                }
                objects.add(entry.value.get(0))
            } else {
                mockRequest.setParameter(newParameterName, entry.value)
            }
            //            }
        }

        // 处理数组属性
        if (!arrayParams.isEmpty()) {
            for (entry in arrayParams.entries) {
                var key: String = entry.key
                if (entry.value.size == 1) {
                    key += "[0]"
                }
                mockRequest.setParameter(key, entry.value.toTypedArray())
            }
        }
        return mockRequest
    }

    private fun getNewParameterName(
        requestProperty: String,
        propertyMap: Map<String, String>
    ): String? {
        var requestProperty = requestProperty
        requestProperty = requestProperty.replace("\\[\\d+\\]".toRegex(), "[]")
        var formProperty = propertyMap[requestProperty]
        if (StringTool.isBlank(formProperty)) {
            formProperty = requestProperty
        }
        return formProperty
    }

    /**
     * Obtain a value from the request that may be used to instantiate the
     * model attribute through type conversion from String to the target type.
     *
     * The default implementation looks for the attribute name to match
     * a URI variable first and then a request parameter.
     *
     * @param attributeName the model attribute name
     * @param request       the current request
     * @return the request value to try to convert or `null`
     */
    protected fun getRequestValueForAttribute(
        attributeName: String?,
        request: NativeWebRequest
    ): String? {
        val variables = getUriTemplateVariables(request)
        return if (StringUtils.hasText(variables[attributeName])) {
            variables[attributeName]
        } else if (StringUtils.hasText(request.getParameter(attributeName))) {
            request.getParameter(attributeName)
        } else {
            null
        }
    }

    protected fun getUriTemplateVariables(request: NativeWebRequest): Map<String, String> {
        val variables = request.getAttribute(
            HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST
        ) as Map<String, String>
        return variables ?: emptyMap()
    }

    /**
     * Validate the model attribute if applicable.
     *
     * The default implementation checks for `@javax.validation.Valid`,
     * Spring's [Validated],
     * and custom annotations whose name starts with "Valid".
     * @param binder the DataBinder to be used
     * @param methodParam the method parameter
     */
    protected fun validateIfApplicable(
        binder: WebDataBinder,
        methodParam: MethodParameter
    ) {
        val annotations = methodParam.parameterAnnotations
        for (ann in annotations) {
            val validatedAnn =
                AnnotationUtils.getAnnotation(ann, Validated::class.java)
            if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
                val hints =
                    validatedAnn?.value() ?: AnnotationUtils.getValue(
                        ann
                    )
                val validationHints =
                    if (hints is Array<Any>) hints else arrayOf(
                        hints
                    )
                binder.validate(validationHints)
                break
            }
        }
    }

    /**
     * Extension point to create the model attribute if not found in the model.
     * The default implementation uses the default constructor.
     * @param attributeName the name of the attribute (never `null`)
     * @param methodParam the method parameter
     * @param binderFactory for creating WebDataBinder instance
     * @param request the current request
     * @return the created model attribute (never `null`)
     */
    @Throws(Exception::class)
    protected fun createAttribute(
        attributeName: String?, methodParam: MethodParameter,
        binderFactory: WebDataBinderFactory?, request: NativeWebRequest?
    ): Any {
        return BeanUtils.instantiateClass(methodParam.parameterType)
    }

    /**
     * Whether to raise a fatal bind exception on validation errors.
     * @param binder the data binder used to perform data binding
     * @param methodParam the method argument
     * @return `true` if the next method argument is not of type [Errors]
     */
    protected fun isBindExceptionRequired(
        binder: WebDataBinder?,
        methodParam: MethodParameter
    ): Boolean {
        val i = methodParam.parameterIndex
        val paramTypes = methodParam.method.parameterTypes
        val hasBindingResult =
            paramTypes.size > i + 1 && Errors::class.java.isAssignableFrom(
                paramTypes[i + 1]
            )
        return !hasBindingResult
    }

    private class MultipartFileWrapper(val name: String?, delegate: MultipartFile) :
        MultipartFile {
        private val delegate: MultipartFile

        val originalFilename: String
            get() = delegate.getOriginalFilename()

        val contentType: String
            get() = delegate.getContentType()

        val isEmpty: Boolean
            get() = delegate.isEmpty()

        val size: Long
            get() = delegate.getSize()

        @get:Throws(IOException::class)
        val bytes: ByteArray
            get() = delegate.getBytes()

        @get:Throws(IOException::class)
        val inputStream: InputStream
            get() = delegate.getInputStream()

        @Throws(IOException::class, IllegalStateException::class)
        fun transferTo(dest: File?) {
            delegate.transferTo(dest)
        }

        init {
            this.delegate = delegate
        }
    }

    companion object {
        @Volatile
        private val propertyMapping: MutableMap<String, MutableMap<String, String>> =
            HashMap() // Map<Form类名, Map<提交的属性名, Form类中的属性名>>
    }
}