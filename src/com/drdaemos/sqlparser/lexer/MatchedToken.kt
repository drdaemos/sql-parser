package com.drdaemos.sqlparser.lexer

import java.util.regex.MatchResult

class MatchedToken(private val matcher: MatchResult) {
    val value: String
        get() = matcher.group(1)

    val start: Int
        get() = matcher.start(1)

    val end: Int
        get() = matcher.end(1)
}