package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.parser.Compiler
import com.drdaemos.sqlparser.tokens.Token

enum class SetQuantifier {
    DISTINCT, ALL
}

// <select-query> ::= "SELECT" [<set-quantifier>] <select-list> <table-expression>
class SelectQuery(children: List<Node> = mutableListOf()) : Node(children) {

    var setQuantifier: SetQuantifier = SetQuantifier.ALL
    override fun compile(compiler: Compiler): Node {
        val token = compiler.getNextToken()
        if (token.expr.toUpperCase() != "SELECT") {
            compiler.rewind()
            throw UnrecognizedTokenException("First token is not SELECT", this)
        }
        checkSetQuantifier(compiler)

        compiler.append(this, SelectList())
        compiler.append(this, TableExpression())

        return this
    }

    private fun checkSetQuantifier(compiler: Compiler) {
        val token = compiler.getNextToken()
        if (token.expr.toUpperCase() == "DISTINCT") {
            setQuantifier = SetQuantifier.DISTINCT
        } else {
            compiler.rewind()
        }
    }
}