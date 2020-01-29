package com.drdaemos.sqlparser.lexer

import com.drdaemos.sqlparser.tokens.Token

data class TokenExtractor(val pattern: String, val map: (match: MatchedToken) -> Token?)