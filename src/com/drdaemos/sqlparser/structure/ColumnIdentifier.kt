package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// <table-reference> ::= <table-identifier> [<alias>] | <subquery> <alias> | <joined-table>
class ColumnIdentifier(tokens: List<Token> = emptyList(), position: Int = 0) : TerminalNode(tokens, position) {
    var alias: String? = null
        set(value) = run {field = value}

    internal fun init(value: String, alias: String) : TerminalNode {
        this.value = value
        this.alias = alias
        return this
    }
}