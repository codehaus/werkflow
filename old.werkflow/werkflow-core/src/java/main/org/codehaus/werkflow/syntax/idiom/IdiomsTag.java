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

import java.util.ArrayList;
import java.util.Collection;

public class IdiomsTag
    extends IdiomTagSupport
{
    private String uri;

    public IdiomsTag()
    {
        // intentionally left blank
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public String getUri()
    {
        return this.uri;
    }

    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        requireStringAttribute( "uri",
                                getUri() );

        Collection idiomDefs = getCollector( IdiomDefinition.class );

        if ( idiomDefs == null )
        {
            idiomDefs = new ArrayList();

            installCollector( IdiomDefinition.class,
                              idiomDefs );

        }

        invokeBody( output );

        IdiomDefinition[] defArray = (IdiomDefinition[]) idiomDefs.toArray( IdiomDefinition.EMPTY_ARRAY );

        IdiomaticTagLibrary tagLib = new IdiomaticTagLibrary( defArray );

        getContext().registerTagLibrary( getUri(),
                                         tagLib );
    }
}
