/*
 * Created on Apr 5, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.werken.werkflow.service.persistence.prevayler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.werken.werkflow.AttributeDeclaration;
import com.werken.werkflow.service.persistence.PersistenceException;

/**
 * 
 * <p>
 * This class relies on the surrounding code to provide synchronisation.
 * </p>
 */
class ProcessStore implements Serializable
{
    public ProcessStore()
    {
        _activeProcesses  = new HashMap();
        _passiveProcesses  = new HashMap();
    }
    
    private HashMap _activeProcesses;
    private HashMap _passiveProcesses;

    public ProcessState activateManager(String packageId, String processId, AttributeDeclaration[] attributes) throws PersistenceException
    {
        ManagerKey key = new ManagerKey(packageId, processId);
        
        if (_activeProcesses.containsKey(key))
        {
            throw new PersistenceException("Manager (" + key + ") is already active");   
        }
    
        ProcessState result = null;
        if (_passiveProcesses.containsKey(key))
        {
            result = (ProcessState) _passiveProcesses.remove(key);
        }
        else
        {
            result = new ProcessState(packageId, processId, attributes);
        }
    
        _activeProcesses.put(key, result);
        
        return result;
    }
        
    public int activeManagerCount()
    {
        return _activeProcesses.size();    
    }

    public void passivateManager(String packageId, String processId) throws PersistenceException
    {
        ManagerKey key = new ManagerKey(packageId, processId);

        if (! _activeProcesses.containsKey(key))
        {
            throw new PersistenceException("Manager (" + key + ") is not active");   
        }
        
        ProcessState state = (ProcessState) _activeProcesses.remove(key);
        _passiveProcesses.put(key, state);
    }
    
    public CaseState createCase(String packageId, String processId, Map attributes) throws PersistenceException
    {
        ManagerKey key = new ManagerKey(packageId, processId);

        
        ProcessState processState = (ProcessState) _activeProcesses.get(key);         
        
        if (null == processState)
        {
            throw new PersistenceException("Manager (" + key + ") is not active");   
        }
        
        return processState.addCase(attributes);
    }
}
        

