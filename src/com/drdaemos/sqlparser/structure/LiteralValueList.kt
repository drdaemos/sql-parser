package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Literal
import com.drdaemos.sqlparser.tokens.Token

// <literal-value-list> ::= "(" <literal-value> *["," <literal-value>] ")"
class LiteralValueList(children: List<Node> = mutableListOf()) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        if (compiler.getNextToken().expr != "(") {
            compiler.rewind()
            throw UnrecognizedTokenException("Literal value list must start with opening parenthesis", this)
        }
        compiler.append(this, LiteralValue(compiler.getNextToken().expr))
        compiler.repeatedAppend(this, { LiteralValue(compiler.getNextToken().expr) }, true)
        if (compiler.getNextToken().expr != ")") {
            compiler.rewind()
            throw UnrecognizedTokenException("Literal value list must end with closing parenthesis", this)
        }

        return this
    }
}