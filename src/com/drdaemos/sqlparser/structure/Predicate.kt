package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.*

// <predicate> ::= <column-identifier> "IS" ["NOT"] "NULL" | <column-identifier> <comparison-operator> <compared-reference> | <column-identifier> <in-operator> <containing-reference> | <column-identifier> "BETWEEN" <literal-value> "AND" <literal-value> |
// <compared-reference> ::= <column-identifier> | <literal> | <subquery>
// <containing-reference> ::= <literal-value-list> | <subquery>
class Predicate(children: List<Node> = mutableListOf()) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        var token = compiler.getNextToken()
        if (token !is Identifier) {
            compiler.rewind()
            throw UnrecognizedTokenException("First token in Predicate is not Identifier", this)
        }
        compiler.append(this, ColumnIdentifier(token.expr))

        token = compiler.getNextToken()
        if (token !is Operator) {
            compiler.rewind()
            throw UnrecognizedTokenException("No operator in Predicate", this)
        }

        when (token.expr.toUpperCase()) {
            "IS NULL", "IS NOT NULL" -> compiler.append(this, BooleanOperator(token.expr))
            "BETWEEN" -> compiler.append(this, BetweenOperator())
            "=", ">", "<", ">=", "<=", "<>", "LIKE" -> {
                compiler.append(this, ComparisonOperator(token.expr))
                appendExpectedReference(compiler)
            }
            "IN" -> {
                compiler.append(this, InOperator(token.expr))
                appendContainingReference(compiler)
            }
            "AND", "OR" -> throw UnrecognizedTokenException("Unexpected operator in Predicate", this)
            else -> throw UnrecognizedTokenException("Unknown token", this)
        }
        return this
    }

    private fun appendExpectedReference(compiler: Compiler) {
        when (val token = compiler.getNextToken()) {
            is Literal -> compiler.append(this, LiteralValue(token.expr))
            is Identifier -> compiler.append(this, ColumnIdentifier(token.expr))
            is BlockDelimiter -> {
                compiler.rewind()
                compiler.append(this, Subquery())
            }
            else -> throw UnrecognizedTokenException("Unknown token in expected reference", this)
        }
    }

    private fun appendContainingReference(compiler: Compiler) {
        if (compiler.getNextToken().expr != "(") {
            compiler.rewind()
            throw UnrecognizedTokenException("Containing reference must start with opening parenthesis", this)
        }
        when (compiler.getNextToken()) {
            is Literal -> {
                compiler.rewind(2)
                compiler.append(this, LiteralValueList())
            }
            is Keyword -> {
                compiler.rewind(2)
                compiler.append(this, Subquery())
            }
            else -> throw UnrecognizedTokenException("Unknown token in containing reference", this)
        }
    }
}