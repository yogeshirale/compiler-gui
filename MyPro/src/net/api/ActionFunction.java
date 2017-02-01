package net.api;

import javax.script.ScriptException;
import net.combinator.Reader;

public abstract interface ActionFunction
{
  public abstract Object run(Object paramObject, Reader paramReader, int paramInt)
    throws ScriptException;
}

