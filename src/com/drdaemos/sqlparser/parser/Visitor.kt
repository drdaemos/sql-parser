package com.drdaemos.sqlparser.parser

import com.drdaemos.sqlparser.structure.Node

interface Visitor {
    abstract fun visitNode(node: Node)
}