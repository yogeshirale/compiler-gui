package net.java.vll.vll4j.gui;

import javax.swing.JTextArea;
import net.java.vll.vll4j.RichCharSequence;
import net.java.vll.vll4j.combinator.Reader;

public class ReaderTextArea
  extends Reader
{
  private boolean useRichCharSequence;
  private JTextArea textArea;
  
  public ReaderTextArea(JTextArea textArea, boolean useRichCharSequence)
  {
    this.textArea = textArea;
    this.useRichCharSequence = useRichCharSequence;
    this.source = (useRichCharSequence ? new RichCharSequence(textArea.getText()) : textArea.getText());
  }
  
  public CharSequence source() 
  {
    if (this.useRichCharSequence) 
    {
      if (this.source.equals(this.textArea.getText())) 
      {
        return this.source;
      }
      this.source = new RichCharSequence(this.textArea.getText());
      return this.source;
    }
    
    return this.textArea.getText();
  }
  
  public int offset() { return this.offset; }
  
  public boolean atEnd() { return this.offset >= this.textArea.getText().length(); }
  
  public char first() { return this.textArea.getText().charAt(this.offset); }
  
  public ReaderTextArea rest() { return drop(1); }
  
  public int line() { return this.line; }
  
  public int column() { return this.column; }
  
  public ReaderTextArea drop(int nbrToDrop) 
  {
    if (this.offset + nbrToDrop > this.textArea.getText().length())
      throw new IllegalArgumentException();
    ReaderTextArea csr = new ReaderTextArea(this.textArea, this.useRichCharSequence);
    this.offset += nbrToDrop;csr.line = this.line;csr.column = this.column;
    String src = this.textArea.getText();
    for (int i = 0; i < nbrToDrop; i++) 
    {
      if (src.charAt(this.offset + i) == '\n') 
      {
        csr.line += 1;
        csr.column = 1;
      } 
      else 
      {
        csr.column += 1;
      }
    }
    return csr;
  }
 
  private int offset = 0;
  private int line = 1; private int column = 1;
  private CharSequence source;
}
