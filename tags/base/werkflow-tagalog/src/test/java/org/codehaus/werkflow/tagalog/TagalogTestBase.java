package org.codehaus.werkflow.tagalog;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import org.codehaus.tagalog.ParserConfiguration;
import org.codehaus.tagalog.TagalogParser;
import org.codehaus.tagalog.sax.TagalogSAXParserFactory;
import org.codehaus.werkflow.Workflow;
import org.codehaus.werkflow.simple.ActionManager;
import org.codehaus.werkflow.spi.Instance;

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

        Object o = parser.parse( context );

        assertNotNull( o );

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
