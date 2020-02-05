package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Comma
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// <join-clause> ::= <joined-table> *[<joined-table>]
class JoinClause(children: List<Node> = mutableListOf()) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        compiler.append(this, JoinedTable())
        var repeats = true
        while (repeats) {
            try {
                compiler.append(this, JoinedTable())
            } catch (e: ParserException) {
                repeats = false
            }
        }
        return this
    }
}