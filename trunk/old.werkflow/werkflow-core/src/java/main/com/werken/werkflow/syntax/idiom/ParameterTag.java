package com.werken.werkflow.syntax.idiom;

import com.werken.werkflow.definition.petri.IdiomDefinition;
import com.werken.werkflow.definition.petri.IdiomParameter;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class ParameterTag
    extends IdiomTagSupport
{
    private String id;
    private String type;
    private boolean required;
    
    public ParameterTag()
    {
        // intentionally left blank
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return this.type;
    }

    public void setRequired(boolean required)
    {
        this.required = required;
    }

    public boolean isRequired()
    {
        return this.required;
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        IdiomDefinition idiomDef = getCurrentIdiomDefinition();

        requireStringAttribute( "id",
                                getId() );

        requireStringAttribute( "type",
                                getType() );

        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        if ( cl == null )
        {
            cl = getContext().getClassLoader();

            if ( cl == null )
            {
                cl = getClass().getClassLoader();
            }
        }

        try
        {
            Class type = cl.loadClass( getType() );

            IdiomParameter param = new IdiomParameter( getId(),
                                                       type,
                                                       isRequired() );
        }
        catch (Exception e)
        {
            throw new JellyTagException( e );
        }
    }
}
