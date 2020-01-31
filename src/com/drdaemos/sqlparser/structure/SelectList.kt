package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.tokens.Comma
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// <select-list> ::= "*" | <select-reference> ["," <select-reference>]
class SelectList(tokens: List<Token> = emptyList(), position: Int = 0) : Node(tokens, position) {
    override fun compile() : Int {
        val token = getNextToken()
        if (token.expr === "*") {
            appendReference(token)
        }

        checkForReferenceSequence()

        return position
    }

    private fun checkForReferenceSequence() {
        var token = getNextToken()
        while (token is Comma) {
            token = getNextToken()
            if (token is Identifier) {
                appendReference(token)
                token = getNextToken()
            } else {
                throw UnrecognizedTokenException("Comma not followed by identifier", --position)
            }
        }

        rewind()
        return
    }

    private fun appendReference(token: Token) {
        val node = SelectReference(tokens, position)
        node.value = token.expr
        append(node)
    }
}