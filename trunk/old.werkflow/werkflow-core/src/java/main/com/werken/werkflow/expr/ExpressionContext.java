package com.werken.werkflow.expr;

public interface ExpressionContext
{
    String[] getNames();
    Object getValue(String name);
}
