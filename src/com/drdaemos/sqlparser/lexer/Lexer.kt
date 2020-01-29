package com.drdaemos.sqlparser.lexer

import com.drdaemos.sqlparser.exceptions.LexerException
import com.drdaemos.sqlparser.tokens.*
import java.util.regex.Pattern
import java.util.regex.Pattern.CASE_INSENSITIVE

open class Lexer(private var detectors: List<TokenDetector>) {
    private lateinit var source: String
    private var position: Int = 0

    fun getTokens(input: String): List<Token> {
        source = input
        position = 0
        val tokens = mutableListOf<Token>()
        while (hasNext()) {
            findNext()?.let { tokens.add(it) }
        }
        return tokens
    }

    private fun hasNext(): Boolean {
        return position < source.length
    }

    private fun findNext(): Token? {
        val iterator = detectors.iterator()

        while (iterator.hasNext()) {
            val detector = iterator.next()
            val match = runMatcher(source, detector.pattern)
            val token = match?.let { detector.map(it) }
            token?.let {
                position += (match.end - position)
                return it
            }
        }

        // query terminator
        if (runMatcher(source, "^\\s*;") != null) {
            position = source.length
            return null
        }

        throw LexerException("Unknown token at position $position, \"" + source.substring(position) + "\"")
    }

    private fun runMatcher(source: String, pattern: String): MatchedToken? {
        val regex = Pattern.compile(pattern, CASE_INSENSITIVE)
        val matcher = regex.matcher(source)
        matcher.region(position, matcher.regionEnd())

        return if (matcher.find()) MatchedToken(matcher) else null
    }
}