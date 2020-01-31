package com.drdaemos.sqlparser.parser

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.structure.Node
import com.drdaemos.sqlparser.structure.Statement
import com.drdaemos.sqlparser.tokens.Token

class SqlParser {
    private var position: Int = 0
    private lateinit var rootNode: Node

    @Throws(ParserException::class)
    fun compileTree(tokens: List<Token>): Node {
        rootNode = Statement(tokens, position)
        rootNode.compile()
        return rootNode
    }
}