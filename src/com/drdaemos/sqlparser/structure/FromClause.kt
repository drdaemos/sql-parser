package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.EndOfTokens
import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Comma
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// "FROM" <table-reference> ["," <table-reference>]
class FromClause(children: List<Node> = mutableListOf()) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        var token = compiler.getNextToken()
        if (token.expr.toUpperCase() != "FROM") {
            compiler.rewind()
            throw UnrecognizedTokenException("First token is not FROM in FromClause", this)
        }

        compiler.append(this, TableReference())

        try {
            token = compiler.getNextToken()
            while (token is Comma) {
                try {
                    compiler.append(this, TableReference())
                    token = compiler.getNextToken()
                } catch (e: UnrecognizedTokenException) {
                    compiler.rewind()
                    throw UnrecognizedTokenException("Comma not followed by reference", this)
                }
            }

            compiler.rewind()
        } catch (e: EndOfTokens) {
        }

        return this
    }
}