package com.drdaemos.sqlparser.parser

import com.drdaemos.sqlparser.exceptions.EndOfTokens
import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.structure.Node
import com.drdaemos.sqlparser.structure.SelectQuery
import com.drdaemos.sqlparser.structure.Statement
import com.drdaemos.sqlparser.tokens.Token

class Compiler(private val tokens: List<Token>, private var position: Int = 0) {

    fun compile() : Node {
        try {
            return Statement().compile(this)
        } catch (e: UnrecognizedTokenException) {
            e.position = position
            throw e
        }
    }

    fun append(parent: Node, child: Node) {
        child.compile(this)
        (parent.children as MutableList<Node>).add(child)
        child.parent = parent
    }

    fun rewind() {
        position--
    }

    @Throws(ParserException::class)
    fun getNextToken() : Token {
        if (hasNext()) {
            return tokens[position++]
        }

        throw EndOfTokens("")
    }

    private fun hasNext() = position < tokens.size
}