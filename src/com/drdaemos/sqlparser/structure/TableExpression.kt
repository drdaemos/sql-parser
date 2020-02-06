package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// <table-expression> ::= <from-clause> [<join-clause> <where-clause> <group-by-clause> <order-by-clause> <having-clause> <limit-clause>]
class TableExpression(children: List<Node> = mutableListOf()) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        compiler.append(this, FromClause())
        compiler.append(this, JoinClause(), true)
        compiler.append(this, WhereClause(), true)
        compiler.append(this, GroupByClause(), true)
        compiler.append(this, OrderByClause(), true)
        compiler.append(this, HavingClause(), true)
        compiler.append(this, LimitClause(), true)
        return this
    }
}