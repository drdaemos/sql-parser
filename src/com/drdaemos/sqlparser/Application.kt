package com.drdaemos.sqlparser

import com.drdaemos.sqlparser.lexer.SqlLexer
import com.drdaemos.sqlparser.queries.SelectQuery

fun main() {
    Application()
}

class Application {
    init {
        val testString = "SELECT * FROM test"
        val tokens = SqlLexer().getTokens(testString)
        println(tokens.toString())
        println(SelectQuery().toString())
    }
}