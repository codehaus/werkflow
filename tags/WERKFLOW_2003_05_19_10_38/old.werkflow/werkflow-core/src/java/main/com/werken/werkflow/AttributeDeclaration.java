package com.werken.werkflow;

import java.io.Serializable;

public interface AttributeDeclaration extends Serializable
{
    static final AttributeDeclaration[] EMPTY_ARRAY = new AttributeDeclaration[0];

    String getId();
    AttributeType getType();
    boolean isIn();
    boolean isOut();
}
