/*
 * Created on Apr 5, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.werken.werkflow.service.persistence.prevayler;

import org.prevayler.util.TransactionWithQuery;

import com.werken.werkflow.AttributeDeclaration;

/**
 * @author kevin
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ActivateManagerCommand extends TransactionWithQuery
{
    private AttributeDeclaration[] _attributes;
    private String _process;
    private String _package;

    ActivateManagerCommand(String packageId, String processId, AttributeDeclaration[] attributes)
    {
        _package = packageId;
        _process = processId;
        _attributes = attributes;
    }

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
      ProcessState state = store.activateManager(_package, _process, _attributes);
          
      return new PrevaylerProcessPersistenceManager(state);
    }
}
