package com.werken.werkflow.syntax.fundamental;

/*
 $Id$

 Copyright 2003 (C) The Werken Company. All Rights Reserved.
 
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
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.
 
 4. Products derived from this Software may not be called "werkflow"
    nor may "werkflow" appear in their names without prior written
    permission of The Werken Company. "werkflow" is a registered
    trademark of The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://werkflow.werken.com/).
 
 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.
 
 */

import com.werken.werkflow.definition.Scope;
import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.definition.MessageTypeLibrary;
import com.werken.werkflow.expr.Expression;
import com.werken.werkflow.expr.ExpressionFactory;
import com.werken.werkflow.jelly.MiscTagSupport;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.JellyTagException;

/** Support for fundamental syntax tags.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public abstract class FundamentalTagSupport
    extends MiscTagSupport
{
    // ----------------------------------------------------------------------
    //     Constants
    // ----------------------------------------------------------------------

    private static final String CURRENT_PROCESS_KEY = "werkflow.current.process";

    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Current <code>ProcessDefinition</code>.
     */
    private ProcessDefinition processDef;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     */
    public FundamentalTagSupport()
    {
        // intentionally left blank
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    protected Scope getCurrentScope()
    {
        return getCurrentScope( getContext() );
    }

    public static Scope getCurrentScope(JellyContext context)
    {
        return (Scope) context.getVariable( Scope.class.getName() );
    }

    protected void setCurrentScope(Scope scope)
    {
        getContext().setVariable( Scope.class.getName(),
                                  scope );
    }

    protected void pushScope()
    {
        Scope curScope = getCurrentScope();

        if ( curScope == null )
        {
            setCurrentScope( new Scope() );
        }
        else
        {
            setCurrentScope( new Scope( curScope ) );
        }
    }

    protected void popScope()
    {
        Scope curScope = getCurrentScope();

        setCurrentScope( curScope.getParent() );
    }

    protected void addProcessDefinition(ProcessDefinition processDef)
        throws JellyTagException
    {
        getCollector( ProcessDefinition.class ).add( processDef );
    }

    protected ProcessDefinition getCurrentProcess()
    {
        return (ProcessDefinition) getContext().getVariable( CURRENT_PROCESS_KEY );
    }

    protected void setCurrentProcess(ProcessDefinition processDef)
    {
        getContext().setVariable( CURRENT_PROCESS_KEY,
                                  processDef );
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

    protected Expression newExpression(String text)
        throws Exception
    {
        return getExpressionFactory().newExpression( text );
    }
}

