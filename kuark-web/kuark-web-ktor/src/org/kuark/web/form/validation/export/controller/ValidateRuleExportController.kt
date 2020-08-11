package org.kuark.web.form.validation.export.controller

import org.kuark.web.form.validation.export.ValidateRuleExporter
import org.soul.commons.collections.CollectionQueryTool
import org.soul.commons.lang.string.StringTool
import org.soul.commons.validation.form.vo.ValidateFormVo
import org.soul.web.router.ValidateRouter
import org.soul.web.shiro.support.BaseWebConf
import org.soul.web.validation.form.export.ValidateRuleExporter
import org.soul.web.validation.form.js.JsRuleCreator
import org.soul.web.validation.form.js.converter.IRuleConverter
import org.soul.web.validation.form.js.converter.RuleConvertContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest

/**
 * Create by (admin) on 2015/2/9.
 */
@Controller
class ValidateRuleExportController {

    @Autowired
    @Qualifier(BaseWebConf.BEAN_NAME)
    private val baseWebConf: BaseWebConf? = null

    @RequestMapping(value = ValidateRouter.LIST)
    fun list(model: Model): String {
        if (validateForms == null) {
            validateForms = ValidateRuleExporter.export(baseWebConf)
        }
        val formList: List<String> =
            CollectionQueryTool.queryProperty(validateForms, "formName")
        model.addAttribute("formList", formList)
        return VALIDATE_RULE_INDEX
    }

    @RequestMapping(value = ValidateRouter.EXPORT)
    fun export(request: HttpServletRequest, model: Model): String {
        if (validateForms == null) {
            validateForms = ValidateRuleExporter.export(baseWebConf)
        }
        val indexStr = request.getParameter("index")
        if (StringTool.isNotBlank(indexStr) && StringTool.isNumeric(indexStr.trim { it <= ' ' })) {
            try {
                val index = Integer.valueOf(indexStr.trim { it <= ' ' })
                if (index < validateForms!!.size) {
                    val validateFormVo: ValidateFormVo? =
                        validateForms!![index]
                    model.addAttribute("validateRules", validateFormVo)
                    return VALIDATE_RULE_EXPORT
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return list(model)
    }

    companion object {
        private var validateForms: List<ValidateFormVo?>? = null
        private const val VALIDATE_RULE_EXPORT = "devtool/validation/ExportRules"
        private const val VALIDATE_RULE_INDEX = "devtool/validation/Index"
    }
}