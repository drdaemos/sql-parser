package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// <table-expression> ::= <from-clause> [<where-clause> <group-by-clause> <order-by-clause> <having-clause> <limit-clause>]
class TableExpression(tokens: List<Token> = emptyList(), position: Int = 0) : Node(tokens, position) {
    override fun compile() : Int {
        append(FromClause(tokens, position))
        return position
    }
}