package com.werken.werkflow.jelly;

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

import org.apache.commons.jelly.Tag;
import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;

import java.util.Collection;
import java.util.Stack;

/** Generally useful Jelly base <code>Tag</code>.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public abstract class MiscTagSupport
    extends TagSupport
{

    public static final String COLLECTOR_SUFFIX = ".collector";
    public static final String STACK_SUFFIX = ".stack";

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Construct.
     */
    public MiscTagSupport()
    {
        super( true );
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Indicate that a <code>String</code> attribute is required.
     *
     *  @param attrName The attribute name.
     *  @param attrValue The attribute value to test.
     *
     *  @throws MissingAttributeException If the value is either null
     *          or the empty string.
     */
    public void requireStringAttribute(String attrName,
                                       String attrValue)
        throws MissingAttributeException
    {
        requireObjectAttribute( attrName,
                                attrValue );

        if ( "".equals( attrValue ) )
        {
            throw new MissingAttributeException( attrName );
        }
    }

    /** Indicate that an <code>Object</code> attribute is required.
     *
     *  @param attrName The attribute name.
     *  @param attrValue The attribute value to test.
     *
     *  @throws MissingAttributeException If the value is null.
     */
    public void requireObjectAttribute(String attrName,
                                       Object attrValue)
        throws MissingAttributeException
    {
        if ( attrValue == null )
        {
            throw new MissingAttributeException( attrName );
        }
    }

    /** Indicate that a particular ancestor <code>Tag</code> is required.
     *
     *  @param tagName The tag name.
     *  @param tagClass The tag class.
     *
     *  @return The matching ancestor tag.
     *
     *  @throws JellyTagException If no matching ancestor exists.
     */
    public Tag requiredAncestor(String tagName,
                                Class tagClass)
        throws JellyTagException
    {
        Tag ancestor = findAncestorWithClass( tagClass );

        if ( ancestor == null )
        {
            throw new JellyTagException( tagName + " ancestor not found" );
        }

        return ancestor;
    }

    public void addToCollector(Class objClass,
                               Object obj)
    {
        Collection collector = getCollector( objClass );

        if ( collector != null )
        {
            collector.add( obj );
        }
    }

    public static String getCollectorName(Class objClass)
    {
        return objClass.getName() + COLLECTOR_SUFFIX;
    }

    public Collection getCollector(Class objClass)
    {
        return (Collection) getContext().getVariable( getCollectorName( objClass ) );
    }

    public void installCollector(Class objClass,
                                 Collection collector)
    {
        installCollector( objClass,
                          collector,
                          context );
    }

    public static void installCollector(Class objClass,
                                        Collection collector,
                                        JellyContext context)
    {
        context.setVariable( getCollectorName( objClass ),
                             collector );
    }

    public void pushObject(Class objClass,
                           Object obj)
    {
        pushObject( objClass,
                    obj,
                    getContext() );
    }

    public void popObject(Class objClass)
        throws JellyTagException
    {
        popObject( objClass,
                   getContext() );
    }

    public Object peekObject(Class objClass)
        throws JellyTagException
    {
        return peekObject( objClass,
                           getContext() );
    }

    public static void pushObject(Class objClass,
                                  Object obj,
                                  JellyContext context)
    {
        Stack stack = (Stack) context.getVariable( objClass.getName() + STACK_SUFFIX );

        if ( stack == null )
        {
            stack = new Stack();

            context.setVariable( objClass.getName() + STACK_SUFFIX,
                                 stack );
        }

        stack.push( obj );
    }

    public static void popObject(Class objClass,
                                 JellyContext context)
        throws JellyTagException
    {
        Stack stack = (Stack) context.getVariable( objClass.getName() + STACK_SUFFIX );

        if ( stack == null
             ||
             stack.isEmpty() )
        {
            throw new JellyTagException( "empty stack: " + objClass.getName() );
        }

        stack.pop();
    }

    public static Object peekObject(Class objClass,
                                    JellyContext context)
        throws JellyTagException
    {
        Stack stack = (Stack) context.getVariable( objClass.getName() + STACK_SUFFIX );

        if ( stack == null
             ||
             stack.isEmpty() )
        {
            throw new JellyTagException( "empty stack: " + objClass.getName() );
        }

        return stack.peek();
    }
}
