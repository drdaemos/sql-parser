# SQL Grammar

```bnf
<statement> ::= <select-query>
<select-query> ::= "SELECT" [<set-quantifier>] <select-list> <table-expression>
<set-quantifier> ::= "DISTINCT" | "ALL"
<select-list> ::= "*" | <select-reference> *["," <select-reference>]
<function-call> ::= <function-identifier> "(" <function-expression> ")"
<table-expression> ::= <from-clause> [<join-clause> <where-clause> <group-by-clause> <order-by-clause> <having-clause> <limit-clause>]
<from-clause> ::= "FROM" <table-reference> *["," <table-reference>]
<select-reference> ::= <column-identifier> [<alias>] | <function-call>
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
<predicate> ::= <column-identifier> "IS" ["NOT"] "NULL" | <column-identifier> <comparison-operator> <compared-reference> | <column-identifier> <in-operator> <containing-reference> | <column-identifier> "BETWEEN" <literal-value> "AND" <literal-value> |
<compared-reference> ::= <column-identifier> | <literal-value> | <subquery>
<containing-reference> ::= "(" <literal-value> *["," <literal-value>] ")" | <subquery>
<in-operator> ::= "IN"
<comparison-operator> ::= "=" | ">" | "<" | "<=" | ">=" | "LIKE"
<where-clause> ::= "WHERE" <filter-condition>
<group-by-clause> ::= "GROUP BY" <column-identifier> *["," <column-identifier>]
<order-by-clause> ::= "ORDER BY" <column-identifier> <order-direction> *["," <column-identifier> <order-direction>]
<order-direction> ::= "ASC" | "DESC"
<having-clause> ::= "HAVING" <filter-condition>
<limit-clause> ::= "LIMIT" <literal-value> ["OFFSET" <literal-value>]

// terminals
<column-identifier> ::= string
<function-identifier> ::= string
<table-identifier> ::= string
<function-expression> ::= string
<alias-identifier> ::= string
<literal-value> ::= string | numeric
```