package net.java.vll.vll4j.combinator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class SimpleLexingParsers
  extends RegexParsers
{
  public void reset()
  {
    this.tokenLexerMap.clear();
    this.setupDone = false;
    this.theLiterals.clear();
    this.sortedLiterals = ((Object[][])null);
    this.literalsMatcher = null;
    this.theRegexs.clear();
    this.regexMatchers = null;
    this.tokenList.clear();
    this.lastTokenId = null;
  }
  
  public void resetWhitespace() {
    String ws = Utils.unEscape(this.whiteSpaceRegex);
    String cmts = Utils.unEscape(this.commentSpecRegex);
    if ((ws.isEmpty()) && (cmts.isEmpty())) {
      this.whiteSpace = Pattern.compile("");
    } else if (cmts.isEmpty()) {
      this.whiteSpace = Pattern.compile(ws);
    } else if (ws.isEmpty()) {
      this.whiteSpace = Pattern.compile(cmts);
    } else {
      String wsp = String.format("(?:(?:%s)|(?:%s))+", new Object[] { ws, cmts });
      this.whiteSpace = Pattern.compile(wsp);
    }
  }
  
  public void defineLiteral(String tokenName, String lit) {
    if (this.setupDone)
      throw new IllegalStateException();
    String litKey = "L" + Utils.escapeMetachars(lit);
    if (this.tokenLexerMap.containsKey(litKey)) {
      throw new IllegalArgumentException(String.format("Literal '%s' already defined", new Object[] { litKey.substring(1) }));
    }
    
    this.theLiterals.add(lit);
    int id = -this.theLiterals.size();
    this.tokenLexerMap.put(litKey, lexerById(id));
    this.tokenList.add(0, tokenName);
  }
  
  public void defineRegex(String tokenName, Pattern pat)
  {
    if (this.setupDone)
      throw new IllegalStateException();
    String regString = pat.toString();
    String regKey = "R" + regString;
    if (this.tokenLexerMap.containsKey(regKey)) {
      throw new IllegalArgumentException(String.format("Regex '%s' already defined", new Object[] { regKey.substring(1) }));
    }
    
    this.theRegexs.add(regString);
    int id = this.theRegexs.size() - 1;
    this.tokenLexerMap.put(regKey, lexerById(id));
    this.tokenList.add(tokenName);
  }
  
  public Parsers.Parser<CharSequence> literal2(String errMsg, String lit)
  {
    String litKey = "L" + Utils.escapeMetachars(lit);
    if (this.tokenLexerMap.containsKey(litKey)) {
      return lexer2parser((Lexer)this.tokenLexerMap.get(litKey), errMsg);
    }
    throw new IllegalArgumentException("Undefined literal");
  }
  

  public Parsers.Parser<CharSequence> regex2(String errMsg, Pattern pat)
  {
    String regString = pat.toString();
    String regKey = "R" + regString;
    if (this.tokenLexerMap.containsKey(regKey)) {
      return lexer2parser((Lexer)this.tokenLexerMap.get(regKey), errMsg);
    }
    throw new IllegalArgumentException("Undefined regex");
  }
  
  public Parsers.Parser<CharSequence> wildCard(String errMsg)
  {
    return lexer2parser(lexerById(Integer.MAX_VALUE), errMsg);
  }
  
  private Parsers.Parser<CharSequence> lexer2parser(final Lexer lexer, final String errMsg) {
 return new Parsers.Parser() {
      public Parsers.ParseResult<CharSequence> apply(Reader in) {
        try {
          Object[] lexRes = lexer.apply(in);
          if (lexRes == null) {
            return new Parsers.Failure(errMsg + SimpleLexingParsers.this.lastTokenId, in);
          }
          return new Parsers.Success((CharSequence)lexRes[0], in.drop(((Integer)lexRes[2]).intValue()));
        } catch (StackOverflowError soe) {
          throw new RuntimeException(String.format("Java bug 5050507 at (%d, %d)", new Object[] { Integer.valueOf(in.line()), Integer.valueOf(in.column()) }), soe);
        }
      }
    };   }
  
  private Lexer lexerById(final int id) {
 return new Lexer() {
      Object[] apply(Reader input) {
        if (!SimpleLexingParsers.this.setupDone) {
          SimpleLexingParsers.this.setupLexerLiterals();
          SimpleLexingParsers.this.setupLexerRegexs();
          SimpleLexingParsers.this.tokenArray = ((String[])SimpleLexingParsers.this.tokenList.toArray(new String[SimpleLexingParsers.this.tokenList.size()]));
          SimpleLexingParsers.this.setupDone = true;
        }
        SimpleLexingParsers.this.lastTokenId = "";
        Object[] litRes = SimpleLexingParsers.this.theLiterals.isEmpty() ? null : SimpleLexingParsers.this.lexKnownLiterals(input);
        Object[] regRes = SimpleLexingParsers.this.regexMatchers.length == 0 ? null : SimpleLexingParsers.this.lexKnownRegexs(input);
        CharSequence lit = litRes == null ? null : (CharSequence)litRes[0];
        CharSequence reg = regRes == null ? null : (CharSequence)regRes[0];
        
        if (lit == null) {
          if (reg == null) {
            return null;
          }
          int regId = ((Integer)regRes[1]).intValue();
          if ((regId == id) || (id == Integer.MAX_VALUE)) {
            return regRes;
          }
          SimpleLexingParsers.this.lastTokenId = SimpleLexingParsers.this.tokenArray[(regId + SimpleLexingParsers.this.sortedLiterals.length)];
          return null;
        }
        

        if (reg == null) {
          int litId = ((Integer)litRes[1]).intValue();
          if ((litId == id) || (id == Integer.MAX_VALUE)) {
            return litRes;
          }
          SimpleLexingParsers.this.lastTokenId = SimpleLexingParsers.this.tokenArray[(litId + SimpleLexingParsers.this.sortedLiterals.length)];
          return null;
        }
        
        if (lit.length() >= reg.length()) {
          int litId = ((Integer)litRes[1]).intValue();
          if (litId == id) {
            return litRes;
          }
          SimpleLexingParsers.this.lastTokenId = SimpleLexingParsers.this.tokenArray[(litId + SimpleLexingParsers.this.sortedLiterals.length)];
          return null;
        }
        
        int regId = ((Integer)regRes[1]).intValue();
        if (regId == id) {
          return regRes;
        }
        SimpleLexingParsers.this.lastTokenId = SimpleLexingParsers.this.tokenArray[(regId + SimpleLexingParsers.this.sortedLiterals.length)];
        return null;
      }
    };   }
  



  private Object[] lexKnownLiterals(Reader input)
  {
    int offset = input.offset();
    int offset2 = handleWhiteSpace(input.source(), offset);
    
    CharSequence cs = input.source().subSequence(offset2, input.source().length());
    










    int k = -1;int patLen = 0;
    String pattern = null;
    for (int i = 0; i < this.sortedLiterals.length; i++) {
      pattern = (String)this.sortedLiterals[i][0];
      patLen = pattern.length();
      if (cs.subSequence(0, Math.min(patLen, cs.length())).equals(pattern)) {
        k = i;
        break;
      }
    }
    if (k >= 0) {
      return new Object[] { pattern, (Integer)this.sortedLiterals[k][1], Integer.valueOf(offset2 - offset + patLen) };
    }
    











    return null;
  }
  
  private Object[] lexKnownRegexs(Reader input)
  {
    int offset = input.offset();
    int offset2 = handleWhiteSpace(input.source(), offset);
    CharSequence cs = input.source().subSequence(offset2, input.source().length());
    
    int idx = -1;int maxLength = -1;
    
    for (int i = 0; i < this.regexMatchers.length; i++) {
      Matcher m = this.regexMatchers[i].reset(cs);
      if ((m.lookingAt()) && (m.end() - m.start() > maxLength)) {
        maxLength = m.end() - m.start();
        idx = i;
      }
    }
    
    if (idx == -1) {
      return null;
    }
    return new Object[] { cs.subSequence(0, maxLength), Integer.valueOf(idx), Integer.valueOf(offset2 - offset + maxLength) };
  }
  
  private void setupLexerLiterals() {
    this.sortedLiterals = new Object[this.theLiterals.size()][];
    for (int i = 0; i < this.sortedLiterals.length; i++) {
      this.sortedLiterals[i] = new Object[2];
      this.sortedLiterals[i][0] = this.theLiterals.get(i);
      this.sortedLiterals[i][1] = Integer.valueOf((i + 1) * -1);
    }
    
    Arrays.sort(this.sortedLiterals, this.literalsComparator);
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (int i = 0; i < this.sortedLiterals.length; i++)
    {
      sb.append(Utils.escapeMetachars(Utils.unEscape((String)this.sortedLiterals[i][0])));
      if (i != this.sortedLiterals.length - 1)
        sb.append(")|(");
    }
    sb.append(")");
    String pattern = sb.toString();
    this.literalsMatcher = Pattern.compile(pattern).matcher("");
  }
  
  private void setupLexerRegexs()
  {
    this.regexMatchers = new Matcher[this.theRegexs.size()];
    for (int i = 0; i < this.regexMatchers.length; i++) {
      this.regexMatchers[i] = Pattern.compile((String)this.theRegexs.get(i)).matcher("");
    }
  }
  

  private String lastTokenId = null;
  public String whiteSpaceRegex = Utils.reEscape(this.whiteSpace.pattern());
  public String commentSpecRegex = "";
  private Map<String, Lexer> tokenLexerMap = new HashMap();
  private boolean setupDone = false;
  private List<String> theLiterals = new ArrayList();
  private Object[][] sortedLiterals = (Object[][])null;
  private Matcher literalsMatcher = null;
  private List<String> tokenList = new ArrayList();
  private String[] tokenArray = null;
  private List<String> theRegexs = new ArrayList();
  private Matcher[] regexMatchers = null;
  private Comparator literalsComparator = new Comparator()
  {
    public int compare(Object a, Object b) {
      String aa = (String)((Object[])(Object[])a)[0];
      String bb = (String)((Object[])(Object[])b)[0];
      return bb.compareTo(aa);
    }
  };
  private Comparator literalsComparator2 = new Comparator()
  {
    public int compare(Object a, Object b) {
      String aa = (String)((Object[])(Object[])a)[0];
      String bb = (String)b;
      return bb.substring(0, Math.min(aa.length(), bb.length())).compareTo(aa);
    }
  };
  
  private static abstract class Lexer
  {
    abstract Object[] apply(Reader paramReader);
  }
}
