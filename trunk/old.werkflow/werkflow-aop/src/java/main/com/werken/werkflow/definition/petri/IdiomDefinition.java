package com.werken.werkflow.definition.petri;

import com.werken.werkflow.definition.Expression;

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

    public static final String COMPONENT = "component";
    public static final String BODY = "body";

    public static final String COMPONENT_PREFIX = "component:";
    public static final String PARAMETER_PREFIX = "parameter:";
    public static final String STASHED_PREFIX = "stashed:";

    private static final String[] EMPTY_STRING_ARRAY = new String[0];


    private String id;
    private Map parameters;
    private Map places;
    private Map transitions;
    private List arcs;

    public IdiomDefinition(String id)
    {
        this( id,
              Collections.EMPTY_MAP );
    }

    public IdiomDefinition(String id,
                           Map parameters)
    {
        this.id          = id;
        this.parameters  = new HashMap( parameters );
        this.places      = new HashMap();
        this.transitions = new HashMap();
        this.arcs        = new ArrayList();
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

        idiom.addPlace( OUT_PLACE,
                        "out" );

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
            if ( ! ( transitionDefs[i].getId().startsWith( COMPONENT_PREFIX )
                     ||
                     transitionDefs[i].getId().equals( COMPONENT ) ) )
            {
                idiom.addTransition( qualifyId( transitionDefs[i].getId(),
                                                idiom ),
                                     transitionDefs[i].getDocumentation() );
            }
        }

        ArcDefinition[] arcDefs = getArcs();

        for ( int i = 0 ; i < arcDefs.length ; ++i )
        {
            if ( ! ( arcDefs[i].getPlaceId().startsWith( COMPONENT_PREFIX )
                     ||
                     arcDefs[i].getTransitionId().startsWith( COMPONENT_PREFIX )
                     ||
                     arcDefs[i].getTransitionId().equals( COMPONENT ) ) )
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
                    System.err.println( "A" );
                    idiom.connectTransitionToPlace( qualifyId( arcDefs[i].getTransitionId(),
                                                               idiom ),
                                                    qualifyId( arcDefs[i].getPlaceId(),
                                                               idiom ),
                                                    expr );
                }
            }
        }
    }

    protected void addComponent(Idiom idiom,
                                Idiom component)
        throws IdiomException
    {
        PlaceDefinition[] placeDefs = getPlaces();

        for ( int i = 0 ; i < placeDefs.length ; ++i )
        {
            if ( placeDefs[i].getId().startsWith( COMPONENT_PREFIX ) )
            {
                String id = createId( placeDefs[i].getId(),
                                      component );

                idiom.addPlace( id,
                                placeDefs[i].getDocumentation() );
            }
        }

        TransitionDefinition[] transitionDefs = getTransitions();

        for ( int i = 0 ; i < transitionDefs.length ; ++i )
        {
            if ( transitionDefs[i].getId().startsWith( COMPONENT_PREFIX ) )
            {
                String id = createId( transitionDefs[i].getId(),
                                      component );

                idiom.addTransition( id,
                                     transitionDefs[i].getDocumentation() );
            }
        }

        ArcDefinition[] arcDefs = getArcs();

        for ( int i = 0 ; i < arcDefs.length ; ++i )
        {
            System.err.println( "ARC: " + arcDefs[i] );
            if ( arcDefs[i].getTransitionId().equals( COMPONENT ) )
            {
                String placeId = createId( arcDefs[i].getPlaceId(),
                                           component );

                if ( arcDefs[i].getType() == ArcDefinition.PLACE_TO_TRANSITION_TYPE )
                {
                    idiom.graftComponentInput( placeId,
                                               component,
                                               getExpression( idiom,
                                                              arcDefs[i].getExpression() ) );
                }
                else
                {
                    idiom.graftComponentOutput( component,
                                                placeId,
                                                getExpression( idiom,
                                                               arcDefs[i].getExpression() ) );
                }
            }
            else if ( arcDefs[i].getPlaceId().startsWith( COMPONENT_PREFIX ) )
            {
                String placeId = createId( arcDefs[i].getPlaceId(),
                                           component );
                
                String transitionId = createId( arcDefs[i].getTransitionId(),
                                                component );
                
                Expression expr = getExpression( idiom,
                                                 arcDefs[i].getExpression() );

                if ( arcDefs[i].getType() == ArcDefinition.PLACE_TO_TRANSITION_TYPE )
                {
                    idiom.connectPlaceToTransition( placeId,
                                                    transitionId,
                                                    expr );
                }
                else
                {
                    System.err.println( "B" );

                    idiom.connectTransitionToPlace( transitionId,
                                                    placeId,
                                                    expr );
                }
            }
            else if ( arcDefs[i].getTransitionId().startsWith( "*" ) )
            {
                String placeId = createId( arcDefs[i].getPlaceId(),
                                           component );

                String transitionId = createId( arcDefs[i].getTransitionId(),
                                                component );

                Expression expr = getExpression( idiom,
                                                 arcDefs[i].getExpression() );

                if ( arcDefs[i].getType() == ArcDefinition.PLACE_TO_TRANSITION_TYPE )
                {
                    idiom.connectPlaceToTransition( placeId,
                                                    transitionId,
                                                    expr );
                }
                else
                {
                    System.err.println( "C" );
                    idiom.connectTransitionToPlace( transitionId,
                                                    placeId,
                                                    expr );
                }
            }
        }
        
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
             OUT_PLACE.equals( id ) )
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
}
