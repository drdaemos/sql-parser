package com.drdaemos.sqlparser.info

import com.drdaemos.sqlparser.structure.Node

interface QueryInfo {
    fun gather(rootNode: Node)
}