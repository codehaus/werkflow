package com.werken.werkflow.semantics.java;

import com.werken.werkflow.definition.fundamental.AbstractMessageSelectorTag;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.expression.Expression;

public class ClassMessageSelectorTag
    extends AbstractMessageSelectorTag
{
    private String className;
    private Expression expression;

    public ClassMessageSelectorTag()
    {
    }

    public void setType(String className)
    {
        this.className = className;
    }

    public String getType()
    {
        return this.className;
    }

    public void setFilter(Expression expression)
    {
        this.expression = expression;
    }

    public Expression getFilter()
    {
        return this.expression;
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        if ( this.className == null
             ||
             "".equals( this.className ) )
        {
            throw new MissingAttributeException( "class" );
        }

        try
        {
            Class messageClass = Class.forName( this.className );
            
            ClassMessageSelector selector = new ClassMessageSelector( messageClass,
                                                                      getFilter() );
            
            
            setMessageSelector( selector );
        }
        catch (Exception e)
        {
            throw new JellyTagException( e );
        }
    }
}
