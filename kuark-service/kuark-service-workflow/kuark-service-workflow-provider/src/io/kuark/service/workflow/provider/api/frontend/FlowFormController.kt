package io.kuark.service.workflow.provider.api.frontend

import io.kuark.base.query.sort.Order
import io.kuark.service.workflow.common.vo.form.FlowFormSearchParams
import io.kuark.service.workflow.provider.biz.ibiz.IFlowFormBiz
import io.kuark.service.workflow.provider.model.po.FlowForm
import org.apache.ibatis.javassist.tools.rmi.ObjectNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


/**
 * 工作流表单控制器
 *
 * @author K
 * @since 1.0.0
 */
@RestController
//region your codes 1
@RequestMapping("/flow/form")
class FlowFormController {
//endregion your codes 1

    //region your codes 2

    @Autowired
    private lateinit var flowFormBiz: IFlowFormBiz

    /**
     * 返回指定id的流程表单
     *
     * @param id 表单id
     * @return 统一响应结果，当找不到表单时，属性data为null
     * @author K
     * @since 1.0.0
     */
    @GetMapping("/get")
    fun get(id: String): FlowForm {
        return flowFormBiz.get(id) ?: throw ObjectNotFoundException("找不到流程表单！id：$id")
    }

    /**
     * 查询表单
     *
     * @param searchParams 查询参数，当查询参数的属性不为空时才会将该属性作为查询条件，各属性间是”与“的关系
     * @param pageNum 分页页码，从1开始，默认为1，小于1将按1处理
     * @param pageSize 分页每页最大条数，默认为20，小于1将按不分页处理
     * @param orders 排序规则
     * @return 统一响应结果，当找不到表单时，属性data为空列表
     * @author K
     * @since 1.0.0
     */
    @GetMapping("/search")
    fun search(
        searchParams: FlowFormSearchParams, pageNum: Int, pageSize: Int, vararg orders: Order
    ): List<FlowForm> {
        return flowFormBiz.search(searchParams, pageNum, pageSize,  *orders)
    }

    /**
     * 保存表单
     *
     * @param flowForm 流程表单
     * @return 统一响应结果
     * @author K
     * @since 1.0.0
     */
    @PostMapping("/save")
    fun save(flowForm: FlowForm): Boolean {
        return flowFormBiz.saveOrUpdate(flowForm)
    }

    /**
     * 更新表单
     *
     * @param flowForm 流程表单
     * @return 统一响应结果
     * @author K
     * @since 1.0.0
     */
    @PutMapping("/update")
    fun update(flowForm: FlowForm): Boolean {
        return flowFormBiz.saveOrUpdate(flowForm)
    }

    /**
     * 删除表单
     *
     * @param id 表单id
     * @return 统一响应结果
     * @author K
     * @since 1.0.0
     */
    @DeleteMapping("/delete")
    fun delete(id: String): Boolean {
        return flowFormBiz.deleteById(id)
    }

    //endregion your codes 2

}