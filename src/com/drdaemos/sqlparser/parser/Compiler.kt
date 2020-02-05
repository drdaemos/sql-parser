package com.drdaemos.sqlparser.parser

import com.drdaemos.sqlparser.exceptions.EndOfTokens
import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.structure.Node
import com.drdaemos.sqlparser.structure.SelectQuery
import com.drdaemos.sqlparser.structure.Statement
import com.drdaemos.sqlparser.structure.TableReference
import com.drdaemos.sqlparser.tokens.Comma
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

    fun append(parent: Node, child: Node, optional: Boolean = false) {
        try {
            child.compile(this)
        } catch (e: ParserException) {
            if (optional) return else throw e
        }
        (parent.children as MutableList<Node>).add(child)
        child.parent = parent
    }

    fun repeatedAppend(parent: Node, childConstructor: () -> Node, optional: Boolean = false) {
        var token = getNextToken()
        while (token is Comma) {
            try {
                append(parent, childConstructor(), optional)
                token = getNextToken()
            } catch (e: UnrecognizedTokenException) {
                rewind()
                throw UnrecognizedTokenException("Comma not followed by repetition", parent)
            }
        }
        rewind()
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