package com.werken.werkflow;

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

/** Name/value pairs for initiating process cases.
 *
 *  @see WfmsRuntime#newProcessCase
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id; $
 */
public interface Attributes
{
    static final Attributes EMPTY_ATTRIBUTES = new Attributes()
        {
            public Object getAttribute(String name)
            {
                return null;
            }
            
            public String[] getAttributeNames()
            {
                return new String[0];
            }

            public boolean hasAttribute(String name)
            {
                return false;
            }
        };

    /** Retrieve an attribute by name.
     *
     *  @param name The attribute name.
     *
     *  @return The attribute value or <code>null</code> if no
     *          value has been set for the attribute.
     */
    Object getAttribute(String name);

    /** Retrieve all attribute names.
     *
     *  @return An array of attribute names.
     */
    String[] getAttributeNames();

    /** Determine if an attribute has been assigned a value.
     *
     *  @param name The attribute name.
     *
     *  @return <code>true</code> if the attribute has been assigned
     *          a value (including <code>null</code>), otherwise
     *          <code>false</code>.
     */
    boolean hasAttribute(String name);
}