package com.werken.werkflow;

public interface AttributeDeclaration
{
    static final AttributeDeclaration[] EMPTY_ARRAY = new AttributeDeclaration[0];

    String getId();
    AttributeType getType();
    boolean isIn();
    boolean isOut();
}
