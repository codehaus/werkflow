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

import java.io.Serializable;
import java.util.HashMap;

import org.prevayler.util.TransactionWithQuery;

import org.codehaus.werkflow.Attributes;

/**
 * @author kevin
 *
 * To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
class NewCaseCommand extends TransactionWithQuery
{
    NewCaseCommand(String packageId, String processId, Attributes attributes)
    {
        _packageId = packageId;
        _processId = processId;

        _attributes = new HashMap();
        String[] names = attributes.getAttributeNames();
        for (int i = 0; i < names.length; i++)
        {
            String name = names[i];
            _attributes.put( name, (Serializable) attributes.getAttribute( name ) );
        }
    }

    private String _packageId;
    private String _processId;
    private HashMap _attributes;

    /**
     * @see org.prevayler.util.TransactionWithQuery#executeAndQuery(java.lang.Object)
     */
    protected Object executeAndQuery(Object prevalentSystem) throws Exception
    {
        if (!(prevalentSystem instanceof ProcessStore))
        {
            throw new IllegalArgumentException( "This command operates on a ProcessStore PrevalentSystem." );
        }

        ProcessStore store = (ProcessStore) prevalentSystem;
        return store.createCase( _packageId, _processId, _attributes );
    }

}
