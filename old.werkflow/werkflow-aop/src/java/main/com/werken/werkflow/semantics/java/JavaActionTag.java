package com.werken.werkflow.semantics.java;

import com.werken.werkflow.definition.fundamental.AbstractActionTag;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.MissingAttributeException;

public class JavaActionTag
    extends AbstractActionTag
{
    public static final String DEFAULT_METHOD_NAME = "execute";

    private String className;
    private String methodName;

    public JavaActionTag()
    {
        this.methodName = DEFAULT_METHOD_NAME;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public String getClassName()
    {
        return this.className;
    }

    public void setType(String className)
    {
        this.className = className;
    }

    public String getType()
    {
        return this.className;
    }

    public void setMethod(String method)
    {
        this.methodName = methodName;
    }

    public String getMethod()
    {
        return this.methodName;
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
            throw new MissingAttributeException( "className" );
        }

        Class beanClass = cl.loadClass( getClassName() );

        Object bean = beanClass.newInstance();

        JavaAction action = new JavaAction( bean,
                                            getMethod() );

        setAction( action );

        setMethod( DEFAULT_METHOD_NAME );
    }
}
