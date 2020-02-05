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

        compiler.repeatedAppend(this, { SelectReference() }, true)

        return this
    }
}