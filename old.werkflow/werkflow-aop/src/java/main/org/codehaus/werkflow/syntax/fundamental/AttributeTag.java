package org.codehaus.werkflow.syntax.fundamental;

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

import org.codehaus.werkflow.AttributeType;
import org.codehaus.werkflow.SimpleAttributeDeclaration;
import org.codehaus.werkflow.definition.ProcessDefinition;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyTagException;

public class AttributeTag
    extends FundamentalTagSupport
    implements AttributeTypeReceptor
{
    private String id;
    private boolean isIn;
    private boolean isOut;
    private AttributeType attrType;

    public AttributeTag()
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

    public void setIn(boolean isIn)
    {
        this.isIn = isIn;
    }

    public boolean isIn()
    {
        return this.isIn;
    }

    public void setOut(boolean isOut)
    {
        this.isOut = isOut;
    }

    public boolean isOut()
    {
        return this.isOut;
    }

    public void receiveAttributeType(AttributeType attrType)
    {
        this.attrType = attrType;
    }

    /** @see org.apache.commons.jelly.Tag
     */
    public void doTag(XMLOutput output)
        throws JellyTagException
    {
        requireStringAttribute( "id",
                                getId() );

        pushObject( AttributeTypeReceptor.class,
                    this );

        invokeBody( output );

        popObject( AttributeTypeReceptor.class );

        if ( this.attrType == null )
        {
            throw new JellyTagException( "no type specified" );
        }

        ProcessDefinition processDef = getCurrentProcess();

        SimpleAttributeDeclaration attrDecl = new SimpleAttributeDeclaration( getId(),
                                                                              this.attrType,
                                                                              isIn(),
                                                                              isOut() );
        processDef.addAttributeDeclaration( attrDecl );
    }
}
