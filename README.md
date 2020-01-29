# sql-parser

Simple SQL lexer and parser (but no yet) written in Kotlin. 

## Usage

```kotlin
import com.drdaemos.sqlparser.lexer.Lexer

// ...
val sqlString = "SELECT * FROM table"
val tokens = Lexer().getTokens(testString)

// [0: SELECT [Keyword], 7: * [Operator], ...]
println(tokens.toString())
```


