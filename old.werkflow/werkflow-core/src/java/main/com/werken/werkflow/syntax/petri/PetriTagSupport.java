package com.werken.werkflow.syntax.petri;

import com.werken.werkflow.definition.petri.DefaultNet;
import com.werken.werkflow.definition.petri.DefaultTransition;
import com.werken.werkflow.jelly.MiscTagSupport;
import com.werken.werkflow.syntax.fundamental.FundamentalTagSupport;

import org.apache.commons.jelly.JellyTagException;

public abstract class PetriTagSupport
    extends FundamentalTagSupport
{
    public PetriTagSupport()
    {

    }

    public DefaultNet getCurrentNet()
        throws JellyTagException
    {
        DefaultNet net = (DefaultNet) getContext().getVariable( DefaultNet.class.getName() );

        if ( net == null )
        {
            throw new JellyTagException( "no current net" );
        }

        return net;
    }

    public void setCurrentNet(DefaultNet newNet)
        throws JellyTagException
    {
        DefaultNet curNet = (DefaultNet) getContext().getVariable( DefaultNet.class.getName() );

        if ( curNet != null
             &&
             newNet != null )
        {
            throw new JellyTagException( "internal error: already a current net" );
        }

        getContext().setVariable( DefaultNet.class.getName(),
                                  newNet );
    }

    public DefaultTransition getCurrentTransition()
        throws JellyTagException
    {
        DefaultTransition transition = (DefaultTransition) getContext().getVariable( DefaultTransition.class.getName() );

        if ( transition == null )
        {
            throw new JellyTagException( "no current transition" );
        }

        return transition;
    }

    public void setCurrentTransition(DefaultTransition newTrans)
        throws JellyTagException
    {
        DefaultTransition curTrans = (DefaultTransition) getContext().getVariable( DefaultTransition.class.getName() );

        if ( curTrans != null
             &&
             newTrans != null )
        {
            throw new JellyTagException( "internal error: already a current transition" );
        }

        getContext().setVariable( DefaultTransition.class.getName(),
                                  newTrans );
    }
}
