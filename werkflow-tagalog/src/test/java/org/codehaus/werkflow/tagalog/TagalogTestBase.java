package org.codehaus.werkflow.tagalog;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import org.codehaus.tagalog.ParseError;
import org.codehaus.tagalog.ParserConfiguration;
import org.codehaus.tagalog.TagalogParser;
import org.codehaus.tagalog.el.ParseController;
import org.codehaus.tagalog.el.ognl.OgnlExpressionParser;
import org.codehaus.tagalog.sax.TagalogSAXParserFactory;
import org.codehaus.tagalog.script.core.tags.CoreTagLibrary;
import org.codehaus.tagalog.script.tags.PITagLibrary;
import org.codehaus.tagalog.script.tags.ScriptTagLibrary;

import org.codehaus.werkflow.Workflow;
import org.codehaus.werkflow.simple.ActionManager;
import org.codehaus.werkflow.spi.Instance;
import org.codehaus.werkflow.tagalog.script.TagalogTagLibrary;

public class TagalogTestBase
    extends TestCase
    implements ActionManager
{
    private TagalogSAXParserFactory factory;

    protected void setUp()
        throws Exception
    {
        super.setUp();

        ParserConfiguration config = new ParserConfiguration();

        config.addTagLibrary( SimpleWerkflowTagLibrary.NS_URI,
                              new SimpleWerkflowTagLibrary() );
        config.addTagLibrary( TagalogTagLibrary.NS_URI,
                              new TagalogTagLibrary() );
        config.addTagLibrary( CoreTagLibrary.NS_URI,
                              new CoreTagLibrary() );

        config.setProcessingInstructionTagLibrary( new PITagLibrary() );

        factory = new TagalogSAXParserFactory(config);
    }

    protected Workflow parseWorkflow()
        throws Exception
    {
        return parseWorkflow( null );
    }

    protected Workflow parseWorkflow(String subName)
        throws Exception
    {
        TagalogParser parser = factory.createParser( getTestResource( subName ) );

        Map context = new HashMap();

        context.put( "actionManager",
                     this );

        ParseController expressionParser = (ParseController) ParseController.DEFAULT.clone();

        expressionParser.replaceExpressionLanguage( ParseController.STANDARD,
                                                    new OgnlExpressionParser() );

        context.put( ScriptTagLibrary.TAGALOG_EL_PARSER,
                     expressionParser );

        Object o = parser.parse( context );

        assertNotNull( o );

        ParseError[] errors = parser.parseErrors();

        if ( errors.length != 0 )
        {
            for ( int i = 0 ; i < errors.length ; i++ )
            {
                System.err.println( errors[i] );
            }
        }

        assertEquals( 0,
                      parser.parseErrors().length );

        return (Workflow) o;
    }

    private InputStream getTestResource(String subName)
    {
        String className = getClass().getName();

        String base = className.substring( className.lastIndexOf( "." ) + 1 );

        String config;

        if ( subName == null
             ||
             subName.equals( "" ) )
        {
            config = base + ".xml";
        }
        else
        {
            config = base + "-" + subName + ".xml";
        }

        return getClass().getResourceAsStream( config );
    }

    public void perform(String actionId,
                        Instance instance,
                        Properties properties)
        throws Exception
    {
    }
}
