package com.werken.werkflow.service.caserepo;

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

/** State momento of a process case.
 *
 *  <p>
 *  Responsible for collaborating with a matching <code>CaseRepository</code>
 *  which acts as a factory, <code>CaseState</code> captures the state of
 *  a process case.
 *  </p>
 *
 *  @see CaseRepository#newCaseState
 *  @see CaseRepository#getCaseState
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public interface CaseState
{
    /** Empty <code>CaseState</code> array. */
    static final CaseState[] EMPTY_ARRAY = new CaseState[0];

    /** Retrieve the identifier of the case.
     *
     *  @return The case identifier.
     */
    String getCaseId();

    String getPackageId();

    /** Retrieve the identifier of the process of which this
     *  state is an instance.
     *
     *  @return The process identifier.
     */
    String getProcessId();

    /** Set an attribute.
     *
     *  @param key The attribute key.
     *  @param value The attribute value.
     */
    void setAttribute(String key,
                      Object value);

    /** Clear an attribute.
     *
     *  @param key The attribute key.
     */
    void clearAttribute(String key);

    /** Retrieve an attribute.
     *
     *  @param key The attribute key.
     *
     *  @return The attribute value or <code>null</code> if the
     *          attribute has not been assigned a value.
     */
    Object getAttribute(String key);

    /** Retrieve the names of all set attributes.
     * 
     *  <p>
     *  Attributes set to explicitly set to <code>null</code> are
     *  included in the list of set attribute names.
     *  </p>
     *
     *  @see #hasAttribute
     *
     *  @return The the names of all set attributes.
     */
    String[] getAttributeNames();

    /** Determine if an attribute has been set.
     *
     *  <p>
     *  This will return <code>true</code> even if the attribute
     *  has been explicitly set to <code>null</code> using
     *  {@link #setAttribute}.
     *  </p>
     *
     *  @see #setAttribute
     *  @see #getAttributeNames
     *
     *  @param key The attribute key.
     *
     *  @return <code>true</code> if the attribute has been set to
     *          any value, otherwise <code>false</code>.
     */
    boolean hasAttribute(String key);
    
    /** Add a mark to a place.
     *
     *  @param placeId The identifier of the place to mark.
     */
    void addMark(String placeId);

    /** Remove a mark from a place.
     *
     *  @param placeId The identifier of the place to unmark.
     */
    void removeMark(String placeId);

    /** Retrieve the identifiers of all marked places.
     *
     *  @return The identifiers of all marked places.
     */
    String[] getMarks();

    /** Determine if a place contains a mark.
     *
     *  @param placeId The identifier of the place.
     *
     *  @return <code>true</code> if the place contains a mark, otherwise
     *          <code>false</code>.
     */
    boolean hasMark(String placeId);

    /** Store the current state persistently
     */
    void store();
}
