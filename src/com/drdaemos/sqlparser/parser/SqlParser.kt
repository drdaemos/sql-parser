package com.drdaemos.sqlparser.parser

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.structure.Node
import com.drdaemos.sqlparser.tokens.Token

class SqlParser {
    private lateinit var compiler: Compiler

    @Throws(ParserException::class)
    fun compileTree(tokens: List<Token>): Node {
        compiler = Compiler(tokens, 0)
        return compiler.compile()
    }
}