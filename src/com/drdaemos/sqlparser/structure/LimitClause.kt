package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// <limit-clause> ::= "LIMIT" numeric ["OFFSET" numeric]
class LimitClause(children: List<Node> = mutableListOf()) : Node(children) {
    var limit: Int? = null
    var offset: Int? = null

    override fun compile(compiler: Compiler): Node {
        if (compiler.getNextToken().expr.toUpperCase() != "LIMIT") {
            compiler.rewind()
            throw UnrecognizedTokenException("First token is not LIMIT", this)
        }
        var token = compiler.getNextToken()
        compiler.append(this, LiteralValue(token.expr))
        try {
            limit = token.expr.trim('"').toInt()
        } catch (e: NumberFormatException) {
        }

        if (compiler.getNextToken().expr.toUpperCase() != "OFFSET") {
            compiler.rewind()
            return this
        }
        token = compiler.getNextToken()
        compiler.append(this, LiteralValue(token.expr))
        try {
            offset = token.expr.trim('"', '\'').toInt()
        } catch (e: NumberFormatException) {
        }

        return this
    }
}