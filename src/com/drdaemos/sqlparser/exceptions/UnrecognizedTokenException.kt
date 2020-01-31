package com.drdaemos.sqlparser.exceptions

class UnrecognizedTokenException(override val message: String?) : ParserException(message) {
    var position: Int = 0
    constructor(message: String?, position: Int) : this(message) {
        this.position = position
    }
}