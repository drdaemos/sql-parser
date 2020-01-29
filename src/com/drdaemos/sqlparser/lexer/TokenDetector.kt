package com.drdaemos.sqlparser.lexer

import com.drdaemos.sqlparser.tokens.Token

data class TokenDetector(val pattern: String, val map: (match: MatchedToken) -> Token?)