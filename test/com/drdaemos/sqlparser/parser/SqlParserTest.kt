package com.drdaemos.sqlparser.parser

import com.drdaemos.sqlparser.structure.*
import com.drdaemos.sqlparser.tokens.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class SqlParserTest {
    private val parser: SqlParser = SqlParser()

    @Test
    fun testParseBasic() {
        val tokens = listOf<Token>(
            Keyword("SELECT", 0),
            Operator("*", 7),
            Keyword("FROM", 9),
            Identifier("db", 14)
        )
        val expected = Statement().init(listOf(
            SelectQuery().init(listOf(
                SelectList().init(listOf(
                    SelectReference().init("*")
                )),
                TableExpression().init(listOf(
                    FromClause().init(listOf(
                        TableReference().init(listOf(
                            TableIdentifier().init("db")
                        ))
                    ))
                ))
            ))
        ))

        val actual = parser.compileTree(tokens)
        assertEquals(expected.toString(), actual.toString())
    }

    @Test
    fun testParseComplex() {
        val query = "SELECT p.id, i.amount FROM products p LEFT JOIN inventory i ON p.id = i.product_id WHERE p.category_id = 1 LIMIT 10 OFFSET 5"
        val expected = Statement().init(listOf(
            SelectQuery().init(listOf(
                SelectList().init(listOf(
                    SelectReference().init("p.id"),
                    SelectReference().init("p.amount")
                )),
                TableExpression().init(listOf(
                    FromClause().init(listOf(
                        TableReference().init(listOf(
                            TableIdentifier().init("products", "p")
                        )),
                        JoinedTable().init(listOf(
                            TableReference().init(listOf(
                                TableIdentifier().init("inventory", "i")
                            )),
                            JoinCondition().init(listOf(
                                FilterCondition(listOf(
                                    Predicate(listOf(
                                        ColumnIdentifier().init("p.id"),
                                        ComparisonOperator().init("="),
                                        ColumnIdentifier().init("i.product_id")
                                    ))
                                ))
                            ))
                        ))
                    )),
                    WhereClause().init(listOf(
                        FilterCondition(listOf(
                            Predicate(listOf(
                                ColumnIdentifier("p.category_id"),
                                ComparisonOperator("="),
                                LiteralValue("1")
                            ))
                        ))
                    )),
                    LimitClause().init("10", "5")
                ))
            ))
        ))

        val actual = parser.compileTree(tokens)
        assertEquals(expected.toString(), actual.toString())
    }
}