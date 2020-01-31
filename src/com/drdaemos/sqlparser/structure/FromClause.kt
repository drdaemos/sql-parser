package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.tokens.Comma
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// "FROM" <table-reference> ["," <table-reference>]
class FromClause(tokens: List<Token> = emptyList(), position: Int = 0) : Node(tokens, position) {
    override fun compile() : Int {
        var token = getNextToken()
        if (token.expr.toUpperCase() !== "FROM") {
            throw UnrecognizedTokenException("First token is not FROM in FromClause", --position)
        }

        token = getNextToken()
        if (token is Identifier) {
            appendReference(token)
        }

        checkForReferenceSequence()
        return position
    }

    private fun checkForReferenceSequence() {
        try {
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
        } catch (e: ParserException) {}

        return
    }

    private fun appendReference(token: Token) {
        val node = TableReference(tokens, position)
        node.value = token.expr
        append(node)
    }
}