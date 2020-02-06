package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

enum class JoinType {
    INNER, LEFT, RIGHT, FULL, UNION, CROSS
}

// <joined-table> ::= <join-type> "JOIN" <table-reference> [<join-condition>]
// <join-type> ::= "INNER" | <outer-join-type> ["OUTER"] | "UNION" | "CROSS"
// <outer-join-type> ::= "LEFT" | "RIGHT" | "FULL"
class JoinedTable(children: List<Node> = mutableListOf(), var joinType: JoinType = JoinType.INNER) : Node(children) {

    override fun compile(compiler: Compiler): Node {
        checkJoinType(compiler)
        compiler.append(this, TableReference())
        compiler.append(this, JoinCondition(), true)

        return this
    }

    private fun checkJoinType(compiler: Compiler) {
        val token = compiler.getNextToken()
        when (token.expr.toUpperCase()) {
            "INNER JOIN" -> {
                joinType = JoinType.INNER
            }
            "LEFT JOIN" -> {
                joinType = JoinType.LEFT
            }
            "RIGHT JOIN" -> {
                joinType = JoinType.RIGHT
            }
            "FULL JOIN" -> {
                joinType = JoinType.FULL
            }
            "UNION JOIN" -> {
                joinType = JoinType.UNION
            }
            "CROSS JOIN" -> {
                joinType = JoinType.CROSS
            }
            else -> {
                compiler.rewind()
                throw UnrecognizedTokenException("Unknown JOIN type", this)
            }
        }
    }

    override fun toSqlString(): String {
        var message = joinType.toString() + " JOIN " + children.first().toSqlString()
        if (children.size > 1) {
            message += " ON " + children.last().toSqlString()
        }
        return message
    }
}