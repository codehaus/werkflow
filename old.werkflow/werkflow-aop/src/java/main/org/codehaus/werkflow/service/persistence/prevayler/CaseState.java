package org.codehaus.werkflow.service.persistence.prevayler;

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

import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
import EDU.oswego.cs.dl.util.concurrent.SyncSet;
import EDU.oswego.cs.dl.util.concurrent.WriterPreferenceReadWriteLock;
import org.codehaus.werkflow.service.persistence.CaseTransfer;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author kevin
 *
 * To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
class CaseState implements CaseTransfer
{
    CaseState( String packageId, String processId, String caseId, Map attributes ) throws ClassCastException
    {
        _packageId = packageId;
        _processId = processId;
        _id = caseId;
        _tokens = new SyncSet( new HashSet(), new WriterPreferenceReadWriteLock() );
        _attributes = new ConcurrentHashMap();

        setAttributes( attributes );
    }

    private final static String[] EMPTY_STRING_ARRAY = {
    };

    private String _packageId;
    private String _processId;
    private String _id;
    private Set _tokens;
    private Map _attributes;

    public String getPackageId()
    {
        return _packageId;
    }

    public String getProcessId()
    {
        return _processId;
    }

    /**
     * @see org.codehaus.werkflow.service.persistence.CaseTransfer#getCaseId()
     */
    public String getCaseId()
    {
        return _id;
    }

    /**
     * @see org.codehaus.werkflow.service.persistence.CaseTransfer#getAttributes()
     */
    public Map getAttributes()
    {
        return Collections.unmodifiableMap( _attributes );
    }

    /**
     * @see org.codehaus.werkflow.service.persistence.CaseTransfer#getTokens()
     */
    public String[] getTokens()
    {
        return (String[]) _tokens.toArray( EMPTY_STRING_ARRAY );
    }

    public void setTokens( String[] tokens )
    {
        _tokens.clear();
        for ( int i = 0; i < tokens.length; i++ )
        {
            _tokens.add( tokens[i] );
        }
    }

    void addToken( String token )
    {
        _tokens.add( token );
    }

    void setAttribute( String name, Serializable value )
    {
        _attributes.put( name, value );
    }

    Serializable getAttribute( String name )
    {
        return (Serializable) _attributes.get( name );
    }

    boolean hasAttribute( String name )
    {
        return _attributes.containsKey( name );
    }

    void setAttributes( Map attributes )
    {
        _attributes.clear();

        Iterator attributeIterator = attributes.entrySet().iterator();
        while ( attributeIterator.hasNext() )
        {
            Map.Entry entry = (Map.Entry) attributeIterator.next();
            _attributes.put( (String) entry.getKey(), (Serializable) entry.getValue() );
        }
    }
}
