# SQL Grammar

```bnf
<statement> ::= <select-query>
<select-query> ::= "SELECT" [<set-quantifier>] <select-list> <table-expression>
<set-quantifier> ::= "DISTINCT" | "ALL"
<select-list> ::= "*" | <select-reference> ["," <select-reference>]
<function-call> ::= <function-identifier> "(" <function-expression> ")"
<table-expression> ::= <from-clause> [<where-clause> <group-by-clause> <order-by-clause> <having-clause> <limit-clause>]
<from-clause> ::= "FROM" <table-reference> ["," <table-reference>]
<select-reference> ::= <column-identifier> [<alias>] | <function-call>
<table-reference> ::= <table-identifier> [<alias>] | <subquery> <alias> | <joined-table>
<alias> ::= ["AS"] <alias-identifier>
<subquery> ::= "(" <select-query> ")"
<joined-table> ::= <join-type> "JOIN" <table-reference> [<join-condition>]
<join-type> ::= "INNER" | <outer-join-type> ["OUTER"] | "UNION"
<outer-join-type> ::= "LEFT" | "RIGHT" | "FULL"
<join-condition> ::= "ON" <filter-condition>
<filter-condition> ::= <predicate> [<boolean-operator> <predicate>]
<boolean-operator> ::= "AND" | "OR"
<predicate> ::= <column-identifier> "IS" ["NOT"] "NULL" | <column-identifier> <comparison-operator> <expected-reference> | <column-identifier> "BETWEEN" <literal> "AND" <literal>
<expected-reference> ::= <column-identifier> | <literal>
<comparison-operator> ::= "=" | ">" | "<" | "<=" | ">=" | "LIKE"
<where-clause> ::= "WHERE" <filter-condition>
<group-by-clause> ::= "GROUP BY" <column-identifier> ["," <column-identifier>]
<order-by-clause> ::= "ORDER BY" <column-identifier> <order-direction> ["," <column-identifier> <order-direction>]
<order-direction> ::= "ASC" | "DESC"
<having-clause> ::= "HAVING" <filter-condition>
<limit-clause> ::= "LIMIT" numeric ["OFFSET" numeric]

// terminals
<column-identifier> ::= string
<function-identifier> ::= string
<table-identifier> ::= string
<function-expression> ::= string
<alias-identifier> ::= string
<literal> ::= string | numeric
```