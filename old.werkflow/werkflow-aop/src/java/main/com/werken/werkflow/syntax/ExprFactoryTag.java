package com.werken.werkflow.syntax;

import com.werken.werkflow.expr.ExpressionFactory;
import com.werken.werkflow.jelly.MiscTagSupport;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class ExprFactoryTag
    extends MiscTagSupport
{
    private String type;

    public ExprFactoryTag()
    {

    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return this.type;
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        requireStringAttribute( "type",
                                getType() );

        ExpressionFactory factory = (ExpressionFactory) getContext().getVariable( getType() );

        if ( factory == null )
        {
            try
            {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();

                if ( cl == null )
                {
                    cl = getClass().getClassLoader();
                }

                Class factoryClass = cl.loadClass( getType() );

                factory = (ExpressionFactory) factoryClass.newInstance();

                getContext().setVariable( getType(),
                                          factory );
            }
            catch (Exception e)
            {
                throw new JellyTagException( e );
            }
        }

        getContext().setVariable( ExpressionFactory.class.getName(),
                                  getType() );
    }
}
