package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Literal
import com.drdaemos.sqlparser.tokens.Token

// <between-operator> ::= "BETWEEN" <literal> "AND" <literal>
class BetweenOperator(children: List<Node> = mutableListOf()) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        compiler.append(this, LiteralValue(compiler.getNextToken().expr))
        if (compiler.getNextToken().expr.toUpperCase() != "AND") {
            compiler.rewind()
            throw UnrecognizedTokenException("Between operator requires AND keyword", this)
        }
        compiler.append(this, LiteralValue(compiler.getNextToken().expr))

        return this
    }
}