package com.drdaemos.sqlparser.exceptions

import com.drdaemos.sqlparser.structure.Node

class EndOfTokens(override val message: String?) : ParserException(message) {
    var position: Int = 0
        set(value) = run {field = value}
    var caller: Node? = null
        set(value) = run {field = value}
    constructor(message: String?, caller: Node) : this(message) {
        this.caller = caller
    }
}