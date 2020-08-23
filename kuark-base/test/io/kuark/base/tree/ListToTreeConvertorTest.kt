package io.kuark.base.tree

import org.junit.jupiter.api.Test
import java.util.*

internal class ListToTreeConvertorTest {

    @Test
    fun testConvert() {
        val list: MutableList<TestRecord> = ArrayList()
        list.add(TestRecord("10", null, "根结点10"))
        list.add(TestRecord("11", "10", "10的子结点11"))
        list.add(TestRecord("12", "10", "10的子结点12"))
        list.add(TestRecord("20", null, "根结点20"))
        list.add(TestRecord("21", "20", "20的子结点21"))
        val treeList = ListToTreeConvertor.convert(list)
        var result = treeList.size == 2
        val treeNode10 = treeList[0]
        result = result && "10" == treeNode10.getUserObject()!!.id
        result = result && treeNode10.getChildren().size === 2
        val treeNode11 = treeNode10.getChildren()[0]
        result = result && "11" == treeNode11.getUserObject()!!.id
        val treeNode12 = treeNode10.getChildren()[1]
        result = result && "12" == treeNode12.getUserObject()!!.id
        val treeNode20 = treeList[1]
        result = result && "20" == treeNode20.getUserObject()!!.id
        result = result && treeNode20.getChildren().size === 1
        val treeNode21 = treeNode20.getChildren()[0]
        result = result && "21" == treeNode21.getUserObject()!!.id
        assert(result)
    }

    internal class TestRecord : ITreeable<String?> {
        var id: String? = null
            private set
        var parentId: String? = null
            private set
        var name: String? = null
            private set

        constructor(id: String?, parentId: String?, name: String?) : super() {
            this.id = id
            this.parentId = parentId
            this.name = name
        }

        companion object {
            private const val serialVersionUID = -3832151541461087421L
        }

        override val selfUniqueIdentifier: String?
            get() = id
        override val parentUniqueIdentifier: String?
            get() = parentId
    }

}