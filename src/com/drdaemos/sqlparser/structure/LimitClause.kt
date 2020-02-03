package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// <limit-clause> ::= "LIMIT" numeric ["OFFSET" numeric]
class LimitClause(value: String, var offset: String? = null) : TerminalNode(value) {
}