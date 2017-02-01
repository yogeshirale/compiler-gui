package net.java.vll.vll4j.combinator;

public abstract class Option<T>
{
  public abstract T get();
  
  public abstract boolean isEmpty();
}

