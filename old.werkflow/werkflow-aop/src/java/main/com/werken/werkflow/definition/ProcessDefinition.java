package com.werken.werkflow.definition;

/*
 $Id$

 Copyright 2003 (C) The Werken Company. All Rights Reserved.
 
 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.
 
 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.
 
 3. The name "werkflow" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.
 
 4. Products derived from this Software may not be called "werkflow"
    nor may "werkflow" appear in their names without prior written
    permission of The Werken Company. "werkflow" is a registered
    trademark of The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://werkflow.werken.com/).
 
 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.
 
 */

import com.werken.werkflow.definition.petri.Net;

import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.AttributeDeclaration;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/** Definition of a process.
 *
 *  @see Net
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class ProcessDefinition
    implements ProcessInfo
{
    // ----------------------------------------------------------------------
    //     Constants
    // ----------------------------------------------------------------------

    /** Empty <code>ProcessDefinition</code> array. */
    public static final ProcessDefinition[] EMPTY_ARRAY = new ProcessDefinition[0];

    /** Empty <code>String</code> array. */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static final class InitiationType
    {
        public static final InitiationType MESSAGE = new InitiationType();
        public static final InitiationType CALL    = new InitiationType();

        private InitiationType()
        {

        }
    }

    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Package identifier. */
    private String packageId;

    /** Process identifier. */
    private String id;

    /** Documentation, possibly null. */
    private String documentation;

    /** Petri-net structure. */
    private Net net;

    private Map attrDecls;

    private String[] inParameters;

    private String[] outParameters;

    private InitiationType initiationType;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    public ProcessDefinition(String packageId,
                             String id,
                             InitiationType initiationType)
    {
        this( packageId,
              id,
              null,
              initiationType );
    }

    public ProcessDefinition(String packageId,
                             String id,
                             Net net,
                             InitiationType initiationType)
    {
        this.packageId = packageId;
        this.id  = id;
        this.net = net;

        this.attrDecls = new HashMap();

        this.initiationType = initiationType;
        this.inParameters   = EMPTY_STRING_ARRAY;
        this.outParameters  = EMPTY_STRING_ARRAY;
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    public void setNet(Net net)
    {
        this.net = net;
    }

    public InitiationType getInitiationType()
    {
        return this.initiationType;
    }

    public boolean isCallable()
    {
        return this.initiationType == InitiationType.CALL;
    }

    public String getPackageId()
    {
        return this.packageId;
    }

    /** @see ProcessInfo
     */
    public String getId()
    {
        return this.id;
    }

    /** Set the documentation.
     *
     *  @param documentation The documentation.
     */
    public void setDocumentation(String documentation)
    {
        this.documentation = documentation;
    }

    /** @see ProcessInfo
     */
    public String getDocumentation()
    {
        return this.documentation;
    }
    
    /** Retrieve the Petri <code>Net</code> structure.
     *
     *  @return The Petri net.
     */
    public Net getNet()
    {
        return this.net;
    }

    public void addAttributeDeclaration(AttributeDeclaration attrDecl)
    {
        this.attrDecls.put( attrDecl.getId(),
                            attrDecl );
    }

    public AttributeDeclaration[] getAttributeDeclarations()
    {
        return (AttributeDeclaration[]) this.attrDecls.values().toArray( AttributeDeclaration.EMPTY_ARRAY );
    }

    public AttributeDeclaration getAttributeDeclaration(String id)
    {
        return (AttributeDeclaration) this.attrDecls.get( id );
    }

    public AttributeDeclaration[] getInParameters()
    {
        Set decls = new HashSet();

        Iterator declIter = this.attrDecls.values().iterator();
        AttributeDeclaration eachDecl = null;

        while ( declIter.hasNext() )
        {
            eachDecl = (AttributeDeclaration) declIter.next();

            if ( eachDecl.isIn() )
            {
                decls.add( eachDecl );
            }
        }
                
        return (AttributeDeclaration[]) decls.toArray( AttributeDeclaration.EMPTY_ARRAY );
    }

    public AttributeDeclaration[] getOutParameters()
    {
        Set decls = new HashSet();

        Iterator declIter = this.attrDecls.values().iterator();
        AttributeDeclaration eachDecl = null;

        while ( declIter.hasNext() )
        {
            eachDecl = (AttributeDeclaration) declIter.next();

            if ( eachDecl.isOut() )
            {
                decls.add( eachDecl );
            }
        }
                
        return (AttributeDeclaration[]) decls.toArray( AttributeDeclaration.EMPTY_ARRAY );
    }
}
