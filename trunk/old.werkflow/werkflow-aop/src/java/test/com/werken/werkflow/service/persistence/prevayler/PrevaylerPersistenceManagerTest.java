/*
 * Created on Apr 5, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.werken.werkflow.service.persistence.prevayler;

import java.io.File;

import junit.framework.TestCase;

import org.prevayler.implementation.TransientPublisher;

import com.werken.werkflow.AttributeDeclaration;
import com.werken.werkflow.Attributes;
import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.admin.DeploymentException;
import com.werken.werkflow.service.persistence.CaseTransfer;
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

        try
        {
            ProcessPersistenceManager processManager = _manager.activate(info);
            assertNotNull(processManager);
            
            _manager.passivate(processManager);
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

    public void testAdd()
    {
        ProcessInfo info = createInfo("add-n-get");

        try
        {
            ProcessPersistenceManager processManager = _manager.activate(info);
            assertNotNull(processManager);

            CaseTransfer newCaseTransfer = processManager.newCase(Attributes.EMPTY_ATTRIBUTES);

            assertNotNull(newCaseTransfer);
            assertNotNull(newCaseTransfer.getCaseId());
            assertFalse(newCaseTransfer.getCaseId().length() == 0);
            
            String caseId = newCaseTransfer.getCaseId();
            assertTrue(processManager.hasCase(caseId));
            
            CaseTransfer loadCaseTransfer = processManager.loadCase(caseId);
            assertNotNull(loadCaseTransfer);
            assertEquals(caseId, loadCaseTransfer.getCaseId());

            _manager.passivate(processManager);            
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

    private PrevaylerPersistenceManager createManager()
    {
        PrevaylerPersistenceManager manager = new PrevaylerPersistenceManager();
        manager.setTransactionPublisher(new TransientPublisher());
        manager.setSnapOnStop(false);
        manager.setStorePath(storePath());

        return manager;
    }

    private void releaseManager()
    {
        deleteDirectory(new File(_manager.storePath()));
        _manager = null;
    }
    
    private void deleteDirectory(File directory)
    {
        if (directory.isDirectory())
        {
            File[] contents = directory.listFiles();
            
            for (int i = 0; i < contents.length; i++)
            {
                File file = contents[i];
                if (file.isDirectory())
                {
                    deleteDirectory(file);
                }
                else
                {
                    file.delete();            
                }
            }
            
            directory.delete();
        }
    }

    private String storePath()
    {
        String tempDir = System.getProperty("java.io.tmpdir");
        return (tempDir.endsWith(File.separator) ? tempDir : tempDir + File.separator) + "werkflow-prevayler-test";
    }

    private ProcessInfo createInfo(String suffix)
    {
        return new SimpleInfo("test-package-" + suffix, "test-process-" + suffix);
    }
    
    private PrevaylerPersistenceManager _manager;

    protected void setUp() throws Exception
    {
        _manager = createManager();
    }

    protected void tearDown() throws Exception
    {
        releaseManager();
    }

}
