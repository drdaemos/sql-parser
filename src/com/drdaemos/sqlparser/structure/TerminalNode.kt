package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.tokens.Token

abstract class TerminalNode(tokens: List<Token> = emptyList(), position: Int = 0) : Node(tokens, position) {
    var value: String? = null
        set(value) = run {field = value}

    override fun compile(): Int {
        return position
    }

    override fun toString(): String {
        return this.javaClass.simpleName + ": \"" + value + "\""
    }

    internal fun init(value: String) : TerminalNode {
        this.value = value
        return this
    }
}