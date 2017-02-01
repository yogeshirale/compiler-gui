package net.combinator;


public class CharSequenceReader
  extends Reader
{
  private CharSequence source;

  public CharSequenceReader(CharSequence source, int offset)
  {
    assert (offset >= 0);
    this.source = source;
    this.offset = offset;
  }
  
  public CharSequenceReader(CharSequence source) {
    this.source = source;
    this.offset = 0;
  }
  
  public CharSequence source()
  {
    return this.source;
  }
  
  public int offset()
  {
    return this.offset;
  }
  
  public boolean atEnd()
  {
    return this.offset >= this.source.length();
  }
  
  public char first()
  {
    return this.offset < this.source.length() ? this.source.charAt(this.offset) : EofCh;
  }
  
  public Reader rest()
  {
    return drop(1);
  }
  
  public int line()
  {
    return this.line;
  }
  
  public int column()
  {
    return this.column;
  }
  
  public CharSequenceReader drop(int nbrToDrop)
  {
    if (this.offset + nbrToDrop > this.source.length()) {
      throw new IllegalArgumentException();
    }
    CharSequenceReader csr = new CharSequenceReader(this.source);
    csr.line = this.line;
    csr.column = this.column;
    this.offset += nbrToDrop;
    for (int i = 0; i < nbrToDrop; i++) {
      if (this.source.charAt(this.offset + i) == '\n') {
        csr.line += 1;
        csr.column = 1;
      } else {
        csr.column += 1;
      }
    }
    return csr;
  }
  

  private int offset = 0;
  private int line = 1; private int column = 1;
  private static char EofCh = '\032';
}
