package com.werken.werkflow.semantics.java;

import com.werken.werkflow.definition.fundamental.AbstractMessageSelectorTag;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.MissingAttributeException;

public class ClassMessageSelectorTag
    extends AbstractMessageSelectorTag
{
    private String className;

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

    public void doTag(XMLOutput output)
        throws Exception
    {
        if ( this.className == null
             ||
             "".equals( this.className ) )
        {
            throw new MissingAttributeException( "class" );
        }

        Class messageClass = Class.forName( this.className );

        ClassMessageSelector selector = new ClassMessageSelector( messageClass );

        setMessageSelector( selector );
    }
}
