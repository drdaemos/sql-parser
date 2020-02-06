package com.drdaemos.sqlparser

import com.drdaemos.sqlparser.info.SelectQueryInfo
import com.drdaemos.sqlparser.lexer.SqlLexer
import com.drdaemos.sqlparser.parser.SqlParser

fun main() {
    val query = "SELECT author.name, count(book.id), sum(book.cost) " +
            "FROM author " +
            "LEFT JOIN book ON (author.id = book.author_id) " +
            "GROUP BY author.name " +
            "HAVING COUNT(*) > 1 AND SUM(book.cost) > 500 " +
            "LIMIT 10;"
    val tokens = SqlLexer().getTokens(query)
    val statementTree = SqlParser().compileTree(tokens)
    val queryInfo = SelectQueryInfo()
    queryInfo.gather(statementTree)
    println(queryInfo.toString())
}