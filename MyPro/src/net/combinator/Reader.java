package net.combinator;

public abstract class Reader
{
  public CharSequence source()
  {
    throw new NoSuchMethodError("not a char sequence reader");
  }
  
  public abstract boolean atEnd();
  
  public abstract char first();
  
  public abstract Reader rest();
  
  public int offset() {
    throw new NoSuchMethodError("not a char sequence reader");
  }
  
  public abstract int line();
  
  public abstract int column();
  
  public Reader drop(int n) {
    Reader r = this;
    for (int cnt = n; 
        cnt > 0; 
        cnt--) { r = r.rest();
    }
    return r;
  }
}