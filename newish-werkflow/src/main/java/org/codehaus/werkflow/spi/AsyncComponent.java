package org.codehaus.werkflow.spi;

import org.codehaus.werkflow.Instance;

public interface AsyncComponent
    extends Component
{
    public static final Path NONE = new Path( new int[] { -1 } );
    public static final Path SELF = new Path( new int[] { -2 } );
    public static final Path DEFER = new Path( new int[] { -3 } );
    
    Path[] begin(Instance instance,
                 Path thisPath);
    
    Path endChild(Instance instance,
                  Path childPath);

    Component[] getChildren();
        
}
