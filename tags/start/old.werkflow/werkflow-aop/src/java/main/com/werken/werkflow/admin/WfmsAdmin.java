package com.werken.werkflow.admin;

import com.werken.werkflow.definition.ProcessDefinition;

/** The administrative interface to the workflow management system.
 *
 *  <p>
 *  The WFMS must be configured using the <code>WfmsAdmin</code> interface
 *  in order to make processes and services available to the client
 *  runtime.
 *  </p>
 *
 *  @see com.werken.werkflow.Wfms
 *  @see com.werken.werkflow.WfmsRuntime
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id$
 */
public interface WfmsAdmin
{
    void deployProcess(ProcessDefinition processDef)
        throws ProcessException;
}
