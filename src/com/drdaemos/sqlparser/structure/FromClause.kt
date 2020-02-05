package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.EndOfTokens
import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Comma
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// "FROM" <table-reference> *["," <table-reference>]
class FromClause(children: List<Node> = mutableListOf()) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        val token = compiler.getNextToken()
        if (token.expr.toUpperCase() != "FROM") {
            compiler.rewind()
            throw UnrecognizedTokenException("First token is not FROM in FromClause", this)
        }

        compiler.append(this, TableReference())

        try {
            compiler.repeatedAppend(this, { TableReference() }, true)
        } catch (e: EndOfTokens) {
        }

        return this
    }
}