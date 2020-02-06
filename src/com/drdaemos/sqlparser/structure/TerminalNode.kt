package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Token

abstract class TerminalNode(var value: String) : Node() {

    override fun compile(compiler: Compiler): Node {
        return this
    }

    override fun toString(): String {
        return this.javaClass.simpleName + ": \"" + value + "\""
    }

    override fun toSqlString(): String {
        return value
    }
}