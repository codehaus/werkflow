package com.werken.werkflow.syntax.idiom;

import com.werken.werkflow.expr.Expression;
import com.werken.werkflow.expr.ExpressionFactory;
import com.werken.werkflow.definition.petri.Net;
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

public class IdiomImplTag
    extends DynaTagSupport
{
    public static final String ROOT_IDIOM_KEY = "werkflow.root.idiom";

    private static final String IDIOM_STACK_KEY = "werkflow.idiom.stack";

    private IdiomDefinition idiomDef;
    private Idiom idiom;

    public IdiomImplTag(IdiomDefinition idiomDef)
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
        System.err.println( "id: " + id + " > " + value );

        try
        {
            Object finalValue = null;

            if ( "expr".equals( getIdiomDefinition().getParameter( id ).getType() ) )
            {
                try
                {
                    if ( value != null )
                    {
                        ExpressionFactory exprFactory = getExpressionFactory();
                        
                        finalValue = exprFactory.newExpression( value.toString() );
                    }
                }
                catch (Exception  e)
                {
                    throw new JellyTagException( e );
                }
            }
            else
            {
                finalValue = value;
            }

            this.idiom.setParameter( id,
                                     finalValue );
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

    /*
    public Class getAttributeType(String id)
        throws JellyTagException
    {
        // System.err.println( "GET ATTR TYPE(" + id + ")" );
        try
        {
            if ( getIdiomDefinition().getParameter( id ).getType().equals( "expr" ) )
            {
                System.err.println( id + " is an EXPR" );
                return Expression.class;
            }
        }
        catch (NoSuchParameterException e)
        {
            // swallow
        }

        System.err.println( id + " is an OBJ" );
        return Object.class;
    }
    */

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

            // System.err.println( "setting net: " + this.idiom + " on " + getContext() );
            // System.err.println( "parent: " + getContext().getParent() );
            // System.err.println( "gparent: " + getContext().getParent().getParent() );

            context.getParent().setVariable( Net.class.getName(),
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

            this.idiom.complete();
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

    protected ExpressionFactory getExpressionFactory()
        throws Exception
    {
        // return (ExpressionFactory) getContext().getVariable( ExpressionFactory.class.getName() );

        String factoryName = (String) getContext().findVariable( ExpressionFactory.class.getName() );

        if ( factoryName == null )
        {
            return null;
        }

        System.err.println( "FACTORY GET: " + getContext() + " -- " + factoryName );

        ExpressionFactory factory = (ExpressionFactory) getContext().findVariable( factoryName );

        System.err.println( "FACTORY itself: " + getContext() + " -- " + factory );

        return factory;
    }
}
