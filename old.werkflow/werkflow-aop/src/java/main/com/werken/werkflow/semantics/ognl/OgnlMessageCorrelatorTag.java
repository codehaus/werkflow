package com.werken.werkflow.semantics.ognl;

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

import com.werken.werkflow.expr.Expression;
import com.werken.werkflow.expr.ExpressionMessageCorrelator;
import com.werken.werkflow.syntax.fundamental.AbstractMessageCorrelatorTag;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.JellyTagException;

public class OgnlMessageCorrelatorTag
    extends AbstractMessageCorrelatorTag
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Correelation test expression. */
    private String test;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     */
    public OgnlMessageCorrelatorTag()
    {
        // intentionally left blank
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    public void setTest(String test)
    {
        this.test = test;
    }

    public String getTest()
    {
        return this.test;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** @see org.apache.commons.jelly.Tag
     */
    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        requireStringAttribute( "test",
                                getTest() );

        try
        {
            Expression expr = OgnlExpressionFactory.getInstance().newExpression( getTest() );
            
            ExpressionMessageCorrelator correlator = new ExpressionMessageCorrelator( getMessageId(),
                                                                                      expr );
            
            setMessageCorrelator( correlator );
        }
        catch (Exception e)
        {
            throw new JellyTagException( e );
        }
    }
}
