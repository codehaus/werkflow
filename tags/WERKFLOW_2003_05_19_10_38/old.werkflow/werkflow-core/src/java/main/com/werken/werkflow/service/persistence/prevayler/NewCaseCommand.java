/*
 * Created on Apr 7, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.werken.werkflow.service.persistence.prevayler;

import java.io.Serializable;
import java.util.HashMap;

import org.prevayler.util.TransactionWithQuery;

import com.werken.werkflow.Attributes;

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
            _attributes.put(name, (Serializable) attributes.getAttribute(name));
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
            throw new IllegalArgumentException("This command operates on a ProcessStore PrevalentSystem.");
        }
        
        ProcessStore store = (ProcessStore) prevalentSystem;
        return store.createCase(_packageId, _processId, _attributes);
    }

}
