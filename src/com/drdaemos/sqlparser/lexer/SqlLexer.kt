package com.drdaemos.sqlparser.lexer

class SqlLexer : Lexer(
    detectors = listOf(
        TokenDetector(
            "^\\s*([a-zA-Z]+\\s[a-zA-Z]+)\\s",
            Extract::keyphrase
        ),
        TokenDetector(
            "^\\s*([a-zA-Z]+)\\s",
            Extract::keyword
        ),
        TokenDetector(
            "^\\s*([a-zA-Z]+)\\s*\\(.*\\)",
            Extract::sqlFunction
        ),
        // keyword operators
        TokenDetector(
            "^\\s*(AND|OR|IS NOT NULL|IS NULL|NOT NULL|NULL|NOT|LIKE|BETWEEN|ALL|ANY|SOME|EXISTS|IN)\\s",
            Extract::operator
        ),
        // math operators
        TokenDetector(
            "^\\s*([+\\-*/%])",
            Extract::operator
        ),
        // comparison operators
        TokenDetector(
            "^\\s*(=|>|<|>=|<=|<>)",
            Extract::operator
        ),
        // null literal
        TokenDetector(
            "^\\s*(NULL)",
            Extract::literal
        ),
        // string literals
        TokenDetector(
            "^\\s*(['\"]\\w+['\"])",
            Extract::literal
        ),
        // numeric literals
        TokenDetector(
            "^\\s*(\\d+)",
            Extract::literal
        ),
        TokenDetector(
            "^\\s*(,)",
            Extract::comma
        ),
        TokenDetector(
            "^\\s*([\\(\\)])",
            Extract::blockDelimiter
        ),
        TokenDetector(
            "^\\s*([\\w.]+)",
            Extract::identifier
        )
    )
) {
}