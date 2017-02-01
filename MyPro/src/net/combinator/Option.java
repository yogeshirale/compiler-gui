package net.combinator;

public abstract class Option<T>
{
  public abstract T get();
  
  public abstract boolean isEmpty();
}

