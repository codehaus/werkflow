package com.werken.werkflow.definition;

import com.werken.werkflow.action.Action;

import java.util.Map;
import java.util.HashMap;

public class ActionLibrary
{
    public static final String MESSAGE_TYPE_LIBRARY_KEY = "werkflow.msg.type.lib";

    private ActionLibrary parent;
    private Map actions;

    public ActionLibrary()
    {
        this( null );
    }
    
    public ActionLibrary(ActionLibrary parent)
    {
        this.parent = parent;
        this.actions = new HashMap();
    }

    public ActionLibrary getParent()
    {
        return this.parent;
    }

    public boolean containsAction(String id)
    {
        if ( this.actions.containsKey( id ) )
        {
            return true;
        }

        if ( this.parent != null )
        {
            return this.parent.containsAction( id );
        }

        return false;
    }

    public void addAction(String id,
                          Action action)
        throws DuplicateActionException
    {
        if ( containsAction( id ) )
        {
            throw new DuplicateActionException( id );
        }

        this.actions.put( id,
                          action );
    }

    public Action getAction(String id)
        throws NoSuchActionException
    {
        if ( ! this.actions.containsKey( id ) )
        {
            if ( this.parent == null )
            {
                throw new NoSuchActionException( id );
            }

            return this.parent.getAction( id );
        }
        
        return (Action) this.actions.get( id );
    }
}
