package com.werken.werkflow.service.persistence;

import com.werken.werkflow.ProcessInfo;
import com.werken.werkflow.admin.DeploymentException;

public interface PersistenceManager
{
    /** Component role. */
    public static final String ROLE = PersistenceManager.class.getName();

    /** Activate the {@link ProcessPersistenceManager} that will maintain
     * the {@link com.werken.werkflow.core.CaseState} information for the process.
     *
     * <p>
     * This method may be called for new processes and processes that have been activated before.
     * </p>
     *
     * <p>
     * A call to {@link passivate} must be made before another call to activate is made
     * for the same process. Implementations should throw a {@link DeploymentException}
     * if this rule is violated.
     * </p>
     *
     * @param processInfo The process to activate
     *
     * @return The ProcessPersistenceManager instance the engine should use to store case details.
     *
     * @throws DeploymentException if the PersistenceManager instance is not able to activate
     *     the process or the process is already active
     *
     * @see ProcessPersistenceManager
     * @see com.werken.werkflow.engine.WorkflowEngine
     */
    ProcessPersistenceManager activate(ProcessInfo processInfo) throws DeploymentException;


    /** The engine is no longer using the supplied {@link ProcessPersistenceManager}.
     *
     * <p>
     * The PersistenceManager is free to release any resources associated with the
     * {@link ProcessPersistenceManager}.
     * </p>
     *
     * <p>
     * If the supplied manager is not active or unknown to the PersistenceManager instance
     * then a {@link PersistenceException} will be thrown.
     * </p>
     *
     * @param manager The ProcessPersistenceManager to passivate
     *
     * @throws PersistenceException If the ProcessPersistenceManager is not active or unknown to the
     *     PersistenceManager.
     *
     * @see ProcessPersistenceManager
     * @see com.werken.werkflow.engine.WorkflowEngine
     */
    void passivate(ProcessPersistenceManager manager) throws PersistenceException;
}
