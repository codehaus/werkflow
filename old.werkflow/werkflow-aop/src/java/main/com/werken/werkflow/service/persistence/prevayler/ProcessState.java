/*
 * Created on Apr 6, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.werken.werkflow.service.persistence.prevayler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.werken.werkflow.AttributeDeclaration;

/**
 * @author kevin
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
class ProcessState implements Serializable
{
    public ProcessState(String packageId, String processId, AttributeDeclaration[] attributes)
    {
        _key = new ManagerKey(packageId, processId);
        _cases = new HashMap();
    }
    
    private int _id;
    private HashMap _cases;
    private ManagerKey _key;
    
    public ManagerKey key()
    {
        return _key;    
    }

    public boolean hasCase(String caseId)
    {
        return _cases.containsKey(caseId);
    }

    public CaseState loadCase(String caseId)
    {
        return (CaseState) _cases.get(caseId);
    }
    
    public CaseState addCase(Map attributes)
    {
        String id = String.valueOf(nextId());
        
        CaseState state = new CaseState(id, attributes);
        _cases.put(id, state);
        
        return state;
    }
    
    private synchronized int nextId()
    {
        return _id++;
    }
}
