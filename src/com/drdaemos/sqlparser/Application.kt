package com.drdaemos.sqlparser

import com.drdaemos.sqlparser.lexer.Lexer
import com.drdaemos.sqlparser.queries.SelectQuery

fun main() {
    Application()
}

class Application {
    init {
        val testString = "SELECT * FROM test"
        val tokens = Lexer().getTokens(testString)
        println(tokens.toString())
        println(SelectQuery().toString())
    }
}