package com.werken.werkflow.engine;

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

import com.werken.werkflow.activity.Activity;

import java.util.Map;

/** Internal <code>Activity</code> implementation.
 *
 *  @see ActivityManager
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public class WorkflowActivity
    implements Activity
{
    // ----------------------------------------------------------------------
    //     Instance members
    // ----------------------------------------------------------------------

    /** Activity-manager. */
    private ActivityManager activityManager;

    /** Case identifier. */
    private String caseId;

    /** Transition identifier. */
    private String transitionId;

    /** Identifiers of places with consumed marks. */
    private String[] placeIds;

    /** Case attributes. */
    private Map caseAttrs;

    // ----------------------------------------------------------------------
    //     Constructors
    // ----------------------------------------------------------------------

    /** Construct.
     *
     *  @param activityManager The activity manager.
     *  @param caseId The case identifier.
     *  @param transitionId The transition identifier.
     *  @param placeIds The identifiers of places of consumed marks.
     *  @param caseAttrs Case attributes.
     */
    public WorkflowActivity(ActivityManager activityManager,
                            String caseId,
                            String transitionId,
                            String[] placeIds,
                            Map caseAttrs)
    {
        this.activityManager = activityManager;
        this.caseId          = caseId;
        this.transitionId    = transitionId;
        this.placeIds        = placeIds;
        this.caseAttrs       = caseAttrs;
    }

    // ----------------------------------------------------------------------
    //     Instance methods
    // ----------------------------------------------------------------------

    /** Retrieve the <code>ActivityManager</code>.
     *
     *  @return The activity manager.
     */
    private ActivityManager getActivityManager()
    {
        return this.activityManager;
    }

    /** @see Activity
     */
    public String getCaseId()
    {
        return this.caseId;
    }

    /** @see Activity
     */
    public String getTransitionId()
    {
        return this.transitionId;
    }

    /** Retrieve the idetntifiers of places of consumed marks.
     *
     *  @return The identifiers of places of consume marks.
     */
    public String[] getPlaceIds()
    {
        return this.placeIds;
    }

    /** Retrieve the case attributes.
     *
     *  @return The case attributes.
     */
    public Map getCaseAttributes()
    {
        return this.caseAttrs;
    }

    /** @see Activity
     */
    public void complete()
    {
        getActivityManager().complete( this );
    }

    /** @see Activity
     */
    public void completeWithError(Throwable error)
    {
        getActivityManager().completeWithError( this,
                                                error );
    }
}
