package com.werken.werkflow.definition.petri;

import junit.framework.TestCase;

import java.util.Arrays;

public class IdiomDefinitionTest
    extends TestCase
{
    public void testConstruct()
    {
        IdiomDefinition idiomDef = new IdiomDefinition( "empty" );

        assertEquals( "empty",
                      idiomDef.getId() );

        assertEquals( 0,
                      idiomDef.getParameters().length );

        assertEquals( 0,
                      idiomDef.getPlaces().length );

        assertEquals( 0,
                      idiomDef.getTransitions().length );

        assertEquals( 0,
                      idiomDef.getArcs().length );
    }

    public void testStaticTransitions()
        throws Exception
    {
        IdiomDefinition idiomDef = new IdiomDefinition( "idiom" );

        TransitionDefinition transDef = new TransitionDefinition( "transition",
                                                                  "transition" );

        idiomDef.addTransition( transDef );

        assertEquals( 1,
                      idiomDef.getTransitions().length );

        assertEquals( transDef,
                      idiomDef.getTransitions()[0] );
    }

    public void testStaticPlaces()
        throws Exception
    {
        IdiomDefinition idiomDef = new IdiomDefinition( "idiom" );
        
        PlaceDefinition placeDef = new PlaceDefinition( "place",
                                                        "place" );

        idiomDef.addPlace( placeDef );

        assertEquals( 1,
                      idiomDef.getPlaces().length );

        assertEquals( placeDef,
                      idiomDef.getPlaces()[0] );
    }

    public void testStaticArcs()
        throws Exception
    {
        IdiomDefinition idiomDef = new IdiomDefinition( "idiom" );

        ArcDefinition arcDef = ArcDefinition.newArcFromPlaceToTransition( "my.place",
                                                                          "my.transition",
                                                                          "" );

        idiomDef.addArc( arcDef );

        assertEquals( 1,
                      idiomDef.getArcs().length );

        assertEquals( arcDef,
                      idiomDef.getArcs()[0] );
    }

    public void testStaticNewIdiom()
        throws Exception
    {
        IdiomDefinition idiomDef = new IdiomDefinition( "idiom" );

        PlaceDefinition placeDef = new PlaceDefinition( "my.place",
                                                        "place.docs" );

        idiomDef.addPlace( placeDef );

        TransitionDefinition transDef = new TransitionDefinition( "my.transition",
                                                                  "transition.docs" );

        idiomDef.addTransition( transDef );

        ArcDefinition arcDef = ArcDefinition.newArcFromPlaceToTransition( "my.place",
                                                                          "my.transition",
                                                                          "" );

        idiomDef.addArc( arcDef );

        Idiom idiom = idiomDef.newIdiom();

        idiom.build();

        assertEquals( 3,
                      idiom.getPlaces().length );

        assertContainsPlace( "in",
                             idiom.getPlaces() );

        assertContainsPlace( "out",
                             idiom.getPlaces() );

        assertContainsPlace( "/idiom:my.place",
                             idiom.getPlaces() );

        assertEquals( 1,
                      idiom.getTransitions().length );

        Place place = idiom.getPlace( "/idiom:my.place" );

        assertEquals( 1,
                      place.getArcsToTransitions().length );

        Transition transition = idiom.getTransitions()[0];

        assertEquals( "/idiom:my.transition",
                      transition.getId() );

        assertEquals( 1,
                      transition.getArcsFromPlaces().length );

        assertSame( place.getArcsToTransitions()[0],
                    transition.getArcsFromPlaces()[0] );
    }

    public void testGraft()
        throws Exception
    {
        IdiomDefinition parentDef = new IdiomDefinition( "parent" ); 

        parentDef.addPlace( new PlaceDefinition( "head",
                                                 "head" ) );

        parentDef.addPlace( new PlaceDefinition( "tail",
                                                 "tail" ) );

        parentDef.addTransition( new TransitionDefinition( "component",
                                                           "replace me" ) );

        parentDef.addArc( ArcDefinition.newArcFromPlaceToTransition( "head",
                                                                     "component",
                                                                     "" ) );

        parentDef.addArc( ArcDefinition.newArcFromTransitionToPlace( "component",
                                                                     "tail",
                                                                     "" ) );

        IdiomDefinition childDef = new IdiomDefinition( "child" ); 

        childDef.addTransition( new TransitionDefinition( "trans",
                                                          "trans" ) );

        childDef.addArc( ArcDefinition.newArcFromPlaceToTransition( "in",
                                                                    "trans",
                                                                    "" ) );

        childDef.addArc( ArcDefinition.newArcFromTransitionToPlace( "trans",
                                                                    "out",
                                                                    "" ) );
                         
        Idiom parentIdiom = parentDef.newIdiom();

        parentIdiom.build();

        Idiom childIdiom = parentIdiom.addComponent( childDef );

        childIdiom.build();

        Place place = null;
        Arc[] arcs = null;

        place = parentIdiom.getPlace( "/parent:head" );

        arcs = place.getArcsToTransitions();

        assertEquals( 1,
                      arcs.length );

        assertEquals( "/parent/child[0]:trans",
                      arcs[0].getTransition().getId() );

        place = parentIdiom.getPlace( "/parent:tail" );

        arcs = place.getArcsFromTransitions();

        assertEquals( 1,
                      arcs.length );

        assertEquals( "/parent/child[0]:trans",
                      arcs[0].getTransition().getId() );

        Transition trans = parentIdiom.getTransition( "/parent/child[0]:trans" );

        arcs = trans.getArcsFromPlaces();

        assertEquals( 1,
                      arcs.length );

        assertEquals( "/parent:head",
                      arcs[0].getPlace().getId() );

        arcs = trans.getArcsToPlaces();

        assertEquals( 1,
                      arcs.length );

        assertEquals( "/parent:tail",
                      arcs[0].getPlace().getId() );
    }

    protected void assertContainsPlace(String id,
                                       Place[] places)
    {
        for ( int i = 0 ; i < places.length ; ++i )
        {
            if ( places[i].getId().equals( id ) )
            {
                return;
            }
        }

        fail( "no place [" + id + "] in " + Arrays.asList( places ) );
    }
}
