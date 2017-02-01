package net.java.vll.vll4j.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import net.java.vll.vll4j.RichCharSequence;
import net.java.vll.vll4j.combinator.Reader;

public class ReaderFile
  extends Reader
{
  public ReaderFile(File theFile, boolean useRichCharSequence)
  {
    if (!theFile.exists())
      throw new IllegalArgumentException("Nonexistent file");
    BufferedReader br = null;
    FileReader fr = null;
    try 
    {
      fr = new FileReader(theFile);
      br = new BufferedReader(fr);
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = br.readLine()) != null)
        sb.append(line).append('\n');
      this.buffer = (useRichCharSequence ? new RichCharSequence(sb.toString()) : sb.toString()); return;
    } 
    catch (Exception ex) {}
    finally 
    {
      try 
      {
        fr.close(); 
      } 
      catch (IOException ex) {}
      try 
      { 
      	br.close();
      } 
      catch (IOException ex) {}
    }
  }
  
  private ReaderFile(CharSequence b) { this.buffer = b; }
  
  public CharSequence source() { return this.buffer; }
  
  public int offset() { return this.offset; }
  
  public boolean atEnd() { return this.offset >= this.buffer.length(); }
  
  public char first() { return this.buffer.charAt(this.offset); }
  
  public ReaderFile rest() { return drop(1); }
  
  public int line() { return this.line; }
  
  public int column() { return this.column; }
  
  public ReaderFile drop(int nbrToDrop) 
  {
    CharSequence src = this.buffer;
    if (this.offset + nbrToDrop > src.length())
      throw new IllegalArgumentException();
    ReaderFile csr = new ReaderFile(this.buffer);
    this.offset += nbrToDrop;csr.line = this.line;csr.column = this.column;
    for (int i = 0; i < nbrToDrop; i++) 
    {
      if (src.charAt(this.offset + i) == '\n') 
      {
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
  private CharSequence buffer;
}
