package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.parser.Visitor
import com.drdaemos.sqlparser.tokens.Token

abstract class Node (protected val tokens: List<Token> = emptyList(), protected var position: Int = 0) : AcceptsVisitor {
    var parent: Node? = null
        set(value) = run {field = value}
    var children: MutableList<Node> = mutableListOf()

    internal fun init(children: List<Node> = emptyList()) : Node {
        this.children = children as MutableList<Node>
        this.children.forEach { it.parent = this }
        return this
    }

    override fun accept(visitor: Visitor) {
        visitor.visitNode(this)
    }

    abstract fun compile() : Int

    fun hasNext() = position < tokens.size

    fun rewind() {
        position--
    }

    fun append(child: Node) {
        position = child.compile()
        children.add(child)
        child.parent = this
    }

    @Throws(ParserException::class)
    fun getNextToken() : Token {
        if (hasNext()) {
            return tokens[position++]
        }

        throw ParserException("Unexpected end of tokens")
    }

    override fun toString(): String {
        var message = this.javaClass.simpleName
        message += if (children.size > 0) ": [" +  children.joinToString { it.toString() } + "]" else ""
        return message
    }
}