package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.*

// <function-expression> ::= <function-identifier> "(" <function-body> ")"
class FunctionExpression(children: List<Node> = mutableListOf(), var alias: String? = null) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        var token = compiler.getNextToken()
        if (token !is SqlFunction) {
            compiler.rewind()
            throw UnrecognizedTokenException("Unknown function name", this)
        }
        compiler.append(this, FunctionIdentifier(token.expr))

        if (compiler.getNextToken().expr != "(") {
            compiler.rewind()
            throw UnrecognizedTokenException("Function call must have opening parenthesis", this)
        }
        var body = ""
        var repeats = true
        while (repeats) {
            token = compiler.getNextToken()
            when (token.expr) {
                "(" -> throw UnrecognizedTokenException("Function call doesn't support subqueries", this)
                ")" -> {
                    compiler.rewind()
                    repeats = false
                }
                else -> body += token.expr
            }
        }
        compiler.append(this, FunctionBody(body))
        if (compiler.getNextToken().expr != ")") {
            compiler.rewind()
            throw UnrecognizedTokenException("Function call must have closing parenthesis", this)
        }
        return this
    }

    override fun toSqlString(): String {
        return children.first().toSqlString() + "(" + children.last().toSqlString() + ")"
    }
}