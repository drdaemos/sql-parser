package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.EndOfTokens
import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// <table-reference> ::= <table-identifier> [<alias>] | <subquery> <alias>
class TableReference(children: List<Node> = mutableListOf()) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        var token = compiler.getNextToken()
        if (token is Identifier) {
            val identifier = token.expr
            var alias: String? = null
            try {
                token = compiler.getNextToken()
                if (token is Identifier) {
                    alias = token.expr
                } else {
                    compiler.rewind()
                }
            } catch (e: EndOfTokens) {}
            compiler.append(this, TableIdentifier(identifier, alias))
        }

        // TODO: process subquery
        return this
    }
}