package org.codehaus.werkflow.service.persistence;

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

public class DefaultChangeSet
    implements ChangeSet
{
    private List modifiedCases;
    private List correlations;
    private List uncorrelateds;
    private List consumptions;

    public DefaultChangeSet()
    {
        this.modifiedCases = new ArrayList();
        this.correlations  = new ArrayList();
        this.uncorrelateds = new ArrayList();
        this.consumptions  = new ArrayList();
    }

    public void addModifiedCase(CaseTransfer modifiedCase)
    {
        this.modifiedCases.add( modifiedCase );
    }

    public void addCorrelation(CorrelationTransfer correlation)
    {
        this.correlations.add( correlation );
    }

    public void addUncorrelated(UncorrelatedTransfer uncorrelated)
    {
        this.uncorrelateds.add( uncorrelated );
    }

    public void addConsumption(ConsumptionTransfer consumption)
    {
        this.consumptions.add( consumption );
    }

    public CaseTransfer[] getModifiedCases()
    {
        return (CaseTransfer[]) this.modifiedCases.toArray( CaseTransfer.EMPTY_ARRAY );
    }

    public CorrelationTransfer[] getCorrelations()
    {
        return (CorrelationTransfer[]) this.correlations.toArray( CorrelationTransfer.EMPTY_ARRAY );
    }

    public UncorrelatedTransfer[] getUncorrelateds()
    {
        return (UncorrelatedTransfer[]) this.uncorrelateds.toArray( UncorrelatedTransfer.EMPTY_ARRAY );
    }

    public ConsumptionTransfer[] getConsumptions()
    {
        return (ConsumptionTransfer[]) this.consumptions.toArray( ConsumptionTransfer.EMPTY_ARRAY );
    }
}
