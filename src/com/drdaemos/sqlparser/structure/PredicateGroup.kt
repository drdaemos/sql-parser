package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.BlockDelimiter
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Operator
import com.drdaemos.sqlparser.tokens.Token

// <predicate-group> ::= "(" <filter-condition> ")"
class PredicateGroup(children: List<Node> = mutableListOf()) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        if (compiler.getNextToken().expr != "(") {
            compiler.rewind()
            throw UnrecognizedTokenException("Predicate group must start with opening parenthesis")
        }
        compiler.append(this, FilterCondition())
        if (compiler.getNextToken().expr != ")") {
            compiler.rewind()
            throw UnrecognizedTokenException("Predicate group must end with closing parenthesis")
        }
        return this
    }
}