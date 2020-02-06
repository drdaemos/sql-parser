package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.BlockDelimiter
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Operator
import com.drdaemos.sqlparser.tokens.Token

// <filter-condition> ::= <predicate> | <predicate-group> *[<boolean-operator> <predicate> | <predicate-group>] |
// <boolean-operator> ::= "AND" | "OR"
class FilterCondition(children: List<Node> = mutableListOf()) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        parseNextExpression(compiler)

        var repeats = true
        while (repeats) {
            try {
                val token = compiler.getNextToken()
                if (token !is Operator || !listOf("AND", "OR").contains(token.expr.toUpperCase())) {
                    compiler.rewind()
                    throw UnrecognizedTokenException("Expression not connected by boolean operator")
                }
                compiler.append(this, BooleanOperator(token.expr))
                parseNextExpression(compiler)
            } catch (e: ParserException) {
                repeats = false
            }
        }
        return this
    }

    private fun parseNextExpression(compiler: Compiler) {
        when (compiler.getNextToken()) {
            is BlockDelimiter -> {
                compiler.rewind()
                compiler.append(this, PredicateGroup())
            }
            else -> {
                compiler.rewind()
                compiler.append(this, Predicate())
            }
        }
    }
}