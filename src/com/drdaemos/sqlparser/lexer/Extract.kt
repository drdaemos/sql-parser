package com.drdaemos.sqlparser.lexer

import com.drdaemos.sqlparser.tokens.*

object Extract {
    fun keyphrase(input: MatchedToken) : Keyword? {
        val keywords = listOf(
            "LEFT JOIN", "RIGHT JOIN", "INNER JOIN", "CROSS JOIN", "UNION JOIN", "FULL JOIN", "ORDER BY", "GROUP BY"
        )
        return if (keywords.contains(input.value.toUpperCase())) Keyword(input.value, input.start) else null
    }

    fun keyword(input: MatchedToken) : Keyword? {
        val keywords = listOf(
            "SELECT", "DISTINCT", "INTO", "TOP", "FROM", "WHERE", "HAVING", "LIMIT", "OFFSET", "ON", "AS", "DESC", "ASC", "ALL"
        )
        return if (keywords.contains(input.value.toUpperCase())) Keyword(input.value, input.start) else null
    }

    fun sqlFunction(input: MatchedToken) : SqlFunction? {
        val keywords = listOf(
            "SUM", "COUNT", "AVG", "LOWER", "UPPER", "CONCAT", "FORMAT", "LEN"
        )
        return if (keywords.contains(input.value.toUpperCase())) SqlFunction(input.value, input.start) else null
    }

    fun blockDelimiter(input: MatchedToken) : BlockDelimiter? = BlockDelimiter(input.value, input.start)

    fun comma(input: MatchedToken) : Comma? = Comma(input.value, input.start)

    fun literal(input: MatchedToken) : Literal? = Literal(input.value, input.start)

    fun operator(input: MatchedToken) : Operator? = Operator(input.value, input.start)

    fun identifier(input: MatchedToken) : Identifier? = Identifier(input.value, input.start)
}