package org.codehaus.werkflow;

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

import java.util.Map;
import java.util.HashMap;

/** Simple <code>Attributes</code> implementation.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class SimpleAttributes
    implements MutableAttributes
{
    // ----------------------------------------------------------------------
    //     Constants
    // ----------------------------------------------------------------------

    /** Empty string array. */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Name/Value mappings. */
    private Map attrs;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     */
    public SimpleAttributes()
    {
        this.attrs = new HashMap();
    }

    /** Construct with initial attributes.
     *
     *  @param initialAttrs The initial attribute name/value pairs.
     */
    public SimpleAttributes(Map initialAttrs)
    {
        this.attrs = new HashMap( initialAttrs );
    }

    public SimpleAttributes(Attributes initialAttrs)
    {
        this.attrs = new HashMap();

        String[] attrNames = initialAttrs.getAttributeNames();

        for ( int i = 0 ; i < attrNames.length ; ++i )
        {
            this.attrs.put( attrNames[i],
                            initialAttrs.getAttribute( attrNames[i] ) );
        }
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Set an attribute value.
     *
     *  @param name The attribute name.
     *  @param value The attribute value.
     */
    public void setAttribute(String name,
                             Object value)
    {
        this.attrs.put( name,
                        value );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /** @see Attributes
     */
    public Object getAttribute(String name)
    {
        return this.attrs.get( name );
    }

    /** @see Attributes
     */
    public String[] getAttributeNames()
    {
        return (String[]) this.attrs.keySet().toArray( EMPTY_STRING_ARRAY );
    }

    /** @see Attributes
     */
    public boolean hasAttribute(String name)
    {
        return this.attrs.containsKey( name );
    }

    public String toString()
    {
        return "[SimpleAttributes: " + this.attrs + "]";
    }
}
