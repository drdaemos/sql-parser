package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// <predicate> ::= <column-identifier> "IS" ["NOT"] "NULL" | <column-identifier> <comparison-operator> <expected-reference> | <column-identifier> "BETWEEN" <literal> "AND" <literal>
// <expected-reference> ::= <column-identifier> | <literal>
class Predicate(children: List<Node> = mutableListOf()) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        TODO()
        return this
    }
}