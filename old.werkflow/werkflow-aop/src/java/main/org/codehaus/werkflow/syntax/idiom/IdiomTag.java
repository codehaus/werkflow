package org.codehaus.werkflow.syntax.idiom;

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

import org.codehaus.werkflow.definition.petri.IdiomDefinition;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class IdiomTag
    extends IdiomTagSupport
{
    private String id;
    private String contains;

    public IdiomTag()
    {
        // intentionally left blank
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public void setContains(String contains)
    {
        this.contains = contains;
    }

    public String getContains()
    {
        return this.contains;
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        requireStringAttribute( "id",
                                getId() );

        IdiomsTag tag = (IdiomsTag) findAncestorWithClass( IdiomsTag.class );

        if ( tag == null )
        {
            throw new JellyTagException( "invalid context for <idiom>" );
        }

        short containsType;

        if ( this.contains == null )
        {
            containsType = IdiomDefinition.CONTAINS_MULTI_COMPONENTS;
        }
        else if ( "none".equals( this.contains ) )
        {
            containsType = IdiomDefinition.CONTAINS_NONE;
        }
        else if ( "action".equals( this.contains ) )
        {
            containsType = IdiomDefinition.CONTAINS_ONE_ACTION;
        }
        else if ( "component".equals( this.contains ) )
        {
            containsType = IdiomDefinition.CONTAINS_ONE_COMPONENT;
        }
        else if ( this.contains == null
                  ||
                  "components".equals( this.contains )
                  ||
                  "".equals( this.contains ) )
        {
            containsType = IdiomDefinition.CONTAINS_MULTI_COMPONENTS;
        }
        else
        {
            throw new JellyTagException( "attribute 'contains' may only accept: action, component, components" );
        }

        IdiomDefinition idiomDef = new IdiomDefinition( tag.getUri(),
                                                        getId(),
                                                        containsType );

        setCurrentIdiomDefinition( idiomDef );

        invokeBody( output );

        setCurrentIdiomDefinition( null );

        addToCollector( IdiomDefinition.class,
                        idiomDef );
    }
}
