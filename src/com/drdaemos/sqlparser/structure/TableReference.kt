package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// <table-reference> ::= <table-identifier> [<alias>] | <subquery> <alias> | <joined-table>
class TableReference(tokens: List<Token> = emptyList(), position: Int = 0) : Node(tokens, position) {
    override fun compile() : Int {
        append(TableIdentifier(tokens, position))
        return position
    }
}