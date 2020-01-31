package com.drdaemos.sqlparser.lexer

import com.drdaemos.sqlparser.lexer.SqlLexer
import com.drdaemos.sqlparser.tokens.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasItems
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class SqlLexerTest {
    private val lexer: SqlLexer = SqlLexer()

    @Test
    fun testGetTokens() {
        val expected = listOf<Token>(
            Keyword("SELECT", 0),
            Operator("*", 7),
            Keyword("FROM", 9),
            Identifier("test", 14)
        )
        val testInput = "SELECT * FROM test"
        assertEquals(expected, lexer.getTokens(testInput))
    }

    @Test
    fun testComplexSelect() {
        val testInput = "SELECT author.name, count(book.id), sum(book.cost) " +
                "FROM author " +
                "LEFT JOIN book ON (author.id = book.author_id) " +
                "GROUP BY author.name " +
                "HAVING COUNT(*) > 1 AND SUM(book.cost) > 500 " +
                "LIMIT 10;"

        val actual = lexer.getTokens(testInput)
        assertThat<List<Token>>(actual, hasItems(
            Keyword("SELECT", 0),
            Identifier("author.name", 7),
            Comma(",", 18),
            SqlFunction("count", 20),
            BlockDelimiter("(", 25),
            Keyword("GROUP BY", 110),
            Operator("*", 144),
            Operator("AND", 151),
            Literal("10", 182)
        ))
    }
}