package com.werken.werkflow.definition.fundamental;

import com.werken.werkflow.action.Action;
import com.werken.werkflow.action.ActionLibrary;
import com.werken.werkflow.jelly.MiscTagSupport;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyException;

public class ActionTag
    extends MiscTagSupport
    implements ActionReceptor
{
    private String id;
    private Action action;
    private boolean isDefault;

    public ActionTag()
    {
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public void setAction(Action action)
    {
        this.action = action;
    }

    public Action getAction()
    {
        return this.action;
    }

    public void setDefault(boolean isDefault)
    {
        this.isDefault = isDefault;
    }

    public boolean isDefault()
    {
        return this.isDefault;
    }

    public void doTag(XMLOutput output)
        throws Exception
    {
        TaskTag task = (TaskTag) findAncestorWithClass( TaskTag.class );

        if ( task == null )
        {
            requireStringAttribute( "id",
                                    getId() );
            invokeBody( output );
            
            if ( getAction() == null )
            {
                throw new JellyException( "no action defined in body" );
            }
            
            JellyContext ocntext = getContext();
            
            ActionLibrary actionLib = (ActionLibrary) context.getVariable( FundamentalDefinitionLoader.ACTION_LIBRARY_KEY );
            
            if ( actionLib == null )
            {
                throw new JellyException( "no action library" );
            }
            
            actionLib.addAction( getId(),
                                 getAction() );

            if ( isDefault() )
            {
                actionLib.setDefaultAction( getAction() );
            }
        }
        else
        {
            JellyContext ocntext = getContext();
            
            ActionLibrary actionLib = (ActionLibrary) context.getVariable( FundamentalDefinitionLoader.ACTION_LIBRARY_KEY );
            
            if ( actionLib == null )
            {
                throw new JellyException( "no action library" );
            }

            Action action = null;

            if ( getId() == null )
            {
                action = actionLib.getDefaultAction();

                if ( action == null )
                {
                    throw new JellyException( "no default action defined" );
                }
            }
            else
            {
                action = actionLib.getAction( getId() );
            }

            task.setAction( new ModifiableAction( getBody(),
                                                  action ) );
        }
    }
}
