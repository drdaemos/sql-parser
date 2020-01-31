package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// <select-reference> ::= <column-identifier> [<alias>] | <function-call>
class SelectReference(tokens: List<Token> = emptyList(), position: Int = 0) : TerminalNode(tokens, position) {
}