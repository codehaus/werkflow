package org.codehaus.werkflow.simple;

import org.codehaus.werkflow.*;

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
import java.util.LinkedList;

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
        else if ( localName.equals( "satisfaction" ) )
        {
            startSatisfaction( attrs );
        }
        else if ( localName.equals( "action" ) )
        {
            startAction( attrs );
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
        else if ( localName.equals( "satisfaction" ) )
        {
            endSatisfaction();
        }
        else if ( localName.equals( "action" ) )
        {
            endAction();
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

        long interval = getAttribute( attrs,
                                      "satisfaction",
                                      "interval",
                                      -1L );
        if ( interval > 0 )
        {
            push( new PolledSatisfaction( id,
                                          interval ) );
            push( new Sequence() );
        }
        else
        {
            push( new Satisfaction( id ) );
            push( new Sequence() );
        }
    }

    void endSatisfaction()
        throws SAXException
    {
        pop( "" );
        pop( "satisfaction" );
    }

    void startAction(Attributes attrs)
        throws SAXException
    {
        String id = getRequiredAttribute( attrs,
                                          "action",
                                          "id" );

        push( new Action( this.actionManager,
                          id ) );
    }

    void endAction()
        throws SAXException
    {
        pop( "action" );
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
        else if ( parent instanceof Sequence )
        {

        }
        else if ( parent instanceof Parallel )
        {

        }
        if ( parent instanceof IfElse )
        {
            if ( element.equals( "then" ) )
            {
                ((IfElse)parent).setTrueBody( head );
            }
            else if ( element.equals( "else" ) )
            {
                ((IfElse)parent).setFalseBody( head );
            }
            else
            {
                throw new SAXParseException( "<if> child may be <then> and <else>",
                                             this.locator );
            }
        }
        else if ( parent instanceof While )
        {
            ((While)parent).setBody( head );
        }
        else if ( parent instanceof Satisfaction )
        {
            ((Satisfaction)parent).setSatisfier( head );
        }
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
