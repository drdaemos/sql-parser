package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.*

// <predicate> ::= <left-hand-side> "IS" ["NOT"] "NULL" | <left-hand-side> <comparison-operator> <compared-reference> | <left-hand-side> <in-operator> <containing-reference> | <left-hand-side> "BETWEEN" <literal-value> "AND" <literal-value> |
// <left-hand-side> ::= <column-identifier> | <function-expression>
// <compared-reference> ::= <column-identifier> | <literal> | <subquery>
// <containing-reference> ::= <literal-value-list> | <subquery>
class Predicate(children: List<Node> = mutableListOf()) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        appendLeftHandSide(compiler)

        val token = compiler.getNextToken()
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

    private fun appendLeftHandSide(compiler: Compiler) {
        when (val token = compiler.getNextToken()) {
            is Identifier -> compiler.append(this, ColumnIdentifier(token.expr))
            is SqlFunction -> {
                compiler.rewind()
                compiler.append(this, FunctionExpression())
            }
            else -> {
                compiler.rewind()
                throw UnrecognizedTokenException("First token in Predicate is not Identifier", this)
            }
        }
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