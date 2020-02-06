package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.EndOfTokens
import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.BlockDelimiter
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// <table-reference> ::= <table-identifier> [<alias>] | <subquery> <alias>
class TableReference(children: List<Node> = mutableListOf()) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        when (val token = compiler.getNextToken()) {
            is Identifier -> {
                compiler.append(this, TableIdentifier(token.expr, compiler.parseAlias()))
            }
            is BlockDelimiter -> {
                compiler.rewind()
                val subquery = Subquery()
                compiler.append(this, subquery)
                subquery.alias = compiler.parseAlias()
            }
        }
        return this
    }
}