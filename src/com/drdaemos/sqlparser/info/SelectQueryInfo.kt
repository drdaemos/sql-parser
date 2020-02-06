package com.drdaemos.sqlparser.info

import com.drdaemos.sqlparser.parser.Visitor
import com.drdaemos.sqlparser.structure.*

class SelectQueryInfo : QueryInfo, Visitor {
    private var selected: MutableList<String> = mutableListOf()
    private var from: MutableList<String> = mutableListOf()
    private var joins: MutableList<String> = mutableListOf()
    private var whereCondition: String? = null
    private var groupByColumns: MutableList<String> = mutableListOf()
    private var orderByColumns: MutableList<String> = mutableListOf()
    private var havingCondition: String? = null
    private var limit: Int? = null
    private var offset: Int? = null

    override fun gather(rootNode: Node) {
        rootNode.accept(this)
    }

    override fun visitNode(node: Node) {
        when (node) {
            is SelectList -> node.children.forEach { selected.add(it.toSqlString()) }
            is FromClause -> node.children.forEach { from.add(it.toSqlString()) }
            is JoinedTable -> joins.add(node.toSqlString())
            is WhereClause -> whereCondition = node.children.first().toSqlString()
            is GroupByClause -> node.children.forEach { groupByColumns.add(it.toSqlString()) }
            is OrderByClause -> node.children.forEach { orderByColumns.add(it.toSqlString()) }
            is HavingClause -> havingCondition = node.children.first().toSqlString()
            is LimitClause -> {
                limit = node.limit
                offset = node.offset
            }
        }
    }
}