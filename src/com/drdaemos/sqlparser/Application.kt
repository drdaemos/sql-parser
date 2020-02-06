package com.drdaemos.sqlparser

import com.drdaemos.sqlparser.lexer.SqlLexer
import com.drdaemos.sqlparser.parser.SqlParser
import com.drdaemos.sqlparser.queries.SelectQuery

fun main() {
    val testString = "SELECT * FROM test"
    val tokens = SqlLexer().getTokens(testString)
    val statementTree = SqlParser().compileTree(tokens)
    println(tokens.toString())
    println(statementTree.toString())
}