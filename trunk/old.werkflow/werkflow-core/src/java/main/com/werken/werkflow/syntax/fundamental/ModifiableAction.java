package com.werken.werkflow.syntax.fundamental;

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

import com.werken.werkflow.MutableAttributes;
import com.werken.werkflow.work.Action;
import com.werken.werkflow.work.ActionInvocation;

import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.XMLOutput;

import java.util.Map;
import java.util.Iterator;
import java.util.Collections;

/** Wrapper around any <code>Action</code> to allow modifications
 *  of the <code>otherAttributes</code>s paramater.
 *
 *  @see Action
 *
 *  @author <a href="mailto;bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class ModifiableAction
    implements Action
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Modification script. */
    private Script script;

    private Map vars;

    /** Wrapped action. */
    private Action action;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  @param script The other-attributes modification script.
     *  @param action The wrapped action.
     */
    public ModifiableAction(Script script,
                            Map vars,
                            Action action)
    {
        this.script = script;
        this.vars   = vars;
        this.action = action;
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Retrieve the other-attributes modification Jelly <code>Script</code>.
     *
     *  @return The modification script.
     */
    public Script getScript()
    {
        return this.script;
    }

    /** Retrieve the wrapped <code>Action</code>.
     *
     *  @return The wrapped action.
     */
    public Action getAction()
    {
        return this.action;
    }

    /** @see Action
     */
    public void perform(ActionInvocation invocation)
    {
        try
        {
            modifyAttributes( invocation );

            getAction().perform( invocation );
        }
        catch (Exception e)
        {
            invocation.completeWithError( e );
        }
    }

    /** Perform modification script.
     *
     *  @param invocation
     *
     *  @throws Exception If an error occurs while attempting
     *          to evaluate the modification script.
     */
    public void modifyAttributes(ActionInvocation invocation)
        throws Exception
    {

        MutableAttributes otherAttrs = invocation.getOtherAttributes();

        for ( Iterator keyIter = this.vars.keySet().iterator();
              keyIter.hasNext(); )
        {
            String key = (String) keyIter.next();

            if ( key.endsWith( "Attr" ) )
            {
                Object value = this.vars.get( key );

                key = key.substring( 0,
                                     key.length()-4 );

                otherAttrs.setAttribute( key,
                                         value );

                System.err.println( key + "--->" + value );
            }
        }

        System.err.println( "!!! modifying attributes" );
        OverlayJellyContext context = new OverlayJellyContext( invocation );

        script.run( context,
                    XMLOutput.createXMLOutput( System.err ) );

        System.err.println( "script: " + script );

        // XMLOutput.createDummyXMLOutput() );

    }

    public String toString()
    {
        return "[ModifiableAction: " + this.action + "]";
    }
}
