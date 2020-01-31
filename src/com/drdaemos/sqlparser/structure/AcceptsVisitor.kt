package com.drdaemos.sqlparser.structure

import com.drdaemos.sqlparser.parser.Visitor

interface AcceptsVisitor {
    fun accept(visitor: Visitor)
}