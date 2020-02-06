package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.EndOfTokens
import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.*

// <select-reference> ::= <column-identifier> [<alias>] | <function-call> [alias>] | <subquery> [<alias>]
class SelectReference(children: List<Node> = mutableListOf()) : Node(children) {
    override fun compile(compiler: Compiler): Node {
        when (val token = compiler.getNextToken()) {
            is Operator -> {
                if (token.expr == "*") compiler.append(this, ColumnIdentifier(token.expr))
                    else throw UnrecognizedTokenException("Unknown operator in reference", this)
            }
            is Identifier -> {
                compiler.append(this, ColumnIdentifier(token.expr, compiler.parseAlias()))
            }
            is BlockDelimiter -> {
                compiler.rewind()
                val subquery = Subquery()
                compiler.append(this, subquery)
                subquery.alias = compiler.parseAlias()
            }
            is SqlFunction -> {
                compiler.rewind()
                val function = FunctionExpression()
                compiler.append(this, function)
                function.alias = compiler.parseAlias()
            }
        }
        return this
    }
}