package com.werken.werkflow.syntax.idiom;

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

import com.werken.werkflow.definition.petri.IdiomDefinition;
import com.werken.werkflow.definition.petri.IdiomDefinitionLibrary;

import org.apache.commons.jelly.Tag;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.expression.ExpressionFactory;
import org.apache.commons.jelly.impl.DynamicTagLibrary;

import org.xml.sax.Attributes;

public class IdiomaticTagLibrary
    extends DynamicTagLibrary
{
    public static final String NAMESPACE_URI = "werkflow:idiomatic";

    private IdiomDefinition[] idiomDefs;
    private ExpressionFactory exprFactory;

    public IdiomaticTagLibrary(IdiomDefinitionLibrary idiomDefLib)
    {
        this( idiomDefLib.getIdiomDefinitions() );
    }

    public IdiomaticTagLibrary(IdiomDefinition[] idiomDefs)
    {
        this.idiomDefs = idiomDefs;

        registerIdioms();
    }
    public Tag createTag(String name,
                         Attributes attributes)
        throws JellyException
    {
        return super.createTag( name,
                                attributes );
    }
    private void registerIdioms()
    {
        for ( int i = 0 ; i < this.idiomDefs.length ; ++i )
        {
            registerIdiom( this.idiomDefs[i] );
        }
    }

    protected void registerIdiom(IdiomDefinition idiomDef)
    {
        registerBeanTag( idiomDef.getId(),
                         new IdiomTagFactory( idiomDef ) );
    }

    public void setExpressionFactory(ExpressionFactory exprFactory)
    {
        this.exprFactory = exprFactory;
    }

    public ExpressionFactory getExpressionFactory()
    {
        return this.exprFactory;
    }
}
