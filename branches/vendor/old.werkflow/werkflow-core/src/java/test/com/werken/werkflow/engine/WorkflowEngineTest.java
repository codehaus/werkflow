package com.werken.werkflow.engine;

import com.werken.werkflow.service.MockWfmsServices;
import com.werken.werkflow.service.caserepo.CaseRepository;
import com.werken.werkflow.service.caserepo.InMemoryCaseRepository;

import junit.framework.TestCase;

public class WorkflowEngineTest
    extends TestCase
{
    private MockWfmsServices services;
    private CaseRepository caseRepo;

    public void setUp()
    {
        this.services = new MockWfmsServices();
        this.caseRepo = new InMemoryCaseRepository();

        this.services.setCaseRepository( this.caseRepo );
    }

    public void tearDown()
    {
        this.services = null;
    }

    public void testConstruct()
    {
        WorkflowEngine engine = new WorkflowEngine( services );

        assertSame( this.services,
                    engine.getServices() );

        assertNotNull( engine.getResourceManager() );
        assertNotNull( engine.getActivityManager() );
        assertNotNull( engine.getWorkItemManager() );

        assertNotNull( engine.getRuntime() );
        assertNotNull( engine.getAdmin() );

        assertEquals( 0,
                      engine.getProcesses().length );
    }   
}
