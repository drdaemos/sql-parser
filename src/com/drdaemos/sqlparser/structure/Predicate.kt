package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Literal
import com.drdaemos.sqlparser.tokens.Operator
import com.drdaemos.sqlparser.tokens.Token

// <predicate> ::= <column-identifier> "IS" ["NOT"] "NULL" | <column-identifier> <comparison-operator> <expected-reference> | <column-identifier> "BETWEEN" <literal> "AND" <literal>
// <expected-reference> ::= <column-identifier> | <literal>
class Predicate(children: List<Node> = mutableListOf()) : Node(children) {
    var group: Int = 0
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
            "AND", "OR" -> throw UnrecognizedTokenException("Unexpected operator in Predicate", this)
            else -> throw UnrecognizedTokenException("Unknown token", this)
        }
        return this
    }

    private fun appendExpectedReference(compiler: Compiler) {
        val token = compiler.getNextToken()
        when (token) {
            is Literal -> compiler.append(this, LiteralValue(token.expr))
            is Identifier -> compiler.append(this, ColumnIdentifier(token.expr))
            else -> throw UnrecognizedTokenException("Unknown token in expected reference", this)
        }
    }
}