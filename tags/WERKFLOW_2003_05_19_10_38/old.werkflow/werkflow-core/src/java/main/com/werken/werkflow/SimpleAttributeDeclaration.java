package com.werken.werkflow;

public class SimpleAttributeDeclaration
    implements AttributeDeclaration
{
    private String id;
    private AttributeType type;
    private boolean isIn;
    private boolean isOut;

    public SimpleAttributeDeclaration(String id,
                                      AttributeType type,
                                      boolean isIn,
                                      boolean isOut)
    {
        this.id    = id;
        this.type  = type;
        this.isIn  = isIn;
        this.isOut = isOut;
    }

    public String getId()
    {
        return this.id;
    }

    public AttributeType getType()
    {
        return this.type;
    }

    public boolean isIn()
    {
        return this.isIn;
    }

    public boolean isOut()
    {
        return this.isOut;
    }
}
