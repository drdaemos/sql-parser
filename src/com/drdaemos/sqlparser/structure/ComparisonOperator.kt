package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.exceptions.ParserException
import com.drdaemos.sqlparser.exceptions.UnrecognizedTokenException
import com.drdaemos.sqlparser.tokens.Identifier
import com.drdaemos.sqlparser.tokens.Token

// <comparison-operator> ::= "=" | ">" | "<" | "<=" | ">=" | "LIKE"
class ComparisonOperator(value: String) : TerminalNode(value) {
}