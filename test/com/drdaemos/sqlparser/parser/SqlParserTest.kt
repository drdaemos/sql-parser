package com.drdaemos.sqlparser.parser

import com.drdaemos.sqlparser.lexer.SqlLexer
import com.drdaemos.sqlparser.structure.*
import com.drdaemos.sqlparser.tokens.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class SqlParserTest {
    private val lexer: SqlLexer = SqlLexer()
    private val parser: SqlParser = SqlParser()

    @Test
    fun testParseBasic() {
        val tokens = listOf<Token>(
            Keyword("SELECT", 0),
            Operator("*", 7),
            Keyword("FROM", 9),
            Identifier("db", 14)
        )
        val expected = Statement(listOf(
            SelectQuery(listOf(
                SelectList(listOf(
                    SelectReference(listOf(
                        ColumnIdentifier("*")
                    ))
                )),
                TableExpression(listOf(
                    FromClause(listOf(
                        TableReference(listOf(
                            TableIdentifier("db")
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
        val query = "SELECT p.id, i.amount " +
                "FROM products p " +
                "LEFT JOIN inventory i ON p.id = i.product_id " +
                "WHERE p.category_id = 1 " +
                "LIMIT 10 OFFSET 5"
        val expected = Statement(listOf(
            SelectQuery(listOf(
                SelectList(listOf(
                    SelectReference(listOf(
                        ColumnIdentifier("p.id")
                    )),
                    SelectReference(listOf(
                        ColumnIdentifier("i.amount")
                    ))
                )),
                TableExpression(listOf(
                    FromClause(listOf(
                        TableReference(listOf(
                            TableIdentifier("products", "p")
                        ))
                    )),
                    JoinClause(listOf(
                        JoinedTable(listOf(
                            TableReference(listOf(
                                TableIdentifier("inventory", "i")
                            )),
                            JoinCondition(listOf(
                                FilterCondition(listOf(
                                    Predicate(listOf(
                                        ColumnIdentifier("p.id"),
                                        ComparisonOperator("="),
                                        ColumnIdentifier("i.product_id")
                                    ))
                                ))
                            ))
                        ), JoinType.LEFT)
                    )),
                    WhereClause(listOf(
                        FilterCondition(listOf(
                            Predicate(listOf(
                                ColumnIdentifier("p.category_id"),
                                ComparisonOperator("="),
                                LiteralValue("1")
                            ))
                        ))
                    )),
                    LimitClause(listOf(
                        LiteralValue("10"),
                        LiteralValue("5")
                    ))
                ))
            ))
        ))

        val tokens = lexer.getTokens(query)
        val actual = parser.compileTree(tokens)
        assertEquals(expected.toString(), actual.toString())
    }

    @Test
    fun testPredicateGroups() {
        val query = "SELECT * " +
                "FROM products p " +
                "LEFT JOIN inventory i ON (p.id = i.product_id) " +
                "WHERE p.category_id = 1 AND (i.test > 0 OR i.test2 < 0)"
        val expected = Statement(listOf(
            SelectQuery(listOf(
                SelectList(listOf(
                    SelectReference(listOf(
                        ColumnIdentifier("*")
                    ))
                )),
                TableExpression(listOf(
                    FromClause(listOf(
                        TableReference(listOf(
                            TableIdentifier("products", "p")
                        ))
                    )),
                    JoinClause(listOf(
                        JoinedTable(listOf(
                            TableReference(listOf(
                                TableIdentifier("inventory", "i")
                            )),
                            JoinCondition(listOf(
                                FilterCondition(listOf(
                                    PredicateGroup(listOf(
                                        FilterCondition(listOf(
                                            Predicate(listOf(
                                                ColumnIdentifier("p.id"),
                                                ComparisonOperator("="),
                                                ColumnIdentifier("i.product_id")
                                            ))
                                        ))
                                    ))
                                ))
                            ))
                        ), JoinType.LEFT)
                    )),
                    WhereClause(listOf(
                        FilterCondition(listOf(
                            Predicate(listOf(
                                ColumnIdentifier("p.category_id"),
                                ComparisonOperator("="),
                                LiteralValue("1")
                            )),
                            BooleanOperator("AND"),
                            PredicateGroup(listOf(
                                FilterCondition(listOf(
                                    Predicate(listOf(
                                        ColumnIdentifier("i.test"),
                                        ComparisonOperator(">"),
                                        LiteralValue("0")
                                    )),
                                    BooleanOperator("OR"),
                                    Predicate(listOf(
                                        ColumnIdentifier("i.test2"),
                                        ComparisonOperator("<"),
                                        LiteralValue("0")
                                    ))
                                ))
                            ))
                        ))
                    ))
                ))
            ))
        ))

        val tokens = lexer.getTokens(query)
        val actual = parser.compileTree(tokens)
        assertEquals(expected.toString(), actual.toString())
    }

    @Test
    fun testSubqueryAliasInFeatures() {
        val query = "SELECT id, (SELECT MAX(price) FROM table) AS max_price FROM table2 WHERE id IN (1,2,3)"
        val expected = Statement(listOf(
            SelectQuery(listOf(
                SelectList(listOf(
                    SelectReference(listOf(
                        ColumnIdentifier("id")
                    )),
                    SelectReference(listOf(
                        Subquery(listOf(
                            SelectQuery(listOf(
                                SelectList(listOf(
                                    SelectReference(listOf(
                                        FunctionExpression(listOf(
                                            FunctionIdentifier("MAX"),
                                            FunctionBody("price")
                                        ), "max_price")
                                    ))
                                )),
                                TableExpression(listOf(
                                    FromClause(listOf(
                                        TableReference(listOf(
                                            TableIdentifier("table")
                                        ))
                                    ))
                                ))
                            ))
                        ), "max_price")
                    ))
                )),
                TableExpression(listOf(
                    FromClause(listOf(
                        TableReference(listOf(
                            TableIdentifier("table2")
                        ))
                    )),
                    WhereClause(listOf(
                        FilterCondition(listOf(
                            Predicate(listOf(
                                ColumnIdentifier("id"),
                                InOperator("IN"),
                                LiteralValueList(listOf(
                                    LiteralValue("1"),
                                    LiteralValue("2"),
                                    LiteralValue("3")
                                ))
                            ))
                        ))
                    ))
                ))
            ))
        ))

        val tokens = lexer.getTokens(query)
        val actual = parser.compileTree(tokens)
        assertEquals(expected.toString(), actual.toString())
    }

    @Test
    fun testOriginalQuery() {
        val query = "SELECT author.name, count(book.id), sum(book.cost) " +
                "FROM author " +
                "LEFT JOIN book ON (author.id = book.author_id) " +
                "GROUP BY author.name " +
                "HAVING COUNT(*) > 1 AND SUM(book.cost) > 500 " +
                "LIMIT 10;"
        val expected = Statement(listOf(
            SelectQuery(listOf(
                SelectList(listOf(
                    SelectReference(listOf(
                        ColumnIdentifier("author.name")
                    )),
                    SelectReference(listOf(
                        FunctionExpression(listOf(
                            FunctionIdentifier("count"),
                            FunctionBody("book.id")
                        ))
                    )),
                    SelectReference(listOf(
                        FunctionExpression(listOf(
                            FunctionIdentifier("sum"),
                            FunctionBody("book.cost")
                        ))
                    ))
                )),
                TableExpression(listOf(
                    FromClause(listOf(
                        TableReference(listOf(
                            TableIdentifier("author")
                        ))
                    )),
                    JoinClause(listOf(
                        JoinedTable(listOf(
                            TableReference(listOf(
                                TableIdentifier("book")
                            )),
                            JoinCondition(listOf(
                                FilterCondition(listOf(
                                    PredicateGroup(listOf(
                                        FilterCondition(listOf(
                                            Predicate(listOf(
                                                ColumnIdentifier("author.id"),
                                                ComparisonOperator("="),
                                                ColumnIdentifier("book.author_id")
                                            ))
                                        ))
                                    ))
                                ))
                            ))
                        ))
                    )),
                    GroupByClause(listOf(
                        ColumnIdentifier("author.name")
                    )),
                    HavingClause(listOf(
                        FilterCondition(listOf(
                            Predicate(listOf(
                                FunctionExpression(listOf(
                                    FunctionIdentifier("COUNT"),
                                    FunctionBody("*")
                                )),
                                ComparisonOperator(">"),
                                LiteralValue("1")
                            )),
                            BooleanOperator("AND"),
                            Predicate(listOf(
                                FunctionExpression(listOf(
                                    FunctionIdentifier("SUM"),
                                    FunctionBody("book.cost")
                                )),
                                ComparisonOperator(">"),
                                LiteralValue("500")
                            ))
                        ))
                    ))
                ))
            ))
        ))

        val tokens = lexer.getTokens(query)
        val actual = parser.compileTree(tokens)
        assertEquals(expected.toString(), actual.toString())
    }
}