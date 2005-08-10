package org.codehaus.werkflow.helpers;

import java.util.Properties;

import org.codehaus.werkflow.simple.ActionManager;
import org.codehaus.werkflow.spi.Instance;

/*
 * $Id$
 */
public class MockActionManager implements ActionManager {

    public void perform( String actionId, Instance instance, Properties properties ) throws Exception {
    }

}
