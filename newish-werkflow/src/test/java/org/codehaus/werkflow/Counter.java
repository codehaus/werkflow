package org.codehaus.werkflow;

public class Counter
    implements Expression
{
    private int num;
    private int counter;

    public Counter(int num)
    {
        this.num = num;
    }

    public boolean evaluate(Context context)
    {
        if ( this.counter < num )
        {
            ++this.counter;
            return true;
        }

        return false;
    }
}
