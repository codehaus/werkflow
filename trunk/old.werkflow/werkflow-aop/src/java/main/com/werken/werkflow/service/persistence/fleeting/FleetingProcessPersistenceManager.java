package com.werken.werkflow.service.persistence.fleeting;

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

import com.werken.werkflow.Attributes;
import com.werken.werkflow.service.persistence.ChangeSet;
import com.werken.werkflow.service.persistence.CaseTransfer;
import com.werken.werkflow.service.persistence.DefaultCaseTransfer;
import com.werken.werkflow.service.persistence.CorrelationTransfer;
import com.werken.werkflow.service.persistence.ProcessPersistenceManager;
import com.werken.werkflow.service.persistence.PersistenceException;

import java.util.Map;
import java.util.HashMap;

public class FleetingProcessPersistenceManager
    implements ProcessPersistenceManager
{
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private int counter;

    private String packageId;
    private String processId;

    public FleetingProcessPersistenceManager(String packageId,
                                             String processId)
    {
        this.packageId = packageId;
        this.processId = processId;
    }

    public void persist(ChangeSet changeSet)
        throws PersistenceException
    {
        System.err.println( "persist: " + changeSet );
    }

    public CaseTransfer newCase(Attributes initialAttrs)
        throws PersistenceException
    {
        System.err.println( "newCase: " + initialAttrs );
        String[] attrNames = initialAttrs.getAttributeNames();

        Map attrsMap = new HashMap();

        for ( int i = 0 ; i < attrNames.length ; ++i )
        {
            attrsMap.put( attrNames[i],
                          initialAttrs.getAttribute( attrNames[i] ) );
        }

        DefaultCaseTransfer caseTransfer = new DefaultCaseTransfer( "case." + (++counter),
                                                                    attrsMap,
                                                                    EMPTY_STRING_ARRAY );

        return caseTransfer;
    }

    public CaseTransfer loadCase(String caseId)
        throws PersistenceException
    {
        System.err.println( "loadCase: " + caseId );
        return null;
    }

    public boolean hasCase(String caseId)
    {
        System.err.println( "hasCase: " + caseId );
        return false;
    }

    public CorrelationTransfer[] getCorrelations()
        throws PersistenceException
    {
        return CorrelationTransfer.EMPTY_ARRAY;
    }
}
