package com.werken.werkflow.semantics.java;

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
import com.werken.werkflow.syntax.fundamental.AbstractMessageSelectorTag;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;

/** Jelly <code>Tag</code> for <code>ClassMessageSelector</code>.
 *
 *  @see ClassMessageSelector
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class ClassMessageSelectorTag
    extends AbstractMessageSelectorTag
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Message class name. */
    private String className;

    /** Filtering expression. */
    private String expression;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     */
    public ClassMessageSelectorTag()
    {
        // intentionally left blank
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Set the message type class.
     *
     *  @param className The message-type class.
     */
    public void setType(String className)
    {
        this.className = className;
    }

    /** Retrieve the message type class.
     *
     *  @return The message-type class.
     */
    public String getType()
    {
        return this.className;
    }

    /** Set the filtering expression.
     *
     *  @param expression The filtering expression.
     */
    public void setFilter(String expression)
    {
        this.expression = expression;
    }

    /** Retrieve the filtering expression.
     *
     *  @return The filtering expression, or <code>null</code> if none.
     */
    public String getFilter()
    {
        return this.expression;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /** @see org.apache.commons.jelly.Tag
     */
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

            Expression expr = JavaExpressionFactory.getInstance().newExpression( getFilter() );

            ClassMessageSelector selector = new ClassMessageSelector( messageClass,
                                                                      expr );

            setMessageSelector( selector );
        }
        catch (Exception e)
        {
            throw new JellyTagException( e );
        }
    }
}
