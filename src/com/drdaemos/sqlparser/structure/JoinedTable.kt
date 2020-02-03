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
class JoinedTable(children: List<Node> = mutableListOf()) : Node(children) {

    var joinType: JoinType = JoinType.INNER
    override fun compile(compiler: Compiler): Node {
        TODO()
        return this
    }
}