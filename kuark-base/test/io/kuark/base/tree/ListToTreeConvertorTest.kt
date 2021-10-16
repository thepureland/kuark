package io.kuark.base.tree

import org.junit.jupiter.api.Test

internal class ListToTreeConvertorTest {

    @Test
    fun testConvert() {
        val list = listOf(
            TestRecord("10", null, "根结点10"),
            TestRecord("11", "10", "10的子结点11"),
            TestRecord("12", "10", "10的子结点12"),
            TestRecord("20", null, "根结点20"),
            TestRecord("21", "20", "20的子结点21")
        )
        val treeList = ListToTreeConverter.convert(list)
        var result = treeList.size == 2
        val treeNode10 = treeList[0]
        result = result && "10" == treeNode10.id
        result = result && treeNode10._getChildren().size === 2
        val treeNode11 = treeNode10._getChildren()[0]
        result = result && "11" == treeNode11._getId()
        val treeNode12 = treeNode10._getChildren()[1]
        result = result && "12" == treeNode12._getId()
        val treeNode20 = treeList[1]
        result = result && "20" == treeNode20._getId()
        result = result && treeNode20._getChildren().size === 1
        val treeNode21 = treeNode20._getChildren()[0]
        result = result && "21" == treeNode21._getId()
        assert(result)
    }

    internal class TestRecord : ITreeNode<String> {
        var id: String
            private set
        var parentId: String? = null
            private set
        var name: String? = null
            private set

        constructor(id: String, parentId: String?, name: String?) : super() {
            this.id = id
            this.parentId = parentId
            this.name = name
        }

        companion object {
            private const val serialVersionUID = -3832151541461087421L
        }

        override fun _getId(): String = id

        override fun _getParentId(): String? = parentId

    }

}