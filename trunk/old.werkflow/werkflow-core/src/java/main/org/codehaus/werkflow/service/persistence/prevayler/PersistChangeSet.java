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

import org.codehaus.werkflow.service.persistence.CaseTransfer;
import org.codehaus.werkflow.service.persistence.ChangeSet;
import org.codehaus.werkflow.service.persistence.PersistenceException;
import org.prevayler.Transaction;

/**
 * Created by IntelliJ IDEA.
 * User: kevin
 * Date: 8/09/2003
 * Time: 13:38:48
 * To change this template use Options | File Templates.
 */
public class PersistChangeSet implements Transaction
{
    public PersistChangeSet( ChangeSet changeSet )
    {
        _changeSet = new SerializableChangeSet( changeSet );
    }

    private ChangeSet _changeSet;

    public void executeOn( Object object )
    {
        if ( !( object instanceof ProcessStore ) )
        {
            throw new IllegalArgumentException( "This command operates on a ProcessStore PrevalentSystem." );
        }

        ProcessStore store = (ProcessStore) object;

        CaseTransfer[] transfers = _changeSet.getModifiedCases();
        for ( int i = 0; i < transfers.length; i++ )
        {
            CaseTransfer transfer = transfers[i];
            transfer.getCaseId();

            try
            {
                CaseState state = store.fetchCase( transfer.getPackageId(), transfer.getProcessId(), transfer.getCaseId() );
                state.setTokens( transfer.getTokens() );
                state.setAttributes( transfer.getAttributes() );
            }
            catch ( PersistenceException e )
            {
                e.printStackTrace();
            }
        }

    }

    private static class SerializableChangeSet implements ChangeSet
    {

        public SerializableChangeSet( ChangeSet changeset )
        {
            _transfers = changeset.getModifiedCases();
        }

        CaseTransfer[] _transfers;

        public CaseTransfer[] getModifiedCases()
        {
            return _transfers;
        }

    }
}
