package com.werken.werkflow.definition.petri;

public class ArcDefinition
{
    public static final ArcDefinition[] EMPTY_ARRAY = new ArcDefinition[0];

    public static final short PLACE_TO_TRANSITION_TYPE = 1;
    public static final short TRANSITION_TO_PLACE_TYPE = 2;

    private short type;
    private String placeId;
    private String transitionId;
    private String expression;

    protected ArcDefinition(short type,
                            String placeId,
                            String transitionId,
                            String expression)
    {
        this.type         = type;
        this.placeId      = placeId;
        this.transitionId = transitionId;
        this.expression   = expression;
    }

    public short getType()
    {
        return this.type;
    }

    public String getPlaceId()
    {
        return this.placeId;
    }

    public String getTransitionId()
    {
        return this.transitionId;
    }

    public String getExpression()
    {
        return this.expression;
    }

    public boolean equals(Object thatObj)
    {
        if ( thatObj instanceof ArcDefinition )
        {
            ArcDefinition that = (ArcDefinition) thatObj;

            return ( getType() == that.getType()
                     &&
                     getPlaceId().equals( that.getPlaceId() )
                     &&
                     getTransitionId().equals( that.getTransitionId() ) );
        }

        return false;
    }

    public int hashCode()
    {
        return ( getPlaceId().hashCode() / 2 ) + ( getTransitionId().hashCode() / 2 ) + getType();
    }

    public static ArcDefinition newArcFromPlaceToTransition(String placeId,
                                                            String transitionId,
                                                            String expression)
    {
        return new ArcDefinition( PLACE_TO_TRANSITION_TYPE,
                                  placeId,
                                  transitionId,
                                  expression );

    }

    public static ArcDefinition newArcFromTransitionToPlace(String transitionId,
                                                            String placeId,
                                                            String expression)
    {
        return new ArcDefinition( TRANSITION_TO_PLACE_TYPE,
                                  placeId,
                                  transitionId,
                                  expression );
    }

    public String toString()
    {
        if ( this.type == TRANSITION_TO_PLACE_TYPE )
        {
            return "[ArcDefinition: transition=" + this.transitionId + " place=" + this.placeId + "]";
        }
        else
        {
            return "[ArcDefinition: place=" + this.placeId + " transition=" + this.transitionId + "]";
        }
    }
}
