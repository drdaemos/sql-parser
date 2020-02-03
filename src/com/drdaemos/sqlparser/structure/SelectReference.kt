package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// <select-reference> ::= <column-identifier> [<alias>] | <function-call>
class SelectReference(children: List<Node> = mutableListOf()) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        val token = compiler.getNextToken()
        if (token.expr == "*" || token is Identifier) {
            // todo: process alias
            compiler.append(this, ColumnIdentifier(token.expr))
        }
        // todo: process function call
        return this
    }
}