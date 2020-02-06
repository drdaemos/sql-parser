package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Comma
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// <order-by-clause> ::= "ORDER BY" <order-by-expression> *["," <order-by-expression>]
class OrderByClause(children: List<Node> = mutableListOf()) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        val token = compiler.getNextToken()
        if (token.expr.toUpperCase() != "ORDER BY") {
            compiler.rewind()
            throw UnrecognizedTokenException("First token is not ORDER BY", this)
        }
        compiler.append(this, OrderByExpression())
        compiler.repeatedAppend(this, { OrderByExpression() }, true)
        return this
    }
}