package com.werken.werkflow.semantics.java;

import com.werken.werkflow.definition.fundamental.AbstractActionTag;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.MissingAttributeException;

public class JavaActionTag
    extends AbstractActionTag
{
    private String className;

    public JavaActionTag()
    {

    }

    public void setClass(String className)
    {
        this.className = className;
    }

    public String getClassName()
    {
        return this.className;
    }

    public void doTag(XMLOutput output)
        throws Exception
    {
        ClassLoader cl = getContext().getClassLoader();

        if ( cl == null )
        {
            cl = Thread.currentThread().getContextClassLoader();
        }

        if ( cl == null )
        {
            cl = getClass().getClassLoader();
        }

        if ( this.className == null )
        {
            throw new MissingAttributeException( "class" );
        }

        Class beanClass = cl.loadClass( getClassName() );

        Object bean = beanClass.newInstance();

        JavaAction action = new JavaAction( bean,
                                            "execute" );

        setAction( action );
    }
}
