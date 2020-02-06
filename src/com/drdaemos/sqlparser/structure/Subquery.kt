package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.BlockDelimiter
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Operator
import com.drdaemos.sqlparser.tokens.Token

// <subquery> ::= "(" <select-query> ")"
class Subquery(children: List<Node> = mutableListOf(), var alias: String? = null) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        if (compiler.getNextToken().expr != "(") {
            compiler.rewind()
            throw UnrecognizedTokenException("Subquery must start with opening parenthesis")
        }
        compiler.append(this, SelectQuery())
        if (compiler.getNextToken().expr != ")") {
            compiler.rewind()
            throw UnrecognizedTokenException("Subquery must end with closing parenthesis")
        }
        return this
    }
}