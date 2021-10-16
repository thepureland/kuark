package io.kuark.base.query

import io.kuark.base.query.enums.Operator
import java.io.Serializable

/**
 * 多个查询条件(where部分)封装类，支持与、或逻辑的任意组合、嵌套
 *
 * @author K
 * @since 1.0.0
 */
class Criteria : Serializable {

    /**
     * 列表各个元素均为and关系，元素类型如下： <br></br>
     * 1. Criterion
     * 2. Criteria
     * 3. 数组，元素类型可能为Criterion或Criteria， 数组各个元素是or关系
     */
    private val criterionGroups = mutableListOf<Any>()

    constructor()

    /**
     * 封装单个查询条件
     *
     * @param criterion
     */
    constructor(criterion: Criterion) {
        addAnd(criterion)
    }
    //endregion
    //region and
    /**
     * 添加单个查询条件
     *
     * @param property 属性名
     * @param operator 逻辑操作符枚举
     * @param value    属性值
     * @return 当前查询对象
     */
    fun addAnd(property: String, operator: Operator, value: Any?): Criteria {
        return addAnd(Criterion(property, operator, value))
    }

    /**
     * 添加多个查询条件，之间是与的关系
     *
     * @param criterions 查询条件可变参数
     * @return 当前查询对象
     */
    fun addAnd(vararg criterions: Criterion): Criteria {
        if (criterions.isNotEmpty()) {
            addCriterion(criterionGroups, *criterions)
        }
        return this
    }

    /**
     * 添加多个查询对象(嵌套)，之间是与的关系
     *
     * @param criterias 查询对象可变参数
     * @return 当前查询对象
     */
    fun addAnd(vararg criterias: Criteria): Criteria {
        if (criterias.isNotEmpty()) {
            addCriteria(criterionGroups, *criterias)
        }
        return this
    }

    /**
     * 添加一个查询条件和一个查询对象(嵌套)，之间是与的关系
     *
     * @param criterion 查询条件
     * @param criteria  查询对象(嵌套)
     * @return 当前查询对象
     */
    fun addAnd(criterion: Criterion, criteria: Criteria): Criteria {
        return addAnd(criterion).addAnd(criteria)
    }

    /**
     * 添加一个查询对象(嵌套)和一个查询条件，之间是与的关系
     *
     * @param criteria  查询对象(嵌套)
     * @param criterion 查询条件
     * @return 当前查询对象
     */
    fun addAnd(criteria: Criteria, criterion: Criterion): Criteria {
        return addAnd(criteria).addAnd(criterion)
    }
    //endregion

    //region or
    /**
     * 添加多个查询条件，之间是或的关系
     *
     * @param criterions 查询条件可变参数
     * @return 当前查询对象
     */
    fun addOr(vararg criterions: Criterion): Criteria {
        if (criterions.isNotEmpty()) {
            addOrGroup(addCriterion(null, *criterions))
        }
        return this
    }

    /**
     * 添加多个查询对象(嵌套)，之间是或的关系
     *
     * @param criterias 查询对象可变参数
     * @return 当前查询对象
     */
    fun addOr(vararg criterias: Criteria): Criteria {
        if (criterias.isNotEmpty()) {
            addOrGroup(addCriteria(null, *criterias))
        }
        return this
    }

    /**
     * 添加一个查询条件和一个查询对象(嵌套)，之间是或的关系
     *
     * @param criterion 查询条件
     * @param criteria  查询对象(嵌套)
     * @return 当前查询对象
     */
    fun addOr(criterion: Criterion, criteria: Criteria): Criteria {
        val objList = addCriterion(null, criterion!!)
        addCriteria(objList, criteria!!)
        addOrGroup(objList)
        return this
    }

    /**
     * 添加一个查询对象(嵌套)和一个查询条件，之间是或的关系
     *
     * @param criteria  查询对象(嵌套)
     * @param criterion 查询条件
     * @return 当前查询对象
     */
    fun addOr(criteria: Criteria, criterion: Criterion): Criteria {
        val objList = addCriteria(null, criteria)
        addCriterion(objList, criterion!!)
        addOrGroup(objList)
        return this
    }

    //endregion

    private fun addCriterion(list: MutableList<Any>?, vararg criterions: Criterion): MutableList<Any> {
        var list = list
        if (list == null) {
            list = ArrayList<Any>(criterions.size)
        }
        for (criterion in criterions) {
            val operator = criterion.operator
            val value = criterion.getValue()
            if (value != null && value !is Collection<*> && value !is Array<*> && "" != value || value is Collection<*> && !value.isEmpty()
                || value is Array<*> && (value as Array<Any?>).size != 0 || operator!!.acceptNull
            ) {
                list.add(criterion)
            }
        }
        return list
    }

