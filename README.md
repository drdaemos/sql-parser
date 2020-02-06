# sql-parser

Simple SQL lexer and parser written in Kotlin. 

## Supports

- SELECT statement with most of its syntax (DB agnostic)

## Doesn't support

- Anything except SELECT
- Union
- Common Table Expression
- Window functions

## Usage

```kotlin
import com.drdaemos.sqlparser.lexer.SqlLexer
import com.drdaemos.sqlparser.parser.SqlParser

// ...
val sqlString = "SELECT * FROM table"
val tokens = SqlLexer().getTokens(testString)
val statementTree = SqlParser().compileTree(tokens)

// [0: SELECT [Keyword], 7: * [Operator], ...]
println(tokens.toString())
// Statement: [SelectQuery: [SelectList: [SelectReference: [ColumnIdentifier: "*"]], ...
println(statementTree.toString())
```

## Supported Grammar

```bnf
<statement> ::= <select-query>
<select-query> ::= "SELECT" [<set-quantifier>] <select-list> <table-expression>
<set-quantifier> ::= "DISTINCT" | "ALL"
<select-list> ::= "*" | <select-reference> *["," <select-reference>]
<function-expression> ::= <function-identifier> "(" <function-body> ")"
<table-expression> ::= <from-clause> [<join-clause> <where-clause> <group-by-clause> <order-by-clause> <having-clause> <limit-clause>]
<from-clause> ::= "FROM" <table-reference> *["," <table-reference>]
<select-reference> ::= <column-identifier> [<alias>] | <function-expression> [<alias>] | <subquery> [<alias>]
<table-reference> ::= <table-identifier> [<alias>] | <subquery> <alias>
<alias> ::= ["AS"] <alias-identifier>
<subquery> ::= "(" <select-query> ")"
<join-clause> ::= <joined-table> *[<joined-table>]
<joined-table> ::= <join-type> "JOIN" <table-reference> [<join-condition>]
<join-type> ::= "INNER" | <outer-join-type> ["OUTER"] | "UNION"
<outer-join-type> ::= "LEFT" | "RIGHT" | "FULL"
<join-condition> ::= "ON" <filter-condition>
<filter-condition> ::= <predicate> | <predicate-group>  *[<boolean-operator> <predicate> | <predicate-group>]
<boolean-operator> ::= "AND" | "OR"
<predicate-group> ::= "(" <filter-condition> ")"
<predicate> ::= <left-hand-side> "IS" ["NOT"] "NULL" | <left-hand-side> <comparison-operator> <compared-reference> | <left-hand-side> <in-operator> <containing-reference> | <left-hand-side> "BETWEEN" <literal-value> "AND" <literal-value> |
<left-hand-side> ::= <column-identifier> | <function-expression>
<compared-reference> ::= <column-identifier> | <literal-value> | <subquery>
<containing-reference> ::= <literal-value-list> | <subquery>
<literal-value-list> ::= "(" <literal-value> *["," <literal-value>] ")"
<in-operator> ::= "IN"
<comparison-operator> ::= "=" | ">" | "<" | "<=" | ">=" | "LIKE"
<where-clause> ::= "WHERE" <filter-condition>
<group-by-clause> ::= "GROUP BY" <column-identifier> *["," <column-identifier>]
<order-by-clause> ::= "ORDER BY" <order-by-expression> *["," <order-by-expression>]
<order-by-expression> ::= <column-identifier> <order-direction>
<order-direction> ::= "ASC" | "DESC"
<having-clause> ::= "HAVING" <filter-condition>
<limit-clause> ::= "LIMIT" <literal-value> ["OFFSET" <literal-value>]

// terminals
<column-identifier> ::= string
<function-identifier> ::= string
<function-body> ::= string
<table-identifier> ::= string
<alias-identifier> ::= string
<literal-value> ::= string | numeric
```


