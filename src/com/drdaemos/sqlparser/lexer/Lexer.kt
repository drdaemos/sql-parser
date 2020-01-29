package com.drdaemos.sqlparser.lexer

import com.drdaemos.sqlparser.exceptions.LexerException
import com.drdaemos.sqlparser.tokens.*
import java.util.regex.Pattern
import java.util.regex.Pattern.CASE_INSENSITIVE

class Lexer {
    private lateinit var source: String
    private var position: Int = 0
    private var extractors = listOf(
        TokenExtractor(
            "^\\s*([a-zA-Z]+\\s[a-zA-Z]+)",
            Extract::keyphrase
        ),
        TokenExtractor(
            "^\\s*([a-zA-Z]+)",
            Extract::keyword
        ),
        TokenExtractor(
            "^\\s*([a-zA-Z]+)\\s*\\(.*\\)",
            Extract::sqlFunction
        ),
        // keyword operators
        TokenExtractor(
            "^\\s*(AND|OR|NOT|NOT NULL|LIKE|BETWEEN|ALL|ANY|SOME|EXISTS|IN|IS NULL|IS NOT NULL)",
            Extract::operator
        ),
        // math operators
        TokenExtractor(
            "^\\s*([+\\-*/%])",
            Extract::operator
        ),
        // comparison operators
        TokenExtractor(
            "^\\s*(=|>|<|>=|<=|<>)",
            Extract::operator
        ),
        // null literal
        TokenExtractor(
            "^\\s*(NULL)",
            Extract::literal
        ),
        // string literals
        TokenExtractor(
            "^\\s*(['\"]\\w+['\"])",
            Extract::literal
        ),
        // numeric literals
        TokenExtractor(
            "^\\s*(\\d+)",
            Extract::literal
        ),
        TokenExtractor(
            "^\\s*(,)",
            Extract::comma
        ),
        TokenExtractor(
            "^\\s*([\\(\\)])",
            Extract::blockDelimiter
        ),
        TokenExtractor(
            "^\\s*([\\w.]+)",
            Extract::identifier
        )
    )

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
        val iterator = extractors.iterator()

        while (iterator.hasNext()) {
            val tokenExtractor = iterator.next()
            val match = runMatcher(source, tokenExtractor.pattern)
            val token = match?.let { tokenExtractor.map(it) }
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