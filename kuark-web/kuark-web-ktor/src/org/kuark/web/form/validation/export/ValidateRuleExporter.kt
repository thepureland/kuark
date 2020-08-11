package org.kuark.web.form.validation.export

import org.apache.commons.collections.Predicate
import org.kuark.web.form.validation.js.JsConstraintCreator
import org.kuark.web.form.validation.js.converter.JsConstraint
import org.kuark.web.form.validation.js.converter.ConstraintConvertContext
import org.soul.commons.collections.CollectionTool
import org.soul.commons.lang.PackageTool
import org.soul.commons.lang.reflect.MethodTool
import org.soul.commons.lang.string.StringTool
import org.soul.commons.log.Log
import org.soul.commons.log.LogFactory
import org.soul.commons.validation.form.support.Comment
import org.soul.commons.validation.form.vo.ValidateFormVo
import org.soul.commons.validation.form.vo.ValidatePropertyVo
import org.soul.commons.validation.form.vo.ValidateRuleVo
import org.soul.web.shiro.support.BaseWebConf
import org.soul.web.validation.form.export.ValidateRuleExporter
import org.soul.web.validation.form.js.JsRuleCreator
import org.soul.web.validation.form.js.converter.IRuleConverter
import org.soul.web.validation.form.js.converter.JsRuleResult
import org.soul.web.validation.form.js.converter.RuleConvertContext
import org.soul.web.validation.form.js.converter.RuleConverterFactory
import java.lang.reflect.Method
import java.util.*

/**
 * Create by (admin) on 2015/2/9.
 */
object ValidateRuleExporter {
    private val LOG: Log = LogFactory.getLog(ValidateRuleExporter::class.java)
    fun export(baseWebConf: BaseWebConf?): List<ValidateFormVo> {
        val results: MutableList<ValidateFormVo> = ArrayList<ValidateFormVo>()
        val classes =
            getFormBeanClasses(baseWebConf)
        for (clazz in classes) {
            results.add(getRules(clazz))
        }
        return results
    }

    private fun getFormBeanClasses(baseWebConf: BaseWebConf?): Collection<Class<*>> {
        val packagePattern: String = baseWebConf.getFormPackagePattern()
        val packages: Set<String> = PackageTool.getPackages(packagePattern, true)
        val classesInPackage: MutableSet<Class<*>> = HashSet()
        for (aPackage in packages) {
            classesInPackage.addAll(PackageTool.getClassesInPackage(aPackage, true))
        }
        return CollectionTool.filter(classesInPackage, object : Predicate() {
            fun evaluate(o: Any): Boolean {
                val obj = o as Class<*>
                val className = obj.name
                val searchForm: Boolean = StringTool.endsWithIgnoreCase(className, "SearchForm")
                val contains: Boolean = StringTool.contains(className, "$")
                return !searchForm && !contains
            }
        })
    }

    private fun getRules(clazz: Class<*>): ValidateFormVo {
        val comment: Comment? = clazz.getAnnotation(Comment::class.java) as Comment?
        var formName = ""
        if (comment != null) {
            formName = getDescription(comment)
        }
        if (formName.isEmpty()) {
            LOG.warn("类【{0}】上的Comment注解值为空！", clazz)
            formName = clazz.name
            //			return null;
        }
        val validateFormVo = ValidateFormVo()
        validateFormVo.setFormName(formName)
        val annotations: MutableMap<String, MutableList<Annotation>> =
            HashMap() // Map<属性名, List<getter上的注解对象>>
        JsConstraintCreator.parseAnnotations(annotations, clazz, null) // Map<属性名, List<getter上的注解对象>>
        val readMethods: List<Method> = MethodTool.getReadMethods(clazz)
        for (getter in readMethods) {
            val property: String = MethodTool.getReadProperty(getter)
            if (property != null) {
                val annoes: List<Annotation>? = annotations[property]
                if (annoes != null) {
                    val validatePropertyVo = ValidatePropertyVo()
                    var cmt = getComment(clazz, getter, property)
                    if (cmt == null) {
                        cmt = property
                        LOG.warn("类【{0}】的【{1}】方法上无Comment注解！", clazz, getter.name)
                    }
                    validatePropertyVo.setPropertyName(cmt)
                    for (anno in annoes) {
                        val converter: IRuleConverter = RuleConverterFactory.getInstance(anno)
                        val context = ConstraintConvertContext(property, null, clazz)
                        val ruleResult: JsConstraint = converter.convert(context)
                        val rulesStr = ruleResult.rule
                        val msgsStr = ruleResult.msg
                        println("msgsStr: $msgsStr")
                        val msgs = msgsStr!!.split("(?<='),").toTypedArray()
                        val ruleAndMsg = msgs[0].split(":(?=')").toTypedArray()
                        val errMsg =
                            ruleAndMsg[1].replaceFirst("^'".toRegex(), "").replaceFirst("'$".toRegex(), "")
                                .trim { it <= ' ' }
                        validatePropertyVo.getRules().add(ValidateRuleVo(rulesStr, errMsg))
                    }
                    validateFormVo.getProperties().add(validatePropertyVo)
                }
            }
        }
        return validateFormVo
    }

    private fun getComment(
        clazz: Class<*>,
        getter: Method,
        prop: String
    ): String? {
        val annotations = getter.annotations
        for (annotation in annotations) {
            if (annotation.annotationType() == Comment::class.java) {
                val desc = getDescription(annotation as Comment)
                if (!desc.isEmpty()) {
                    return desc
                } else {
                    LOG.warn("类【{0}】的【{1}】方法上的Comment注解值为空！", clazz, getter.name)
                }
            }
        }
        return null
    }

    private fun getDescription(comment: Comment): String {
        var value: String = comment.value()
        val detail: String = comment.detail()
        if (!detail.trim { it <= ' ' }.isEmpty()) {
            value += ",$detail"
        }
        return value.trim { it <= ' ' }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val split =
            "rule:'1,2',rule:/\\d{2,3}/,rule:true,rule:5".split("(?<=['/\\d]|true|false),(?=[a-zA-Z]+)").toTypedArray()
        println(split.size)
    }
}