package com.werken.werkflow.syntax.idiom;

/*
 $Id$

 Copyright 2003 (C) The Codehaus. All Rights Reserved.

 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.

 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.

 3. The name "werkflow" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Codehaus.  For written permission,
    please contact info@codehaus.org.

 4. Products derived from this Software may not be called "werkflow"
    nor may "werkflow" appear in their names without prior written
    permission of The Codehaus. "werkflow" is a registered
    trademark of The Codehaus.

 5. Due credit should be given to The Codehaus -
    http://werkflow.codehaus.org/

 THIS SOFTWARE IS PROVIDED BY THE CODEHAUS AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE CODEHAUS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */

import com.werken.werkflow.expr.Expression;
import com.werken.werkflow.expr.ExpressionFactory;
import com.werken.werkflow.definition.petri.Net;
import com.werken.werkflow.definition.petri.Idiom;
import com.werken.werkflow.definition.petri.IdiomDefinition;
import com.werken.werkflow.definition.petri.IdiomException;
import com.werken.werkflow.definition.petri.NoSuchParameterException;
import com.werken.werkflow.definition.petri.InvalidParameterTypeException;
import com.werken.werkflow.syntax.fundamental.ActionReceptor;
import com.werken.werkflow.syntax.fundamental.FundamentalTagSupport;
import com.werken.werkflow.work.Action;
import com.werken.werkflow.jelly.MiscTagSupport;

import org.apache.commons.jelly.DynaTagSupport;
import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.impl.StaticTag;

import java.util.Stack;

public class IdiomImplTag
    extends StaticTag
      // extends DynaTagSupport
    implements ActionReceptor
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
        try
        {
            if ( getIdiomDefinition().getParameter( id ).getType().equals( "expr" ) )
            {
                return Expression.class;
            }
        }
        catch (NoSuchParameterException e)
        {
            // swallow
        }

        return Object.class;
    }
    */

    public void setContext(JellyContext context)
        throws JellyTagException
    {
        super.setContext( context );

        Idiom current = currentIdiom();

        if ( current != null )
        {
            if ( current.getIdiomDefinition().getContainsType() == IdiomDefinition.CONTAINS_NONE
                 ||
                 current.getIdiomDefinition().getContainsType() == IdiomDefinition.CONTAINS_ONE_ACTION )
            {
                throw new JellyTagException( "not allowed within parent" );
            }
        }

        if ( current == null )
        {
            this.idiom = getIdiomDefinition().newIdiom();

            context.setVariable( ROOT_IDIOM_KEY,
                                 this.idiom );

            context.getParent().setVariable( Net.class.getName(),
                                             this.idiom );
        }
        else
        {
            this.idiom = current.addComponent( getIdiomDefinition() );
        }

        this.idiom.setScope( FundamentalTagSupport.getCurrentScope( getContext() ) );
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        try
        {
            this.idiom.build();

            pushIdiom( this.idiom );

            MiscTagSupport.pushObject( ActionReceptor.class,
                                       this,
                                       getContext() );

            invokeBody( output );

            MiscTagSupport.popObject( ActionReceptor.class,
                                      getContext() );

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
    {
        String factoryName = (String) getContext().findVariable( ExpressionFactory.class.getName() );

        if ( factoryName == null )
        {
            return null;
        }

        ExpressionFactory factory = (ExpressionFactory) getContext().findVariable( factoryName );

        return factory;
    }

    public void receiveAction(Action action)
        throws JellyTagException
    {
        if ( getIdiomDefinition().getContainsType() == IdiomDefinition.CONTAINS_NONE )
        {
            throw new JellyTagException( "action not allowed within this context" );
        }

        if ( getIdiomDefinition().getContainsType() == IdiomDefinition.CONTAINS_ONE_ACTION )
        {
            try
            {
                this.idiom.addAction( action );
            }
            catch (IdiomException e)
            {
                throw new JellyTagException( e );
            }
        }
        else
        {
            try
            {
                Idiom actionIdiom = this.idiom.addComponent( IdiomDefinition.ACTION_IDIOM );

                actionIdiom.build();
                actionIdiom.addAction( action );
                actionIdiom.complete();
            }
            catch (IdiomException e)
            {
                throw new JellyTagException( e );
            }
        }
    }

    public String getUri()
    {
        return getIdiomDefinition().getUri();
    }

    public String getLocalName()
    {
        return getIdiomDefinition().getId();
    }
}
