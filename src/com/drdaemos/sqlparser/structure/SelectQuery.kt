package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.tokens.Token

enum class SetQuantifier {
    DISTINCT, ALL
}

// <select-query> ::= "SELECT" [<set-quantifier>] <select-list> <table-expression>
class SelectQuery(tokens: List<Token> = emptyList(), position: Int = 0) : Node(tokens, position) {

    var setQuantifier: SetQuantifier = SetQuantifier.ALL
    override fun compile(): Int {
        val token = getNextToken()
        if (token.expr.toUpperCase() !== "SELECT") {
            throw UnrecognizedTokenException("First token is not SELECT in SelectQuery", --position)
        }
        checkSetQuantifier()

        append(SelectList(tokens, position))
        append(TableExpression(tokens, position))

        return position
    }

    private fun checkSetQuantifier() {
        val token = getNextToken()
        if (token.expr.toUpperCase() === "DISTINCT") {
            setQuantifier = SetQuantifier.DISTINCT
        } else {
            rewind()
        }
    }
}