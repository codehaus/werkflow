package com.werken.werkflow.bsf;

import com.werken.werkflow.Attributes;
import com.werken.werkflow.expr.ExpressionContext;

import org.apache.bsf.BSFManager;
import org.apache.bsf.BSFException;

import java.util.Map;
import java.util.Iterator;

public class BsfUtil
{
    public static void populate(BSFManager manager,
                                Map beans)
    {
        Iterator nameIter = beans.keySet().iterator();
        String   eachName = null;
        Object   value    = null;

        while ( nameIter.hasNext() )
        {
            eachName = (String) nameIter.next();
            value    = beans.get( eachName );

            try
            {
                if ( value != null )
                {
                    manager.declareBean( eachName,
                                         value,
                                         value.getClass() );
                }
                else
                {
                    manager.declareBean( eachName,
                                         null,
                                         Object.class );
                }
            }
            catch (BSFException e)
            {
                // swallow
            }
        }
    }

    public static void unpopulate(BSFManager manager,
                                  Map beans)
    {
        Iterator nameIter = beans.keySet().iterator();
        String   eachName = null;

        while ( nameIter.hasNext() )
        {
            eachName = (String) nameIter.next();

            try
            {
                manager.undeclareBean( eachName );
            }
            catch (BSFException e)
            {
                // swallow
            }
        }
    }

    public static void populate(BSFManager manager,
                                Attributes attributes)
    {
        String[] names = attributes.getAttributeNames();
        Object   value = null;

        for ( int i = 0 ; i < names.length ; ++i )
        {
            value = attributes.getAttribute( names[i] );

            try
            {
                if ( value != null )
                {
                    manager.declareBean( names[i],
                                         value,
                                         value.getClass() );
                }
                else
                {
                    manager.declareBean( names[i],
                                         null,
                                         Object.class );
                }
            }
            catch (BSFException e)
            {
                // swallow
            }
        }
    }

    public static void unpopulate(BSFManager manager,
                                  Attributes attributes)
    {
        String[] names = attributes.getAttributeNames();

        for ( int i = 0 ; i < names.length ; ++i )
        {
            try
            {
                manager.undeclareBean( names[i] );
            }
            catch (BSFException e)
            {
                // swallow
            }
        }
    }

    public static void populate(BSFManager manager,
                                ExpressionContext context)
    {
        String[] names = context.getNames();
        Object   value = null;

        for ( int i = 0 ; i < names.length ; ++i )
        {
            value = context.getValue( names[i] );

            try
            {
                if ( value != null )
                {
                    manager.declareBean( names[i],
                                         value,
                                         value.getClass() );
                }
                else
                {
                    manager.declareBean( names[i],
                                         null,
                                         Object.class );
                }
            }
            catch (BSFException e)
            {
                // swallow
            }
        }
    }

    public static void unpopulate(BSFManager manager,
                                  ExpressionContext context)
    {
        String[] names = context.getNames();

        for ( int i = 0 ; i < names.length ; ++i )
        {
            try
            {
                manager.undeclareBean( names[i] );
            }
            catch (BSFException e)
            {
                // swallow
            }
        }
    }
}
