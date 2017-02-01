package net.java.vll.vll4j.api;

import javax.script.ScriptException;
import net.java.vll.vll4j.combinator.Reader;

public abstract interface ActionFunction
{
  public abstract Object run(Object paramObject, Reader paramReader, int paramInt)
    throws ScriptException;
}

