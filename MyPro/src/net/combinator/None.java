package net.combinator;


class None<T>
  extends Option<T>
{
  public boolean isEmpty() { return true; }
  
  public T get() { throw new UnsupportedOperationException(); }
}

