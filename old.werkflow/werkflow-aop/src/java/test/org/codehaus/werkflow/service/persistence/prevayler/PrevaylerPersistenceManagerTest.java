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

import java.io.File;

import junit.framework.TestCase;

import org.prevayler.implementation.TransientPublisher;

import org.codehaus.werkflow.AttributeDeclaration;
import org.codehaus.werkflow.Attributes;
import org.codehaus.werkflow.ProcessInfo;
import org.codehaus.werkflow.admin.DeploymentException;
import org.codehaus.werkflow.service.persistence.CaseTransfer;
import org.codehaus.werkflow.service.persistence.PersistenceException;
import org.codehaus.werkflow.service.persistence.ProcessPersistenceManager;

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
         * @see org.codehaus.werkflow.ProcessInfo#getPackageId()
         */
        public String getPackageId()
        {
            return _packageId;
        }

        /**
         * @see org.codehaus.werkflow.ProcessInfo#getId()
         */
        public String getId()
        {
            return _processId;
        }

        /**
         * @see org.codehaus.werkflow.ProcessInfo#getDocumentation()
         */
        public String getDocumentation()
        {
            throw new RuntimeException("Unimplemented method");
        }

        /**
         * @see org.codehaus.werkflow.ProcessInfo#addAttributeDeclaration(org.codehaus.werkflow.AttributeDeclaration)
         */
        public void addAttributeDeclaration(AttributeDeclaration attrDecl)
        {
            throw new RuntimeException("Unimplemented method");
        }

        /**
         * @see org.codehaus.werkflow.ProcessInfo#getAttributeDeclarations()
         */
        public AttributeDeclaration[] getAttributeDeclarations()
        {
            return AttributeDeclaration.EMPTY_ARRAY;
        }

        /**
         * @see org.codehaus.werkflow.ProcessInfo#getAttributeDeclaration(java.lang.String)
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
