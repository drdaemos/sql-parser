package com.drdaemos.sqlparser.tokens

abstract class Token(val expr: String, val position: Int) {
    override fun toString(): String {
        return position.toString() + ": " + expr + " [" + this.javaClass.simpleName + "]"
    }

    override fun equals(other: Any?): Boolean {
        return other is Token && expr == other.expr && position == other.position
    }

    override fun hashCode(): Int {
        var result = expr.hashCode()
        result = 31 * result + position
        return result
    }
}