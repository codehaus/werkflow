package com.werken.werkflow.bsf;

import com.werken.werkflow.expr.Expression;
import com.werken.werkflow.expr.AbstractExpression;
import com.werken.werkflow.expr.ExpressionContext;

import org.apache.bsf.BSFManager;
import org.apache.bsf.BSFEngine;
import org.apache.bsf.BSFException;
import org.apache.bsf.util.ObjectRegistry;

public abstract class BsfExpression
    extends AbstractExpression
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

    public Object evaluate(ExpressionContext context)
        throws Exception
    {
        Object result = null;

        ObjectRegistry registry = new BsfObjectRegistry();

        synchronized ( this.manager )
        {
            this.manager.setObjectRegistry( registry );

            BsfUtil.populate( this.manager,
                              context );

            result = this.engine.eval( text, 0, 0, text );
            
            BsfUtil.unpopulate( this.manager,
                                context );
            
        }

        return result;
    }

    public String toString()
    {
        return getText();
    }
}
