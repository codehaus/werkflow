package com.werken.werkflow.syntax.petri;

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
