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

import com.werken.werkflow.definition.ProcessDefinition;
import com.werken.werkflow.definition.petri.DefaultNet;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

import java.util.List;
import java.util.ArrayList;

/** Define a new process.
 *
 *  <p>
 *  The &lt;process&gt; tag is the top-most tag in the fundamental
 *  process definition syntax.  It aggregates the &lt;place&gt,
 *  &lt;transition&gt;, &lt;message-type&gt; and &lt;message-initiator&gt;
 *  tags.
 *  </p>
 *
 *  <p>
 *  An <code>id</code> attribute is required and a &lt;documentation&gt;
 *  element is optional.  It must contain at least 2 &lt;place&gt; tags
 *  with the identifiers of <b>in</b> and <b>out</b>.
 *  </p>
 *
 *  <p>
 *  <pre>
 *  &lt;process id="my.process"&gt;
 *    &lt;documentation&gt;
 *      My process does stuff.
 *    &lt;/documentation&gt;
 *
 *    &lt;place id="in"/&gt;
 *    &lt;place id="out"/&gt;
 *
 *    &lt;transition id="some.transition"&gt;
 *      ....
 *    &/lt;transition&gt;
 *  </pre>
 *  </p>
 *
 *  @see PlaceTag
 *  @see TransitionTag
 *  @see MessageTypeTag
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class ProcessTag
    extends FundamentalTagSupport
    implements DocumentableTag
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Process identifier. */
    private String id;

    /** Documentation, possibly null. */
    private String documentation;

    /** Petri-net structure. */
    private DefaultNet net;

    /** Initiation type. */
    private String initiation;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     */
    public ProcessTag()
    {
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Set the identifier.
     *
     *  @param id The identifier.
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /** Retrieve the identifier.
     *
     *  @return The identifier.
     */
    public String getId()
    {
        return this.id;
    }

    /** Retrieve the Petri-net structure.
     *
     *  @return The Petri net.
     */
    public DefaultNet getNet()
    {
        return this.net;
    }

    /** @see DocumentableTag
     */
    public void setDocumentation(String documentation)
    {
        this.documentation = documentation;
    }

    /** Retrieve the documentation.
     *
     *  @return The documentation.
     */
    public String getDocumentation()
    {
        return this.documentation;
    }

    public void setInitiation(String initiation)
    {
        this.initiation = initiation;
    }

    public String getInitiation()
    {
        return this.initiation;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** @see org.apache.commons.jelly.Tag
     */
    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        requireStringAttribute( "id",
                                getId() );

        requireStringAttribute( "initiation",
                                getInitiation() );

        ProcessDefinition.InitiationType initiationType = null;

        if ( getInitiation().equals( "message" ) )
        {
            initiationType = ProcessDefinition.InitiationType.MESSAGE;
        }
        else if ( getInitiation().equals( "call" ) )
        {
            initiationType = ProcessDefinition.InitiationType.CALL;
        }
        else
        {
            throw new JellyTagException( "initiation attribute must be 'message' or 'other'" );
        } 

        this.net = new DefaultNet();

        setDocumentation( null );

        invokeBody( output );

        ProcessDefinition def = new ProcessDefinition( getId(),
                                                       this.net,
                                                       initiationType );

        def.setDocumentation( getDocumentation() );

        List defList = (List) getContext().getVariable( FundamentalDefinitionLoader.FUNDAMENTAL_DEFINITION_LIST );

        defList.add( def );

        this.net = null;
    }
}
