package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Comma
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// <select-list> ::= "*" | <select-reference> ["," <select-reference>]
class SelectList(children: List<Node> = mutableListOf()) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        compiler.append(this, SelectReference())

        var token = compiler.getNextToken()
        while (token is Comma) {
            try {
                compiler.append(this, SelectReference())
                token = compiler.getNextToken()
            } catch (e: UnrecognizedTokenException) {
                compiler.rewind()
                throw UnrecognizedTokenException("Comma not followed by reference", this)
            }
        }

        compiler.rewind()

        return this
    }
}