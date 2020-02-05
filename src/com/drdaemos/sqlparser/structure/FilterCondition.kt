package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Operator
import com.drdaemos.sqlparser.tokens.Token

// <filter-condition> ::= <predicate> [<boolean-operator> <predicate>]
// <boolean-operator> ::= "AND" | "OR"
class FilterCondition(children: List<Node> = mutableListOf()) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        compiler.append(this, Predicate())

        var repeats = true
        while (repeats) {
            try {
                val token = compiler.getNextToken()
                if (token !is Operator || !listOf("AND", "OR").contains(token.expr.toUpperCase())) {
                    compiler.rewind()
                    throw UnrecognizedTokenException("Predicate not preceded by boolean operator")
                }
                compiler.append(this, BooleanOperator(token.expr))
                compiler.append(this, Predicate())
            } catch (e: ParserException) {
                repeats = false
            }
        }
        return this
    }
}