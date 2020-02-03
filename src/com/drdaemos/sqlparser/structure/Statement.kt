package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Token

// <statement> ::= <select-query>
class Statement(children: List<Node> = mutableListOf()) : Node(children) {

    override fun compile(compiler: Compiler) : Node {
        compiler.append(this, SelectQuery())
        return this
    }
}