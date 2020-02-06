package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Comma
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// <group-by-clause> ::= "GROUP BY" <column-identifier> *["," <column-identifier>]
class GroupByClause(children: List<Node> = mutableListOf()) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        val token = compiler.getNextToken()
        if (token.expr.toUpperCase() != "GROUP BY") {
            compiler.rewind()
            throw UnrecognizedTokenException("First token is not GROUP BY", this)
        }
        compiler.append(this, ColumnIdentifier(compiler.getNextToken().expr))
        compiler.repeatedAppend(this, { ColumnIdentifier(compiler.getNextToken().expr) }, true)
        return this
    }
}