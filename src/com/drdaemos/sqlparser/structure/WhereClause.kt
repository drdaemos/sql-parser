package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Comma
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// <where-clause> ::= "WHERE" <filter-condition>
class WhereClause(children: List<Node> = mutableListOf()) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        val token = compiler.getNextToken()
        if (token.expr.toUpperCase() != "WHERE") {
            compiler.rewind()
            throw UnrecognizedTokenException("First token is not WHERE", this)
        }
        compiler.append(this, FilterCondition())
        return this
    }
}