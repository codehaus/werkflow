package org.codehaus.werkflow.definition.petri;

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

import org.codehaus.werkflow.work.Action;
import org.codehaus.werkflow.expr.Expression;
import org.codehaus.werkflow.definition.Waiter;
import org.codehaus.werkflow.definition.MessageType;
import org.codehaus.werkflow.definition.MessageWaiter;
import org.codehaus.werkflow.definition.MessageCorrelator;
import org.codehaus.werkflow.definition.NoSuchMessageTypeException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class IdiomDefinition
{
    public static final IdiomDefinition[] EMPTY_ARRAY = new IdiomDefinition[0];

    public static final String IN_PLACE = "in";
    public static final String OUT_PLACE = "out";

    public static final String ACTION = "action";
    public static final String COMPONENT = "component";

    public static final String COMPONENT_PREFIX = "component:";
    public static final String PARAMETER_PREFIX = "parameter:";
    public static final String STASHED_PREFIX = "stashed:";
    public static final String COMPLETE_PREFIX = "complete:";
    public static final String GLOBAL_OUT = "global:out";

    public static final short CONTAINS_NONE = 0;
    public static final short CONTAINS_MULTI_COMPONENTS = 1;
    public static final short CONTAINS_ONE_COMPONENT = 2;
    public static final short CONTAINS_ONE_ACTION = 3;

    public static final IdiomDefinition ACTION_IDIOM = new IdiomDefinition( "werkflow:internal-idioms",
                                                                            "werkflow.action",
                                                                            CONTAINS_ONE_ACTION );

    static
    {
        /*
        try
        {
            ACTION_IDIOM.addParameter( new IdiomParameter( "action",
                                                           "action",
                                                           true ) );
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit( 1 );
        }
        */

        ACTION_IDIOM.addTransition( new TransitionDefinition( "action",
                                                              "action" ) );

        ACTION_IDIOM.addArc( ArcDefinition.newArcFromPlaceToTransition( "in",
                                                                        "action",
                                                                        null ) );

        ACTION_IDIOM.addArc( ArcDefinition.newArcFromTransitionToPlace( "action",
                                                                        "out",
                                                                        null ) );
    }

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private String uri;
    private String id;
    private Map parameters;
    private Map places;
    private Map transitions;
    private List arcs;
    private short containsType;

    public IdiomDefinition(String uri,
                           String id,
                           short containsType)
    {
        this( uri,
              id,
              Collections.EMPTY_MAP,
              containsType );
    }

    public IdiomDefinition(String uri,
                           String id,
                           Map parameters,
                           short containsType)
    {
        this.uri         = uri;
        this.id          = id;
        this.parameters  = new HashMap( parameters );
        this.places      = new HashMap();
        this.transitions = new HashMap();
        this.arcs        = new ArrayList();
        this.containsType = containsType;
    }

    public String toString()
    {
        return "[IdiomDefinition: id=" + this.id
            + "; parameters=" + this.parameters
            + "; places=" + this.places.values()
            + "; transitions=" + this.transitions.values()
            + "; arcs=" + this.arcs
            + "]";
    }

    public short getContainsType()
    {
        return this.containsType;
    }

    public String getUri()
    {
        return this.uri;
    }

    public String getId()
    {
        return this.id;
    }

    public void addParameter(IdiomParameter parameter)
        throws DuplicateParameterException
    {
        if ( this.parameters.containsKey( parameter.getId() ) )
        {
            throw new DuplicateParameterException( parameter );
        }

        this.parameters.put( parameter.getId(),
                             parameter );
    }

    public IdiomParameter[] getParameters()
    {
        return (IdiomParameter[]) this.parameters.values().toArray( IdiomParameter.EMPTY_ARRAY );
    }

    public IdiomParameter getParameter(String id)
        throws NoSuchParameterException
    {
        if ( ! this.parameters.containsKey( id ) )
        {
            throw new NoSuchParameterException( id );
        }

        return (IdiomParameter) this.parameters.get( id );
    }

    public void addPlace(PlaceDefinition placeDef)
    {
        this.places.put( placeDef.getId(),
                         placeDef );
    }

    public PlaceDefinition getPlace(String id)
    {
        return (PlaceDefinition) this.places.get( id );
    }

    public PlaceDefinition[] getPlaces()
    {
        return (PlaceDefinition[]) this.places.values().toArray( PlaceDefinition.EMPTY_ARRAY );
    }

    public void addTransition(TransitionDefinition transitionDef)
    {
        this.transitions.put( transitionDef.getId(),
                              transitionDef );
    }

    public TransitionDefinition getTransition(String id)
    {
        return (TransitionDefinition) this.transitions.get( id );
    }

    public TransitionDefinition[] getTransitions()
    {
        return (TransitionDefinition[]) this.transitions.values().toArray( TransitionDefinition.EMPTY_ARRAY );
    }

    public void addArc(ArcDefinition arcDef)
    {
        this.arcs.add( arcDef );
    }

    public ArcDefinition[] getArcs()
    {
        return (ArcDefinition[]) this.arcs.toArray( ArcDefinition.EMPTY_ARRAY );
    }

    public Idiom newIdiom()
    {
        Idiom idiom = new Idiom( this );

        return idiom;
    }

    Idiom newIdiom(Idiom parent,
                   int counter)
    {
        Idiom idiom = new Idiom( this,
                                 parent,
                                 counter );

        return idiom;
    }

    protected void buildStatic(Idiom idiom)
        throws IdiomException
    {
        idiom.addPlace( IN_PLACE,
                        "in" );

        idiom.stash( "in",
                     IN_PLACE );

        idiom.addPlace( OUT_PLACE,
                        "out" );

        idiom.stash( "out",
                     OUT_PLACE );

        PlaceDefinition[] placeDefs = getPlaces();

        for ( int i = 0 ; i < placeDefs.length ; ++i )
        {
            if ( ! placeDefs[i].getId().startsWith( COMPONENT_PREFIX ) )
            {
                idiom.addPlace( qualifyId( placeDefs[i].getId(),
                                           idiom ),

                                placeDefs[i].getDocumentation() );

                if ( placeDefs[i].getStashId() != null )
                {
                    idiom.stash( placeDefs[i].getStashId(),
                                 placeDefs[i].getId() );
                }
            }
        }

        TransitionDefinition[] transitionDefs = getTransitions();

        for ( int i = 0 ; i < transitionDefs.length ; ++i )
        {
            if ( ! ( transitionDefs[i].getId().equals( ACTION )
                     ||
                     transitionDefs[i].getId().equals( COMPONENT )
                     ||
                     transitionDefs[i].getId().startsWith( COMPLETE_PREFIX ) ) )
            {
                try
                {
                    idiom.addTransition( qualifyId( transitionDefs[i].getId(),
                                                    idiom ),
                                         transitionDefs[i].getDocumentation(),
                                         getAction( idiom,
                                                    transitionDefs[i].getAction() ),
                                         getWaiter( idiom,
                                                    transitionDefs[i].getWaiter() ) );
                }
                catch (NoSuchMessageTypeException e)
                {
                    throw new IdiomException( e );
                }
            }
        }

        ArcDefinition[] arcDefs = getArcs();

        for ( int i = 0 ; i < arcDefs.length ; ++i )
        {
            if ( ! ( arcDefs[i].getPlaceId().startsWith( COMPONENT_PREFIX )
                     ||
                     arcDefs[i].getTransitionId().startsWith( COMPONENT_PREFIX )
                     ||
                     arcDefs[i].getTransitionId().equals( COMPONENT )
                     ||
                     arcDefs[i].getTransitionId().equals( ACTION )
                     ||
                     arcDefs[i].getTransitionId().startsWith( COMPLETE_PREFIX ) ) )
            {
                Expression expr = getExpression( idiom,
                                                 arcDefs[i].getExpression() );

                if ( arcDefs[i].getType() == ArcDefinition.PLACE_TO_TRANSITION_TYPE )
                {
                    idiom.connectPlaceToTransition( qualifyId( arcDefs[i].getPlaceId(),
                                                               idiom ),
                                                    qualifyId( arcDefs[i].getTransitionId(),
                                                               idiom ),
                                                    expr );
                }
                else
                {
                    idiom.connectTransitionToPlace( qualifyId( arcDefs[i].getTransitionId(),
                                                               idiom ),
                                                    qualifyId( arcDefs[i].getPlaceId(),
                                                               idiom ),
                                                    expr );
                }
            }
        }
    }

    protected void buildComplete(Idiom idiom)
        throws IdiomException
    {
        TransitionDefinition[] transitionDefs = getTransitions();

        for ( int i = 0 ; i < transitionDefs.length ; ++i )
        {
            if ( transitionDefs[i].getId().startsWith( COMPLETE_PREFIX ) )
            {
                String useId = transitionDefs[i].getId().substring( COMPLETE_PREFIX.length() );

                try
                {
                    idiom.addTransition( qualifyId( useId,
                                                    idiom ),
                                         transitionDefs[i].getDocumentation(),
                                         getAction( idiom,
                                                    transitionDefs[i].getAction() ),
                                         getWaiter( idiom,
                                                    transitionDefs[i].getWaiter() ) );
                }
                catch (NoSuchMessageTypeException e)
                {
                    throw new IdiomException( e );
                }
            }
        }

        ArcDefinition[] arcDefs = getArcs();

        for ( int i = 0 ; i < arcDefs.length ; ++i )
        {
            if ( arcDefs[i].getTransitionId().startsWith( COMPLETE_PREFIX ) )
            {
                String useTransId = arcDefs[i].getTransitionId().substring( COMPLETE_PREFIX.length() );

                String usePlaceId = arcDefs[i].getPlaceId();

                if ( usePlaceId.startsWith( STASHED_PREFIX ) )
                {
                    usePlaceId = idiom.getStashed( usePlaceId.substring( STASHED_PREFIX.length() ) );
                }
                else
                {
                    usePlaceId = qualifyId( usePlaceId,
                                            idiom );
                }

                Expression expr = getExpression( idiom,
                                                 arcDefs[i].getExpression() );

                if ( arcDefs[i].getType() == ArcDefinition.PLACE_TO_TRANSITION_TYPE )
                {
                    idiom.connectPlaceToTransition( usePlaceId,
                                                    qualifyId( useTransId,
                                                               idiom ),
                                                    expr );
                }
                else
                {
                    idiom.connectTransitionToPlace( qualifyId( useTransId,
                                                               idiom ),
                                                    usePlaceId,
                                                    expr );
                }
            }
        }
    }

    protected void addAction(Idiom idiom,
                             Action action)
        throws IdiomException
    {
        TransitionDefinition transDefs[] = getTransitions();

        for ( int i = 0 ; i < transDefs.length ; ++i )
        {
            if ( transDefs[i].getId().equals( ACTION ) )
            {
                try
                {
                    idiom.addTransition( qualifyId( transDefs[i].getId(),
                                                    idiom ),
                                         transDefs[i].getDocumentation(),
                                         action,
                                         getWaiter( idiom,
                                                    transDefs[i].getWaiter() ) );
                }
                catch (NoSuchMessageTypeException e)
                {
                    throw new IdiomException( e );
                }
            }
        }

        ArcDefinition[] arcDefs = getArcs();

        for ( int i = 0 ; i < arcDefs.length ; ++i )
        {
            if ( arcDefs[i].getTransitionId().equals( ACTION ) )
            {
                String placeId = qualifyId( arcDefs[i].getPlaceId(),
                                            idiom );

                String transId = qualifyId( arcDefs[i].getTransitionId(),
                                            idiom );

                Expression expr = getExpression( idiom,
                                                 arcDefs[i].getExpression() );

                if ( arcDefs[i].getType() == ArcDefinition.PLACE_TO_TRANSITION_TYPE )
                {
                    idiom.connectPlaceToTransition( placeId,
                                                    transId,
                                                    expr );
                }
                else
                {
                    idiom.connectTransitionToPlace( transId,
                                                    placeId,
                                                    expr );
                }
            }
        }
    }

    protected void addComponent(Idiom idiom,
                                Idiom component)
        throws IdiomException
    {
        Map stashings = new HashMap();

        PlaceDefinition[] placeDefs = getPlaces();

        for ( int i = 0 ; i < placeDefs.length ; ++i )
        {
            if ( placeDefs[i].getId().startsWith( COMPONENT_PREFIX ) )
            {
                String id = createId( placeDefs[i].getId(),
                                      component );

                idiom.addPlace( id,
                                placeDefs[i].getDocumentation() );

                if ( placeDefs[i].getStashId() != null )
                {
                    stashings.put( placeDefs[i].getStashId(),
                                   id );
                }
            }
        }

        TransitionDefinition[] transitionDefs = getTransitions();

        for ( int i = 0 ; i < transitionDefs.length ; ++i )
        {
            if ( transitionDefs[i].getId().startsWith( COMPONENT_PREFIX ) )
            {
                String id = createId( transitionDefs[i].getId(),
                                      component );

                try
                {
                    idiom.addTransition( id,
                                         transitionDefs[i].getDocumentation(),
                                         getAction( idiom,
                                                    transitionDefs[i].getAction() ),
                                         getWaiter( idiom,
                                                    transitionDefs[i].getWaiter() ) );
                }
                catch (NoSuchMessageTypeException e)
                {
                    throw new IdiomException( e );
                }
            }
        }

        ArcDefinition[] arcDefs = getArcs();

        for ( int i = 0 ; i < arcDefs.length ; ++i )
        {
            String useId = arcDefs[i].getPlaceId();

            if ( useId.startsWith( STASHED_PREFIX ) )
            {
                useId = idiom.getStashed( useId.substring( STASHED_PREFIX.length() ) );
            }
            else
            {
                useId = createId( useId,
                                  component );
            }

            if ( arcDefs[i].getTransitionId().equals( COMPONENT ) )
            {
                if ( arcDefs[i].getType() == ArcDefinition.PLACE_TO_TRANSITION_TYPE )
                {
                    idiom.graftComponentInput( useId,
                                               component,
                                               getExpression( idiom,
                                                              arcDefs[i].getExpression() ) );
                }
                else
                {
                    idiom.graftComponentOutput( component,
                                                useId,
                                                getExpression( idiom,
                                                               arcDefs[i].getExpression() ) );
                }
            }
            else if ( arcDefs[i].getPlaceId().startsWith( COMPONENT_PREFIX ) )
            {
                String transitionId = createId( arcDefs[i].getTransitionId(),
                                                component );

                Expression expr = getExpression( idiom,
                                                 arcDefs[i].getExpression() );

                if ( arcDefs[i].getType() == ArcDefinition.PLACE_TO_TRANSITION_TYPE )
                {
                    idiom.connectPlaceToTransition( useId,
                                                    transitionId,
                                                    expr );
                }
                else
                {
                    idiom.connectTransitionToPlace( transitionId,
                                                    useId,
                                                    expr );
                }
            }
        }

        idiom.stashAll( stashings );

        /*
          for ( int i = 0 ; i < placeDefs.length ; ++i )
          {
          if ( placeDefs[i].getId().startsWith( COMPONENT_PREFIX ) )
          {
          if ( placeDefs[i].getStashId() != null )
          {
          idiom.stash( placeDefs[i].getStashId(),
          placeDefs[i].getId() );
          }
          }
          }
        */
    }

    protected String createId(String id,
                              Idiom component)
    {
        if ( id.startsWith( COMPONENT_PREFIX ) )
        {
            return qualifyId( id.substring( COMPONENT_PREFIX.length() ),
                              component );
        }

        return qualifyId( id,
                          component.getParent() );
    }

    protected String qualifyId(String id,
                               Idiom component)
    {
        if ( IN_PLACE.equals( id )
             ||
             OUT_PLACE.equals( id )
             ||
             GLOBAL_OUT.equals( id ) )
        {
            return id;
        }

        return component.getId() + ":" + id;
    }

    protected Expression getExpression(Idiom idiom,
                                       String exprStr)
        throws NoSuchParameterException
    {
        if ( exprStr == null
             ||
             exprStr.equals( "" ) )
        {
            return null;
        }

        if ( exprStr.startsWith( PARAMETER_PREFIX ) )
        {
            return (Expression) idiom.getParameter( exprStr.substring( PARAMETER_PREFIX.length() ) );
        }

        if ( exprStr.equals( "TRUE" ) )
        {
            return Expression.TRUE;
        }

        if ( exprStr.equals( "FALSE" ) )
        {
            return Expression.FALSE;
        }

        return null;
    }

    protected Action getAction(Idiom idiom,
                               String actionStr)
        throws NoSuchParameterException
    {
        if ( actionStr == null
             ||
             actionStr.equals( "" ) )
        {
            return null;
        }

        if ( actionStr.startsWith( PARAMETER_PREFIX ) )
        {
            Action action = (Action) idiom.getParameter( actionStr.substring( PARAMETER_PREFIX.length() ) );

            return action;
        }

        return null;
    }

    protected Waiter getWaiter(Idiom idiom,
                               WaiterDefinition waiterDef)
        throws NoSuchParameterException, NoSuchMessageTypeException
    {
        if ( waiterDef instanceof MessageWaiterDefinition )
        {
            return getMessageWaiter( idiom,
                                     (MessageWaiterDefinition) waiterDef );
        }

        return null;
    }

    protected MessageWaiter getMessageWaiter(Idiom idiom,
                                             MessageWaiterDefinition waiterDef)
        throws NoSuchParameterException, NoSuchMessageTypeException
    {
        String msgTypeStr = waiterDef.getMessageType();

        if ( msgTypeStr.startsWith( PARAMETER_PREFIX ) )
        {
            msgTypeStr = (String) idiom.getParameter( msgTypeStr.substring( PARAMETER_PREFIX.length() ) );
        }

        MessageType msgType = idiom.getScope().getMessageType( msgTypeStr );

        String bindName = waiterDef.getBind();

        if ( bindName != null
             &&
             bindName.startsWith( PARAMETER_PREFIX ) )
        {
            bindName = (String) idiom.getParameter( bindName.substring( PARAMETER_PREFIX.length() ) );
        }

        if ( bindName == null
             ||
             bindName.equals( "" ) )
        {
            bindName = "message";
        }

        MessageWaiter waiter = new MessageWaiter( msgType,
                                                  bindName );

        String corrStr = waiterDef.getCorrelator();

        if ( corrStr != null )
        {
            MessageCorrelator correlator = null;

            if ( corrStr.startsWith( PARAMETER_PREFIX ) )
            {
                corrStr = corrStr.substring( PARAMETER_PREFIX.length() );

                correlator = (MessageCorrelator) idiom.getParameter( corrStr );
            }

            waiter.setMessageCorrelator( correlator );
        }

        return waiter;
    }
}
