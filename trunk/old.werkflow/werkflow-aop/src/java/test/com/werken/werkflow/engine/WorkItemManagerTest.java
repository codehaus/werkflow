package com.werken.werkflow.engine;

import com.werken.werkflow.resource.ResourceClass;
import com.werken.werkflow.resource.NoSuchResourceClassException;
import com.werken.werkflow.service.SimpleWfmsServices;
import com.werken.werkflow.task.MockTask;
import com.werken.werkflow.task.ResourceSpec;

import junit.framework.TestCase;

public class WorkItemManagerTest
    extends TestCase
{
    private SimpleWfmsServices services;
    private WorkflowEngine engine;

    private ResourceClass humanResourceClass;
    private ResourceClass machineResourceClass;
    private ResourceClass otherOfflineResourceClass;
    private ResourceClass otherOnlineResourceClass;

    private WorkItemManager manager;

    public void setUp()
        throws Exception
    {
        this.services = new SimpleWfmsServices();
        this.engine   = new WorkflowEngine( this.services );

        this.humanResourceClass = new ResourceClass( "human",
                                                     ResourceClass.Category.CAPABILITY,
                                                     ResourceClass.Accessibility.OFFLINE );

        this.otherOfflineResourceClass = new ResourceClass( "otherOffline",
                                                            ResourceClass.Category.CAPABILITY,
                                                            ResourceClass.Accessibility.OFFLINE );
        
        this.machineResourceClass = new ResourceClass( "machine",
                                                       ResourceClass.Category.CAPABILITY,
                                                       ResourceClass.Accessibility.ONLINE );

        this.otherOnlineResourceClass = new ResourceClass( "otherOnline",
                                                           ResourceClass.Category.CAPABILITY,
                                                           ResourceClass.Accessibility.ONLINE );

        this.engine.addResourceClass( this.humanResourceClass );
        this.engine.addResourceClass( this.machineResourceClass );
        this.engine.addResourceClass( this.otherOfflineResourceClass );
        this.engine.addResourceClass( this.otherOnlineResourceClass );

        this.manager = engine.getWorkItemManager();
    }

    public void testConstruct()
    {
        assertNotNull( this.manager );

        assertSame( engine,
                    this.manager.getEngine() );
    }

    public void testRequiresOfflineResource_ResourceSpec()
        throws Exception
    {
        ResourceSpec spec = new ResourceSpec();

        assertFalse( this.manager.requiresOfflineResource( spec ) );

        spec.addResourceClassId( "machine" );

        assertFalse( this.manager.requiresOfflineResource( spec ) );

        spec.addResourceClassId( "otherOnline" );

        assertFalse( this.manager.requiresOfflineResource( spec ) );

        spec.addResourceClassId( "human" );

        assertTrue( this.manager.requiresOfflineResource( spec ) );

        spec.addResourceClassId( "no class" );

        try
        {
            this.manager.requiresOfflineResource( spec );
        }
        catch (NoSuchResourceClassException e)
        {
            // expected and correcto
            assertEquals( "no class",
                          e.getId() );
        }
    }

    public void testRequiresOfflineResource_Task()
        throws Exception
    {
        MockTask task = new MockTask( "the task" );
        
        assertFalse( this.manager.requiresOfflineResource( task ) );

        ResourceSpec spec = new ResourceSpec();

        task.addResourceSpec( spec );

        assertFalse( this.manager.requiresOfflineResource( task ) );

        spec.addResourceClassId( "machine" );

        assertFalse( this.manager.requiresOfflineResource( task ) );

        spec.addResourceClassId( "otherOnline" );

        assertFalse( this.manager.requiresOfflineResource( task ) );

        spec.addResourceClassId( "human" );

        assertTrue( this.manager.requiresOfflineResource( task ) );

        spec.addResourceClassId( "no class" );

        try
        {
            this.manager.requiresOfflineResource( task );
        }
        catch (NoSuchResourceClassException e)
        {
            // expected and correcto
            assertEquals( "no class",
                          e.getId() );
        }
    }
}
