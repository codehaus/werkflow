package com.werken.werkflow.semantics.jexl;

import com.werken.werkflow.Attributes;

import org.apache.commons.jexl.JexlContext;

import java.util.Map;
import java.util.HashMap;

public class AttributesJexlContext
    implements JexlContext
{
    private Attributes attrs;

    public AttributesJexlContext(Attributes attrs)
    {
        this.attrs = attrs;
    }

    public Map getVars()
    {
        Map vars = new HashMap();

        String[] names = this.attrs.getAttributeNames();

        for ( int i = 0 ; i < names.length ; ++i )
        {
            vars.put( names[i],
                      attrs.getAttribute( names[i] ) );
        }

        return vars;
    }

    public void setVars(Map map)
    {
        // intentionally left blank
    }
}
