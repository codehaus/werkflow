package com.werken.werkflow.bsf;

import com.werken.werkflow.expr.Expression;
import com.werken.werkflow.expr.ExpressionContext;

import org.apache.bsf.BSFManager;
import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFException;
import org.apache.bsf.util.ObjectRegistry;

public abstract class BsfExpression
    implements Expression
{

    private String language;
    private String text;

    private BSFManager manager;
    private BSFEngine engine;
    
    public BsfExpression(String language,
                         String text)
        throws BSFException
    {
        this.language = language;
        this.text     = text;

        this.manager  = new BSFManager();
        this.engine   = this.manager.loadScriptingEngine( language );
    }

    public String getLanguage()
    {
        return this.language;
    }

    public String getText()
    {
        return this.text;
    }

    protected BSFManager getBSFManager()
    {
        return this.manager;
    }

    protected BSFEngine getBSFEngine()
    {
        return this.engine;
    }

    public boolean evaluateAsBoolean(ExpressionContext context)
        throws Exception
    {
        ObjectRegistry registry = new BsfObjectRegistry();

        synchronized ( this.manager )
        {
            this.manager.setObjectRegistry( registry );

            BsfUtil.populate( this.manager,
                              context );

            boolean result = asBoolean( this.engine.eval( text, 0, 0, text ) );

            BsfUtil.unpopulate( this.manager,
                                context );

            return result;
        }
    }

    public abstract boolean asBoolean(Object value);

    public boolean stringAsBoolean(String str)
    {
        if ( str.equals( "true" )
             ||
             str.equals( "on" )
             ||
             str.equals( "1" )
             ||
             str.equals( "yes" ) )
        {
            return true;
        }

        return false;
    }

    public String toString()
    {
        return getText();
    }
}
