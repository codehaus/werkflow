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

public class ArcDefinition
{
    public static final ArcDefinition[] EMPTY_ARRAY = new ArcDefinition[0];

    public static final short PLACE_TO_TRANSITION_TYPE = 1;
    public static final short TRANSITION_TO_PLACE_TYPE = 2;

    private short type;
    private String placeId;
    private String transitionId;
    private String expression;

    protected ArcDefinition(short type,
                            String placeId,
                            String transitionId,
                            String expression)
    {
        this.type         = type;
        this.placeId      = placeId;
        this.transitionId = transitionId;
        this.expression   = expression;
    }

    public short getType()
    {
        return this.type;
    }

    public String getPlaceId()
    {
        return this.placeId;
    }

    public String getTransitionId()
    {
        return this.transitionId;
    }

    public String getExpression()
    {
        return this.expression;
    }

    public boolean equals(Object thatObj)
    {
        if ( thatObj instanceof ArcDefinition )
        {
            ArcDefinition that = (ArcDefinition) thatObj;

            return ( getType() == that.getType()
                     &&
                     getPlaceId().equals( that.getPlaceId() )
                     &&
                     getTransitionId().equals( that.getTransitionId() ) );
        }

        return false;
    }

    public int hashCode()
    {
        return ( getPlaceId().hashCode() / 2 ) + ( getTransitionId().hashCode() / 2 ) + getType();
    }

    public static ArcDefinition newArcFromPlaceToTransition(String placeId,
                                                            String transitionId,
                                                            String expression)
    {
        return new ArcDefinition( PLACE_TO_TRANSITION_TYPE,
                                  placeId,
                                  transitionId,
                                  expression );

    }

    public static ArcDefinition newArcFromTransitionToPlace(String transitionId,
                                                            String placeId,
                                                            String expression)
    {
        return new ArcDefinition( TRANSITION_TO_PLACE_TYPE,
                                  placeId,
                                  transitionId,
                                  expression );
    }

    public String toString()
    {
        if ( this.type == TRANSITION_TO_PLACE_TYPE )
        {
            return "[ArcDefinition: transition=" + this.transitionId + " place=" + this.placeId + "]";
        }
        else
        {
            return "[ArcDefinition: place=" + this.placeId + " transition=" + this.transitionId + "]";
        }
    }
}
