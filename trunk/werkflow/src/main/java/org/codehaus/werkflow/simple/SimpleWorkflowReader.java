package org.codehaus.werkflow.simple;

import org.codehaus.werkflow.Workflow;
import org.codehaus.werkflow.idioms.*;
import org.codehaus.werkflow.idioms.interactive.*;
import org.codehaus.werkflow.spi.*;
import org.codehaus.werkflow.helpers.*;

import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Properties;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;

public class SimpleWorkflowReader
    extends DefaultHandler
{
    private ActionManager actionManager;
    private ExpressionFactory exprFactory;
    private Workflow workflow;
    private Locator locator;
    private LinkedList stack;

    public SimpleWorkflowReader(ActionManager actionManager,
                                ExpressionFactory exprFactory)
    {
        this.actionManager = actionManager;
        this.exprFactory   = exprFactory;
    }

    public void setDocumentLocator(Locator locator)
    {
        this.locator = locator;
    }

    public Workflow getWorkflow()
    {
        if ( stack.size() != 1 )
        {
            return null;
        }

        return (Workflow) stack.getFirst();
    }

    Expression newExpression(String text)
        throws SAXParseException
    {
        try
        {
            return this.exprFactory.newExpression( text );
        }
        catch (Exception e)
        {
            throw new SAXParseException( e.getMessage(),
                                         this.locator );
        }
    }

    public void startDocument()
        throws SAXException
    {
        this.stack = new LinkedList();
    }

    public void endDocument()
        throws SAXException
    {

    }

    public void startElement(String uri,
                             String localName,
                             String qname,
                             Attributes attrs)
        throws SAXException
    {
        //System.err.println( "startElement(" + localName + ")" );
        if ( ! uri.equals( "http://werkflow.codehaus.org/simple" ) )
        {
            throw new SAXParseException( "unsupported namespace: " + uri,
                                         this.locator );
        }

        if ( localName.equals( "workflow" ) )
        {
            startWorkflow( attrs );
        }
        else if ( localName.equals( "sequence" ) )
        {
            startSequence( attrs );
        }
        else if ( localName.equals( "parallel" ) )
        {
            startParallel( attrs );
        }
        else if ( localName.equals( "if" ) )
        {
            startIf( attrs );
        }
        else if ( localName.equals( "then" ) )
        {
            startThen( attrs );
        }
        else if ( localName.equals( "else" ) )
        {
            startElse( attrs );
        }
        else if ( localName.equals( "while" ) )
        {
            startWhile( attrs );
        }
        else if ( localName.equals( "task" ) )
        {
            startTask( attrs );
        }
        else if ( localName.equals( "satisfaction" ) )
        {
            startSatisfaction( attrs );
        }
        else if ( localName.equals( "action" ) )
        {
            startAction( attrs );
        }
        else if ( localName.equals( "choice" ) )
        {
            startChoice( attrs );
        }
        else if ( localName.equals( "option" ) )
        {
            startOption( attrs );
        }
        else
        {
            throw new SAXParseException( "Unknown element <" + localName + ">",
                                         this.locator );
        }
    }

    public void endElement(String uri,
                           String localName,
                           String qname)
        throws SAXException
    {
        if ( localName.equals( "workflow" ) )
        {
            endWorkflow();
        }
        else if ( localName.equals( "sequence" ) )
        {
            endSequence();
        }
        else if ( localName.equals( "parallel" ) )
        {
            endParallel();
        }
        else if ( localName.equals( "if" ) )
        {
            endIf();
        }
        else if ( localName.equals( "then" ) )
        {
            endThen();
        }
        else if ( localName.equals( "else" ) )
        {
            endElse();
        }
        else if ( localName.equals( "while" ) )
        {
            endWhile();
        }
        else if ( localName.equals( "task" ) )
        {
            endTask();
        }
        else if ( localName.equals( "satisfaction" ) )
        {
            endSatisfaction();
        }
        else if ( localName.equals( "action" ) )
        {
            endAction();
        }
        else if ( localName.equals( "choice" ) )
        {
            endChoice();
        }
        else if ( localName.equals( "option" ) )
        {
            endOption();
        }
        else
        {
            throw new SAXParseException( "Unknown element <" + localName + ">",
                                         this.locator );
        }
    }

    void startWorkflow(Attributes attrs)
        throws SAXException
    {
        if ( ! this.stack.isEmpty() )
        {
            throw new SAXParseException( "<workflow> can only be root element",
                                         this.locator );
        }

        String id = getRequiredAttribute( attrs,
                                          "workflow",
                                          "id" );

        push( new Workflow( id ) );
        push( new Sequence() );
    }

    void endWorkflow()
        throws SAXException
    {
        pop( "" );
        // DO NOT POP WORKFLOW
    }


    void startSequence(Attributes attrs)
        throws SAXException
    {
        push( new Sequence() );
    }

    void endSequence()
        throws SAXException
    {
        pop( "sequence" );
    }

    void startParallel(Attributes attrs)
        throws SAXException
    {
        push ( new Parallel() );
    }

    void endParallel()
        throws SAXException
    {
        pop( "parallel" );
    }

    void startIf(Attributes attrs)
        throws SAXException
    {
        String test = getRequiredAttribute( attrs,
                                            "if",
                                            "test" );

        Expression testExpr = newExpression( test );

        push( new IfElse( testExpr ) );
    }

    void endIf()
        throws SAXException
    {
        pop( "if" );
    }

    void startThen(Attributes attrs)
        throws SAXException
    {
        push( new Sequence() );
    }

    void endThen()
        throws SAXException
    {
        pop( "then" );
    }

    void startElse(Attributes attrs)
        throws SAXException
    {
        push ( new Sequence() );
    }

    void endElse()
        throws SAXException
    {
        pop( "else" );
    }

    void startWhile(Attributes attrs)
        throws SAXException
    {
        String test = getRequiredAttribute( attrs,
                                            "while",
                                            "test" );

        Expression testExpr = newExpression( test );

        push( new While( testExpr ) );
        push( new Sequence() );
    }

    void endWhile()
        throws SAXException
    {
        pop( "" );
        pop( "while" );
    }

    void startSatisfaction(Attributes attrs)
        throws SAXException
    {
        String id = getRequiredAttribute( attrs,
                                          "satisfaction",
                                          "id" );

        push( new SimpleSatisfaction( id ) );
    }

    void endSatisfaction()
        throws SAXException
    {
        pop( "satisfaction" );
    }

    void startAction(Attributes attrs)
        throws SAXException
    {
        String id = getRequiredAttribute( attrs,
                                          "action",
                                          "id" );

        int len = attrs.getLength();

        Properties properties = new Properties();

        for ( int i = 0 ; i < len ; ++i )
        {
            String name = attrs.getLocalName( i );

            if ( ! "id".equals( name ) )
            {
                String value = attrs.getValue( i );
                properties.setProperty( name,
                                        value );
            }

        }

        push( new Action( this.actionManager,
                          id,
                          properties ) );
    }

    void endAction()
        throws SAXException
    {
        pop( "action" );
    }

    void startTask(Attributes attrs)
        throws SAXException
    {
        String id = getRequiredAttribute( attrs,
                                          "task",
                                          "id" );

        String taskDescription = getRequiredAttribute( attrs,
                                          "task",
                                          "taskDescription" );

        String assignee = getRequiredAttribute( attrs,
                                          "task",
                                          "assignee" );

        push( new Task( id, taskDescription, assignee) );
    }

    void endTask()
        throws SAXException
    {
        pop( "task" );
    }

    void startChoice(Attributes attrs)
        throws SAXException
    {
        String id = getRequiredAttribute( attrs,
                                          "choice",
                                          "id" );

        push( new MultipleChoice( id ) );
    }

    void endChoice()
        throws SAXException
    {
        pop( "choice" );
    }

    void startOption(Attributes attrs)
        throws SAXException
    {
        String id = getRequiredAttribute( attrs,
                                          "option",
                                          "id" );

        push( new Choice( id ) );
        push( new Sequence() );
                                          
    }

    void endOption()
        throws SAXException
    {
        pop( "" );
        pop( "option" );
    }

    String getRequiredAttribute(Attributes attrs,
                                String element,
                                String name)
        throws SAXParseException
    {
        String value = attrs.getValue( name );

        if ( value == null
             ||
             value.trim().equals( "" ) )
        {
            throw new SAXParseException( "attribute '" + name + "' required on <" + element + ">",
                                         this.locator );
        }

        return value.trim();
    }

    long getAttribute(Attributes attrs,
                     String element,
                     String name,
                     long defaultValue)
        throws SAXParseException
    {
        String valueText = attrs.getValue( name );

        if ( valueText == null
             ||
             valueText.trim().equals( "" ) )
        {
            return defaultValue;
        }

        try
        {
            return Long.parseLong( valueText.trim() );
        }
        catch (NumberFormatException e)
        {
            throw new SAXParseException( e.getMessage(),
                                         this.locator );
        }
    }

    Component peek()
    {
        return (Component) this.stack.getLast();
    }

    void push(Component component)
    {
        this.stack.addLast( component );
    }

    void pop(String element)
        throws SAXParseException
    {
        Component head = (Component) this.stack.removeLast();

        Component parent = (Component) this.stack.getLast();

        if ( parent instanceof Workflow )
        {
            ((Workflow)parent).setBody( head );
        }
        else if ( parent instanceof MultipleChoice )
        {
            ((MultipleChoice)parent).addChoice( (Choice) head );
        }
        else if ( parent instanceof Sequence )
        {
            ((Sequence)parent).addStep( head );
        }
        else if ( parent instanceof Parallel )
        {
            ((Parallel)parent).addBranch( head );
        }
        else if ( parent instanceof IfElse )
        {
            if ( element.equals( "then" ) )
            {
                if ( ((IfElse)parent).getTrueBody() != null )
                {
                    throw new SAXParseException( "<then> may be specified at most once",
                                                 this.locator );
                }

                ((IfElse)parent).setTrueBody( head );
            }
            else if ( element.equals( "else" ) )
            {
                if ( ((IfElse)parent).getTrueBody() == null )
                {
                    throw new SAXParseException( "<then> must come before <else>",
                                                 this.locator );
                }
                if ( ((IfElse)parent).getFalseBody() != null )
                {
                    throw new SAXParseException( "<else> may be specified at most once",
                                                 this.locator );
                }

                ((IfElse)parent).setFalseBody( head );
            }
            else
            {
                throw new SAXParseException( "<if> child may be <then> or <else> not <" + element + ">",
                                             this.locator );
            }
        }
        else if ( parent instanceof While )
        {
            ((While)parent).setBody( head );
        }
        else if ( parent instanceof Choice )
        {
            ((Choice)parent).setBody( head );
        }
        /*
        else if ( parent instanceof SimpleSatisfaction )
        {
            // ((SimpleSatisfaction)parent).setSatisfier( head );

            //System.err.println( parent + " is parent of " + head );
        }
        */
        else
        {
            throw new SAXParseException( "unknown component type '" + parent.getClass().getName() + "'",
                                         this.locator );
        }
    }

    // ----------------------------------------------------------------------
    // ----------------------------------------------------------------------

    public static Workflow read(ActionManager actionManager,
                                ExpressionFactory exprFactory,
                                File in)
        throws IOException, SAXException, ParserConfigurationException
    {
        return read( actionManager,
                     exprFactory,
                     new InputSource( new FileInputStream( in ) ) );
    }

    public static Workflow read(ActionManager actionManager,
                                ExpressionFactory exprFactory,
                                InputStream in)
        throws IOException, SAXException, ParserConfigurationException
    {
        return read( actionManager,
                     exprFactory,
                     new InputSource( in ) );
    }

    public static Workflow read(ActionManager actionManager,
                                ExpressionFactory exprFactory,
                                URL in)
        throws IOException, SAXException, ParserConfigurationException
    {
        return read( actionManager,
                     exprFactory,
                     new InputSource( in.toExternalForm() ) );
    }

    public static Workflow read(ActionManager actionManager,
                                ExpressionFactory exprFactory,
                                InputSource in)
        throws IOException, SAXException, ParserConfigurationException
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        factory.setNamespaceAware( true );

        SAXParser parser = factory.newSAXParser();

        SimpleWorkflowReader reader = new SimpleWorkflowReader( actionManager,
                                                                exprFactory);

        parser.parse( in,
                      reader );

        return reader.getWorkflow();
    }
}
