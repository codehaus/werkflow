package com.werken.werkflow.service.persistence.fleeting;

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
