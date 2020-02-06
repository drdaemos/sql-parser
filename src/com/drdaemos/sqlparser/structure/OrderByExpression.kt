package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Comma
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

enum class OrderDirection {
    ASC, DESC
}

// <order-by-expression> ::= <column-identifier> [<order-direction>]
// <order-direction> ::= "ASC" | "DESC"
class OrderByExpression(children: List<Node> = mutableListOf(), var direction: OrderDirection = OrderDirection.ASC) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        var token = compiler.getNextToken()
        if (token !is Identifier) {
            compiler.rewind()
            throw UnrecognizedTokenException("No identifier in Order By expression", this)
        }
        compiler.append(this, ColumnIdentifier(token.expr))
        token = compiler.getNextToken()
        when (token.expr) {
            "ASC" -> direction = OrderDirection.ASC
            "DESC" -> direction = OrderDirection.DESC
            else -> compiler.rewind()
        }
        return this
    }
}