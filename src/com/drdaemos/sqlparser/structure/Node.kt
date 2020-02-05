package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.EndOfTokens
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.parser.Visitor
import com.drdaemos.sqlparser.tokens.Comma
import com.drdaemos.sqlparser.tokens.Token

abstract class Node(val children: List<Node> = mutableListOf()) : AcceptsVisitor {
    var parent: Node? = null
        set(value) = run {field = value}

    init {
        this.children.forEach { it.parent = this }
    }

    override fun accept(visitor: Visitor) {
        visitor.visitNode(this)
    }

    abstract fun compile(compiler: Compiler) : Node

    override fun toString(): String {
        var message = this.javaClass.simpleName
        message += if (children.isNotEmpty()) ": [" +  children.joinToString { it.toString() } + "]" else ""
        return message
    }
}