    private fun addCriteria(list: MutableList<Any>?, vararg criterias: Criteria): MutableList<Any> {
        var list = list
        if (list == null) {
            list = ArrayList<Any>(criterias.size)
        }
        for (criteria in criterias) {
            if (!criteria.getCriterionGroups().isEmpty()) {
                list.add(criteria)
            }
        }
        return list
    }

    private fun addOrGroup(list: List<*>) {
        if (list.isNotEmpty()) {
            criterionGroups.add(list.toTypedArray())
        }
    }

    fun getCriterionGroups(): List<Any> {
        return criterionGroups
    }

    /**
     * 输出查询条件 <br></br>
     * 注: 输入内容只作查询条件关系的确认,不能当作实际执行sql语句!
     *
     * @return 多个查询条件字符串
     */
    override fun toString(): String {
        val sb = StringBuilder()
        if (criterionGroups.isNotEmpty()) {
            for (group in criterionGroups) {
                if (group is Criterion) {
                    sb.append(group.toString())
                } else if (group is Array<*>) { // or
                    if (group.isNotEmpty()) {
                        sb.append("(")
                        for (obj in group) {
                            sb.append(obj.toString())
                            sb.append(" OR ")
                        }
                        sb.delete(sb.length - 4, sb.length)
                        sb.append(")")
                    }
                } else if (group is Criteria) {
                    sb.append(group.toString())
                }
                sb.append(" AND ")
            }
            sb.delete(sb.length - 5, sb.length)
        }
        return sb.toString()
    }


    companion object {

        /**
         * 添加单个查询条件
         *
         * @param property 属性名
         * @param operator 逻辑操作符枚举
         * @param value    属性值
         * @return 新的查询对象
         */
        fun add(property: String, operator: Operator, value: Any?): Criteria =
            Criteria(Criterion(property, operator, value))

        //region static and

        /**
         * 添加多个查询条件，之间是与的关系
         *
         * @param criterions 查询条件可变参数
         * @return 新的查询对象
         */
        fun and(vararg criterions: Criterion): Criteria = Criteria().addAnd(*criterions)

        /**
         * 添加多个查询对象(嵌套)，之间是与的关系
         *
         * @param criterias 查询对象可变参数
         * @return 新的查询对象
         */
        fun and(vararg criterias: Criteria): Criteria = Criteria().addAnd(*criterias)

        /**
         * 添加一个查询条件和一个查询对象(嵌套)，之间是与的关系
         *
         * @param criterion 查询条件
         * @param criteria  查询对象(嵌套)
         * @return 新的查询对象
         */
        fun and(criterion: Criterion, criteria: Criteria): Criteria = Criteria().addAnd(criterion, criteria)

        /**
         * 添加一个查询对象(嵌套)和一个查询条件，之间是与的关系
         *
         * @param criteria  查询对象(嵌套)
         * @param criterion 查询条件
         * @return 新的查询对象
         */
        fun and(criteria: Criteria, criterion: Criterion): Criteria = Criteria().addAnd(criteria, criterion)

        //endregion

        //region static or
        /**
         * 添加多个查询条件，之间是或的关系
         *
         * @param criterions 查询条件可变参数
         * @return 新的查询对象
         */
        fun or(vararg criterions: Criterion): Criteria = Criteria().addOr(*criterions)

        /**
         * 添加多个查询对象(嵌套)，之间是或的关系
         *
         * @param criterias 查询对象可变参数
         * @return 新的查询对象
         */
        fun or(vararg criterias: Criteria): Criteria = Criteria().addOr(*criterias)

        /**
         * 添加一个查询条件和一个查询对象(嵌套)，之间是或的关系
         *
         * @param criterion 查询条件
         * @param criteria  查询对象(嵌套)
         * @return 新的查询对象
         */
        fun or(criterion: Criterion, criteria: Criteria): Criteria = Criteria().addOr(criterion, criteria)

        /**
         * 添加一个查询对象(嵌套)和一个查询条件，之间是或的关系
         *
         * @param criteria  查询对象(嵌套)
         * @param criterion 查询条件
         * @return 新的查询对象
         */
        fun or(criteria: Criteria, criterion: Criterion): Criteria = Criteria().addOr(criteria, criterion)
    }
}