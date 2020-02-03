package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// <table-expression> ::= <from-clause> [<where-clause> <group-by-clause> <order-by-clause> <having-clause> <limit-clause>]
class TableExpression(children: List<Node> = mutableListOf()) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        compiler.append(this, FromClause())
        // TODO: Add other clauses
        return this
    }
}