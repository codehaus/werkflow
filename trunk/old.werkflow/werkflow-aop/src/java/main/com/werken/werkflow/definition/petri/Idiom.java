package com.werken.werkflow.definition.petri;

import com.werken.werkflow.definition.Expression;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Idiom
    implements NetSegment
{
    public static final Idiom[] EMPTY_ARRAY = new Idiom[0];

    private boolean staticBuilt;

    private Idiom parent;
    private int index;

    private IdiomDefinition idiomDef;
    private Map parameters;
    private List components;
    private Map stashed;

    private Map places;
    private Map transitions;

    Idiom(IdiomDefinition idiomDef)
    {
        this( idiomDef,
              null,
              -1 );
    }

    Idiom(IdiomDefinition idiomDef,
          Idiom parent,
          int counter)
    {
        this.idiomDef   = idiomDef;
        this.parameters = new HashMap();
        this.components = new ArrayList();
        this.stashed    = new HashMap();

        this.places      = new HashMap();
        this.transitions = new HashMap();

        this.parent = parent;
        this.index  = index;
    }

    public IdiomDefinition getIdiomDefinition()
    {
        return this.idiomDef;
    }

    public Idiom getParent()
    {
        return this.parent;
    }

    public int getIndex()
    {
        return this.index;
    }

    public void setParameter(String name,
                             Object value)
        throws NoSuchParameterException, InvalidParameterTypeException
    {
        IdiomParameter param = getIdiomDefinition().getParameter( name );

        if ( ! param.getType().isInstance( value ) )
        {
            throw new InvalidParameterTypeException( param,
                                                     value );
        }

        this.parameters.put( name,
                             value );
    }

    public Object getParameter(String name)
        throws NoSuchParameterException
    {
        // just call to see if no-such should throw.

        getIdiomDefinition().getParameter( name );
        
        return this.parameters.get( name );
    }

    public void verify()
        throws IdiomException
    {
        verifyParameters();
    }

    public void build()
        throws IdiomException
    {
        verify();

        getIdiomDefinition().buildStatic( this );

        if ( getParent() != null )
        {
            getParent().addComponent( this );
        }
    }

    protected void verifyParameters()
        throws IdiomException
    {
        IdiomParameter[] params = getIdiomDefinition().getParameters();

        for ( int i = 0 ; i < params.length ; ++i )
        {
            if ( params[i].isRequired() )
            {
                if ( ! this.parameters.containsKey( params[i].getId() ) )
                {
                    throw new MissingParameterException( params[i] );
                }
            }
        }
    }

    public Idiom addComponent(IdiomDefinition idiomDef)
    {
        Idiom idiom = idiomDef.newIdiom( this,
                                         this.components.size() );
        
        this.components.add( idiom );

        return idiom;
    }

    private void addComponent(Idiom component)
        throws IdiomException
    {
        getIdiomDefinition().addComponent( this,
                                           component );
    }

    public Idiom[] getComponents()
    {
        return (Idiom[]) this.components.toArray( EMPTY_ARRAY );
    }

    void addPlace(String id,
                  String documentation)
    {
        DefaultPlace place = new DefaultPlace( id );

        place.setDocumentation( documentation );

        this.places.put( id,
                         place );
    }

    void removePlace(String id)
    {
        this.places.remove( id );
    }

    public Place getPlace(String id)
    {
        return (Place) this.places.get( id );
    }

    public Place[] getPlaces()
    {
        List places = new ArrayList();

        getPlaces( places );

        return (Place[]) places.toArray( Place.EMPTY_ARRAY );
    }

    void getPlaces(List dest)
    {
        dest.addAll( this.places.values() );

        Idiom[] components = getComponents();

        for ( int i = 0 ; i < components.length ; ++i )
        {
            components[i].getPlaces( dest );
        }
    }

    DefaultPlace getMutablePlace(String id)
    {
        return (DefaultPlace) this.places.get( id );
    }

    DefaultPlace getInPlace()
    {
        return (DefaultPlace) this.places.get( IdiomDefinition.IN_PLACE );
    }

    DefaultPlace getOutPlace()
    {
        return (DefaultPlace) this.places.get( IdiomDefinition.OUT_PLACE );
    }

    void addTransition(String id,
                       String documentation)
    {
        DefaultTransition transition = new DefaultTransition( id );

        transition.setDocumentation( documentation );

        this.transitions.put( id,
                              transition );
    }

    public Transition getTransition(String id)
    {
        Transition trans = (Transition) this.transitions.get( id );

        if ( trans == null )
        {
            Idiom[] components = getComponents();

            for ( int i = 0 ; ( i < components.length ) && ( trans == null ) ; ++i )
            {
                trans = components[i].getTransition( id );
            }
        }

        return trans;
    }

    public Transition[] getTransitions()
    {
        List transitions = new ArrayList();

        getTransitions( transitions );

        return (Transition[]) transitions.toArray( Transition.EMPTY_ARRAY );
    }

    void getTransitions(List dest)
    {
        dest.addAll( this.transitions.values() );

        Idiom[] components = getComponents();

        for ( int i = 0 ; i < components.length ; ++i )
        {
            components[i].getTransitions( dest );
        }
    }

    DefaultTransition getMutableTransition(String id)
    {
        return (DefaultTransition) this.transitions.get( id );
    }

    void connectPlaceToTransition(String placeId,
                                  String transitionId,
                                  Expression expression)
    {
        DefaultPlace place = getMutablePlace( placeId );
        DefaultTransition transition = getMutableTransition( transitionId );

        DefaultArc arc = new DefaultArc( place,
                                         transition );

        place.addOutboundArc( arc );
        transition.addInboundArc( arc );

        arc.setExpression( expression );
    }

    void connectTransitionToPlace(String transitionId,
                                  String placeId,
                                  Expression expression)
    {
        DefaultPlace place = getMutablePlace( placeId );
        DefaultTransition transition = getMutableTransition( transitionId );

        DefaultArc arc = new DefaultArc( place,
                                         transition );

        transition.addOutboundArc( arc );
        place.addInboundArc( arc );

        arc.setExpression( expression );
                                              
    }

    void graftComponentInput(String placeId,
                             Idiom component,
                             Expression expression)
    {
        DefaultPlace place = getMutablePlace( placeId );

        DefaultPlace componentIn = component.getInPlace();

        Arc[] componentArcs = componentIn.getArcsToTransitions();
        DefaultTransition componentTransition = null;

        DefaultArc graftArc = null;

        for ( int i = 0 ; i < componentArcs.length ; ++i )
        {
            componentTransition = (DefaultTransition) componentArcs[i].getTransition();
            
            graftArc = new DefaultArc( place,
                                       componentTransition );
            
            graftArc.setExpression( componentArcs[i].getExpression() );

            componentIn.removeOutboundArc( componentArcs[i] );
            componentTransition.removeInboundArc( componentArcs[i] );

            place.addOutboundArc( graftArc );
            componentTransition.addInboundArc( graftArc );
        }

        component.removePlace( componentIn.getId() );
    }

    void graftComponentOutput(Idiom component,
                              String placeId,
                              Expression expression)
    {
        DefaultPlace place = getMutablePlace( placeId );
        
        DefaultPlace componentOut = component.getOutPlace();
        
        Arc[] componentArcs = componentOut.getArcsFromTransitions();
        DefaultTransition componentTransition = null;
        
        DefaultArc graftArc = null;
        
        for ( int i = 0 ; i < componentArcs.length ; ++i )
        {
            componentTransition = (DefaultTransition) componentArcs[i].getTransition();
            
            graftArc = new DefaultArc( place,
                                       componentTransition );
            
            graftArc.setExpression( componentArcs[i].getExpression() );

            componentOut.removeInboundArc( componentArcs[i] );
            componentTransition.removeOutboundArc( componentArcs[i] );

            place.addInboundArc( graftArc );
            componentTransition.addOutboundArc( graftArc );
        }

        component.removePlace( componentOut.getId() );
    }

    /*
    void connectPlaceToIdiom(String placeId,
                             Idiom idiom)
    {
        DefaultPlace place = getMutablePlace( placeId );
        DefaultTransition transition = idiom.getMutableInTransition();

        DefaultArc arc = new DefaultArc( place,
                                         transition );

        place.addOutboundArc( arc );
        transition.addInboundArc( arc );
    }

    void connectIdiomToPlace(Idiom idiom,
                             String placeId)
    {
        DefaultPlace place = getMutablePlace( placeId );
        DefaultTransition transition = idiom.getMutableInTransition();

        DefaultArc arc = new DefaultArc( place,
                                         transition );

        transition.addOutboundArc( arc );
        place.addInboundArc( arc );
    }
    */

    void stash(String id,
               String value)
    {
        this.stashed.put( id,
                          value );
    }

    String getStashed(String id)
    {
        return (String) this.stashed.get( id );
    }

    public String toString()
    {
        return "[Idiom: " + getIdiomDefinition().getId() + "]";
    }
}
