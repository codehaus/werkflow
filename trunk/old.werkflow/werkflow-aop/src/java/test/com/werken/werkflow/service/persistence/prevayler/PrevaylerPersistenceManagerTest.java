/*
 * Created on Apr 5, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.werken.werkflow.service.persistence.prevayler;

import junit.framework.TestCase;

import com.werken.werkflow.AttributeDeclaration;
import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.admin.DeploymentException;
import com.werken.werkflow.service.persistence.PersistenceException;
import com.werken.werkflow.service.persistence.ProcessPersistenceManager;

/**
 * @author kevin
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PrevaylerPersistenceManagerTest extends TestCase
{

    /**
     * Constructor for PrevaylerPersistenceManagerTest.
     * @param name
     */
    public PrevaylerPersistenceManagerTest(String name)
    {
        super(name);
    }

    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(PrevaylerPersistenceManagerTest.class);
    }

    public void testActivatePassivate()
    {
        
        ProcessInfo info = createInfo("activate-passivate");        
        PrevaylerPersistenceManager manager =  new PrevaylerPersistenceManager();
        
        assertNotNull(manager);
        
        try
        {
            ProcessPersistenceManager processManager  = manager.activate(info);
            assertNotNull(processManager);
            manager.passivate(processManager);
            try
            {
                processManager.loadCase("test-case");
            }
            catch (PersistenceException pe)
            {
                // -- this is good :)
                
                return;
            }
            
            fail("Able to execute action on passive manager");
        }
        catch (DeploymentException de)
        {
            fail(de.getMessage());
        }
        catch (PersistenceException pe)
        {
            fail(pe.getMessage());
        }
    }

/*
    public void testAddAndGet()
    {
        ProcessInfo info = createInfo("and-n-get");        
        PrevaylerPersistenceManager manager =  new PrevaylerPersistenceManager();
        
        assertNotNull(manager);
        
        try
        {
            ProcessPersistenceManager processManager  = manager.activate(info);
            assertNotNull(processManager);
            
            processManager.newCase();
        }
        catch (DeploymentException de)
        {
            fail(de.getMessage());
        }
        catch (PersistenceException pe)
        {
            fail(pe.getMessage());
        }
    }
  */  
    
    private class SimpleInfo implements ProcessInfo
    {

        private String _packageId;
        private String _processId;

        SimpleInfo(String packageId, String processId)
        {
            _packageId = packageId;
            _processId = processId;
        }

        /**
         * @see com.werken.werkflow.ProcessInfo#getPackageId()
         */
        public String getPackageId()
        {
            return _packageId;
        }

        /**
         * @see com.werken.werkflow.ProcessInfo#getId()
         */
        public String getId()
        {
            return _processId;
        }

        /**
         * @see com.werken.werkflow.ProcessInfo#getDocumentation()
         */
        public String getDocumentation()
        {
            throw new RuntimeException("Unimplemented method");
        }

        /**
         * @see com.werken.werkflow.ProcessInfo#addAttributeDeclaration(com.werken.werkflow.AttributeDeclaration)
         */
        public void addAttributeDeclaration(AttributeDeclaration attrDecl)
        {
            throw new RuntimeException("Unimplemented method");
        }

        /**
         * @see com.werken.werkflow.ProcessInfo#getAttributeDeclarations()
         */
        public AttributeDeclaration[] getAttributeDeclarations()
        {
            return AttributeDeclaration.EMPTY_ARRAY;
        }

        /**
         * @see com.werken.werkflow.ProcessInfo#getAttributeDeclaration(java.lang.String)
         */
        public AttributeDeclaration getAttributeDeclaration(String id)
        {
            throw new RuntimeException("Unimplemented method");
        }
        
    }

    private ProcessInfo createInfo(String suffix)
    {
        return new SimpleInfo("test-package-" + suffix, "test-process-" + suffix);        
    }
}
