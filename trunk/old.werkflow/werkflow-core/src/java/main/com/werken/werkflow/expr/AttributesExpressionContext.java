package com.werken.werkflow.expr;

import com.werken.werkflow.Attributes;

public class AttributesExpressionContext
    extends SimpleExpressionContext
{
    public AttributesExpressionContext(Attributes attrs)
    {
        String[] attrNames = attrs.getAttributeNames();

        for ( int i = 0 ; i < attrNames.length ; ++i )
        {
            setValue( attrNames[i],
                      attrs.getAttribute( attrNames[i] ) );
        }
    }
}
