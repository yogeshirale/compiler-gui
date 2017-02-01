package net.combinator;

class Some<T>
  extends Option<T>
{
  private T t;

  public Some(T t) { this.t = t; }
  
  public boolean isEmpty() { return false; }
  
  public T get() { return (T)this.t; }
}
