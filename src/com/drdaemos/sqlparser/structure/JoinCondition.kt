package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// <join-condition> ::= "ON" <filter-condition>
class JoinCondition(children: List<Node> = mutableListOf()) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        val token = compiler.getNextToken()
        if (token.expr.toUpperCase() != "ON") {
            compiler.rewind()
            throw UnrecognizedTokenException("First token is not ON", this)
        }
        compiler.append(this, FilterCondition())
        return this
    }
}