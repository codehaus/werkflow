package com.werken.werkflow.avalon.plexus;

import com.werken.werkflow.Wfms;
import com.werken.werkflow.WfmsRuntime;
import com.werken.werkflow.admin.WfmsAdmin;
import com.werken.werkflow.engine.WorkflowEngine;
import com.werken.werkflow.log.AvalonLog;
import com.werken.werkflow.service.SimpleWfmsServices;
import com.werken.werkflow.service.caserepo.CaseRepository;
import com.werken.werkflow.service.messaging.MessagingManager;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.plexus.service.Serviceable;
import org.apache.plexus.service.ServiceBroker;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.thread.ThreadSafe;

public class PlexusWfms
    extends AbstractLogEnabled
    implements Wfms, Serviceable, Configurable, Initializable, ThreadSafe
{
    private WorkflowEngine engine;
    private ServiceBroker serviceBroker;
    private Configuration configuration;

    public PlexusWfms()
    {
    }

    private WorkflowEngine getEngine()
    {
        return this.engine;
    }

    public WfmsAdmin getAdmin()
    {
        return getEngine().getAdmin();
    }

    public WfmsRuntime getRuntime()
    {
        return getEngine().getRuntime();
    }

    public void service(ServiceBroker serviceBroker)
        throws ServiceException
    {
        this.serviceBroker = serviceBroker;
    }

    public ServiceBroker getServiceBroker()
    {
        return this.serviceBroker;
    }

    public void configure(Configuration configuration)
        throws ConfigurationException
    {
        this.configuration = configuration;
    }

    public Configuration getConfiguration()
    {
        return this.configuration;
    }

    public void initialize()
        throws Exception
    {
        Configuration caseRepoConfig = getConfiguration().getChild( "case-repo" );

        if ( caseRepoConfig == null )
        {
            throw new Exception( "no <case-repo> specified" );
        }

        String caseRepoId = caseRepoConfig.getValue();

        CaseRepository caseRepo = (CaseRepository) getServiceBroker().lookup( CaseRepository.ROLE,
                                                                              caseRepoId );

        Configuration messagingManagerConfig = getConfiguration().getChild( "messaging-manager" );

        if ( messagingManagerConfig == null )
        {
            throw new Exception( "no <messaging-manager> specified" );
        }

        String messagingManagerId = getConfiguration().getChild( "message-manager" ).getValue();

        MessagingManager messagingManager = (MessagingManager) getServiceBroker().lookup( MessagingManager.ROLE,
                                                                                          messagingManagerId );
        SimpleWfmsServices services = new SimpleWfmsServices();

        services.setCaseRepository( caseRepo );

        services.setMessagingManager( messagingManager );

        this.engine = new WorkflowEngine( services,
                                          new AvalonLog( getLogger() ) );
    }
}
