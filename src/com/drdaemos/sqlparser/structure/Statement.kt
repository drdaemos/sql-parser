package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.tokens.Token

// <statement> ::= <select-query>
class Statement(tokens: List<Token> = emptyList(), position: Int = 0) : Node(tokens, position) {

    override fun compile() : Int {
        append(SelectQuery(tokens, position))

        return position
    }
}