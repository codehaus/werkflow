package com.werken.werkflow.syntax.idiomatic;

import com.werken.werkflow.definition.petri.Idiom;
import com.werken.werkflow.definition.petri.IdiomDefinition;
import com.werken.werkflow.definition.petri.IdiomException;
import com.werken.werkflow.definition.petri.NoSuchParameterException;
import com.werken.werkflow.definition.petri.InvalidParameterTypeException;

import org.apache.commons.jelly.DynaTagSupport;
import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

import java.util.Stack;

public class IdiomTag
    extends DynaTagSupport
{
    public static final String ROOT_IDIOM_KEY = "werkflow.root.idiom";

    private static final String IDIOM_STACK_KEY = "werkflow.idiom.stack";

    private IdiomDefinition idiomDef;
    private Idiom idiom;

    public IdiomTag(IdiomDefinition idiomDef)
    {
        this.idiomDef = idiomDef;
    }

    public IdiomDefinition getIdiomDefinition()
    {
        return this.idiomDef;
    }

    public void setAttribute(String id,
                             Object value)
        throws JellyTagException
    {
        try
        {
            this.idiom.setParameter( id,
                                     value );
        }
        catch (NoSuchParameterException e)
        {
            throw new JellyTagException( "no such parameter: " + id );
        }
        catch (InvalidParameterTypeException e)
        {
            throw new JellyTagException( "invalid type for parameter: " + id );
        }
    }

    public Class getAttributeType(String id)
        throws JellyTagException
    {
        try
        {
            return getIdiomDefinition().getParameter( id ).getType();
        }
        catch (NoSuchParameterException e)
        {
            // swallow
        }

        return Object.class;
    }

    public void setContext(JellyContext context)
        throws JellyTagException
    {
        super.setContext( context );

        Idiom current = currentIdiom();

        if ( current == null )
        {
            this.idiom = getIdiomDefinition().newIdiom();

            context.setVariable( ROOT_IDIOM_KEY,
                                 this.idiom );
        }
        else
        {
            this.idiom = current.addComponent( getIdiomDefinition() );
        }
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        try
        {
            this.idiom.build();
            
            pushIdiom( this.idiom );
            
            invokeBody( output );
            
            popIdiom();
        }
        catch (IdiomException e)
        {
            throw new JellyTagException( e );
        }
    }

    protected void pushIdiom(Idiom idiom)
    {
        Stack stack = (Stack) getContext().getVariable( IDIOM_STACK_KEY );

        if ( stack == null )
        {
            stack = new Stack();
            getContext().setVariable( IDIOM_STACK_KEY,
                                      stack );
        }

        stack.push( idiom );
    }

    protected void popIdiom()
    {
        Stack stack = (Stack) getContext().getVariable( IDIOM_STACK_KEY );

        if ( stack == null
             ||
             stack.isEmpty() )
        {
            return;
        }

        stack.pop();
    }

    protected Idiom currentIdiom()
    {
        Stack stack = (Stack) getContext().getVariable( IDIOM_STACK_KEY );

        if ( stack == null
             ||
             stack.isEmpty() )
        {
            return null;
        }

        return (Idiom) stack.peek();
    }
}
