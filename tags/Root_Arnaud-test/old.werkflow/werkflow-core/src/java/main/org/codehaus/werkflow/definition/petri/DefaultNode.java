package org.codehaus.werkflow.definition.petri;

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

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;

/** Default base for <code>Place</code>s and <code>Transition</code>s.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class DefaultNode
    extends DefaultElement
    implements Node
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Node identifier. */
    private String id;

    /** Arcs towards this node. */
    private List inboundArcs;

    /** Arcs from this node. */
    private List outboundArcs;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  @param id The identifier.
     */
    public DefaultNode(String id)
    {
        this.id = id;
        this.inboundArcs  = new ArrayList();
        this.outboundArcs = new ArrayList();
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** @see Node
     */
    public String getId()
    {
        return this.id;
    }

    /** Add an inbound <code>Arc</code>.
     *
     *  @param arc The arc.
     */
    void addInboundArc(Arc arc)
    {
        this.inboundArcs.add( arc );
    }

    /** Return all inbound <code>Arc</code>s.
     *
     *  @return The inbound arcs.
     */
    protected Arc[] getInboundArcs()
    {
        return (Arc[]) this.inboundArcs.toArray( Arc.EMPTY_ARRAY );
    }

    void removeInboundArc(Arc arc)
    {
        while ( this.inboundArcs.remove( arc ) )
        {
            // intentionally left blank
        }
    }

    void replaceInboundArc(Arc oldArc,
                           Arc newArc)
    {
        ListIterator arcIter = this.inboundArcs.listIterator();

        while ( arcIter.hasNext() )
        {
            if ( arcIter.next().equals( oldArc ) )
            {
                arcIter.set( newArc );
            }
        }
    }

    /** Add an outbound <code>Arc</code>.
     *
     *  @param arc The arc.
     */
    void addOutboundArc(Arc arc)
    {
        this.outboundArcs.add( arc );
    }

    void removeOutboundArc(Arc arc)
    {
        while( this.outboundArcs.remove( arc ) )
        {
            // intentionally left blank
        }
    }

    void replaceOutboundArc(Arc oldArc,
                            Arc newArc)
    {
        ListIterator arcIter = this.outboundArcs.listIterator();

        while ( arcIter.hasNext() )
        {
            if ( arcIter.next().equals( oldArc ) )
            {
                arcIter.set( newArc );
            }
        }
    }

    /** Return all outbound <code>Arc</code>s.
     *
     *  @return The outbound arcs.
     */
    protected Arc[] getOutboundArcs()
    {
        return (Arc[]) this.outboundArcs.toArray( Arc.EMPTY_ARRAY );
    }
}

