package net.java.vll.vll4j.api;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import net.java.vll.vll4j.combinator.CharSequenceReader;
import net.java.vll.vll4j.combinator.PackratParsers;
import net.java.vll.vll4j.combinator.Parsers;
import net.java.vll.vll4j.combinator.Parsers.ParseResult;
import net.java.vll.vll4j.combinator.Parsers.Parser;
import net.java.vll.vll4j.combinator.Reader;
import net.java.vll.vll4j.combinator.Utils;
import net.java.vll.vll4j.gui.ReaderFile;
import org.xml.sax.SAXException;










public class Vll4j
  extends PackratParsers
{
  public static Vll4j fromStream(InputStream is)
    throws ParserConfigurationException, SAXException, IOException
  {
    Vll4j vll4j = new Vll4j();
    vll4j.initForest(is);
    return vll4j;
  }
  
  public static Vll4j fromString(String inString) throws ParserConfigurationException, SAXException, IOException
  {
    ByteArrayInputStream bais = new ByteArrayInputStream(inString.getBytes());
    Vll4j vll4j = new Vll4j();
    vll4j.initForest(bais);
    return vll4j;
  }
  
  public static Vll4j fromFile(File inFile) throws ParserConfigurationException, SAXException, IOException
  {
    FileInputStream fis = new FileInputStream(inFile);
    Vll4j vll4j = new Vll4j();
    vll4j.initForest(fis);
    return vll4j;
  }
  
  public <T> Parsers.Parser<T> getParserFor(String ruleName) throws ParserConfigurationException, SAXException, IOException
  {
    if (!this.forest.ruleBank.containsKey(ruleName))
      throw new IllegalArgumentException(String.format("unknown rule: %s", new Object[] { ruleName }));
    reset();
    VisitorParserGeneration vpg = new VisitorParserGeneration(this.forest, this, false);
    NodeBase top = (NodeBase)this.forest.ruleBank.get(ruleName);
    return (Parsers.Parser)top.accept(vpg);
  }
  
  public <T> Parsers.ParseResult<T> parseAll(Parsers.Parser<T> p, CharSequence cs)
  {
    this.forest.bindings.put("vllSource", cs);
    return phrase(p).apply(new CharSequenceReader(cs));
  }
  
  public <T> Parsers.ParseResult<T> parseAll(Parsers.Parser<T> p, Reader rdr)
  {
    this.forest.bindings.put("vllSource", rdr.source());
    return phrase(p).apply(rdr);
  }
  
  public static void main(String[] args) {
    if ((args.length != 3) && (args.length != 2)) {
      System.err.println("usage: java vll4j.Vll4j grammar [parser-name] file/data");
      System.exit(1);
    }
    try {
      FileInputStream fis = new FileInputStream(args[0]);
      Vll4j vll4j = fromStream(fis);
      Parsers.Parser<Object> p = vll4j.getParserFor(args.length == 2 ? "Main" : args[1]);
      File dataFile = new File(args[(args.length - 1)]);
      Parsers.ParseResult pr = dataFile.exists() ? vll4j.parseAll(p, new ReaderFile(dataFile, false)) : vll4j.parseAll(p, args[(args.length - 1)]);
      
      if (pr.successful()) {
        System.out.println(Utils.dumpValue(pr.get(), true));
      } else
        System.out.println(vll4j.dumpResult(pr));
    } catch (Exception e) {
      System.err.println(e);
    }
  }
  
  private void initForest(InputStream fis) throws ParserConfigurationException, SAXException, IOException
  {
    this.forest.openInputStream(fis, false);
    this.commentSpecRegex = this.forest.comment;
    this.whiteSpaceRegex = this.forest.whiteSpace;
    resetWhitespace();
  }
  
  private Forest forest = new Forest();
}
