package org.codehaus.werkflow.syntax.petri;

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

import org.codehaus.werkflow.definition.petri.DefaultNet;
import org.codehaus.werkflow.definition.petri.DefaultPlace;
import org.codehaus.werkflow.definition.petri.DuplicateIdException;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

/** Define a <code>Place</code>.
 *
 *  <p>
 *  Within a &lt;process&gt; &lt;place&gt;s can be defined for
 *  use as input sources and output sinks for transitions.  It
 *  may contain an optional &lt;documentation&gt; element.
 *  </p>
 *
 *  <p>
 *  <pre>
 *  &lt;process id="my.process"&gt;
 *    &lt;place id="place.one"/&gt;
 *    &lt;place id="place.two"&gt;
 *      &lt;documentation&gt;
 *        This is the docs.
 *      &lt;/documentation&gt;
 *    &lt;place&gt;
 *
 *  @see ProcessTag
 *  @see TransitionTag
 *  @see InputTag
 *  @see OutputTag
 *  @see DocumentationTag
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class PlaceTag
    extends PetriTagSupport
      // implements DocumentableTag
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Unique identifier. */
    private String id;

    /** Documentation, possibly null. */
    private String documentation;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     */
    public PlaceTag()
    {
        // intentionally left blank
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

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /** @see org.apache.commons.jelly.Tag
     */
    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        DefaultNet net = getCurrentNet();

        requireStringAttribute( "id",
                                getId() );

        DefaultPlace place = null;

        try
        {
            place = net.addPlace( getId() );
        }
        catch (DuplicateIdException e)
        {
            throw new JellyTagException( e );
        }

        setDocumentation( null );

        invokeBody( output );

        place.setDocumentation( getDocumentation() );
    }
}
