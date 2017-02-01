 package net.java.vll.vll4j.combinator;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;




public class RegexParsers
  extends Parsers
{
  protected Pattern whiteSpace = Pattern.compile("\\s+");
  
  public boolean skipWhitespace() {
    return this.whiteSpace.toString().length() > 0;
  }
  
  protected int handleWhiteSpace(CharSequence source, int offset) {
    if (skipWhitespace()) {
      Matcher m = this.whiteSpace.matcher(source.subSequence(offset, source.length()));
      if (m.lookingAt()) {
        return offset + m.end();
      }
      return offset;
    }
    return offset;
  }
  
  public Parsers.Parser<CharSequence> literal(String lit)
  {
    return literal(String.format("expected literal(%s)", new Object[] { lit }), lit);
  }
  
  private boolean startsWith(CharSequence cs, String t) {
    if (t.length() > cs.length())
      return false;
    for (int i = 0; i < t.length(); i++)
      if (cs.charAt(i) != t.charAt(i))
        return false;
    return true;
  }
  
  public Parsers.Parser<CharSequence> literal(final String errMsg, final String lit) {
return new Parsers.Parser()
    {
      public Parsers.ParseResult<CharSequence> apply(Reader input) {
        int offset2 = RegexParsers.this.handleWhiteSpace(input.source(), input.offset());
        CharSequence cs = input.source();
        if (RegexParsers.this.startsWith(cs.subSequence(offset2, cs.length()), lit)) {
          return new Parsers.Success(lit, input.drop(offset2 - input.offset() + lit.length()));
        }
        return new Parsers.Failure(errMsg, input);
      }
    };   }
  
  public Parsers.Parser<CharSequence> regex(Pattern p) {
    return regex(String.format("expected regex(%s)", new Object[] { p.toString() }), p);
  }
  
  public Parsers.Parser<CharSequence> regex(final String errMsg, final Pattern p) {
return new Parsers.Parser()
    {
      public Parsers.ParseResult<CharSequence> apply(Reader input) {
        int offset2 = RegexParsers.this.handleWhiteSpace(input.source(), input.offset());
        if ((p.toString().equals("\\\\z")) && (offset2 >= input.source().length())) {
          return new Parsers.Success("", input.drop(offset2 - input.offset()));
        }
        CharSequence cs = input.source();
        Matcher m = p.matcher(cs.subSequence(offset2, cs.length()));
        if (m.lookingAt()) {
          return new Parsers.Success(m.group(), input.drop(offset2 - input.offset() + m.group().length()));
        }
        return new Parsers.Failure(errMsg, input);
      }
    };   }
  

  public <T> Parsers.Parser<T> phrase(final Parsers.Parser<T> p)
  {
return new Parsers.Parser()
    {
      public Parsers.ParseResult<T> apply(Reader input) {
        Parsers.ParseResult<T> pr = p.apply(input);
        
        if (!pr.successful())
          return new Parsers.Failure("phrase", input, (Parsers.NoSuccess)pr);
        if (pr.next().atEnd())
          return pr;
        int step = RegexParsers.this.handleWhiteSpace(pr.next().source(), pr.next().offset());
        if (pr.next().drop(step - pr.next().offset()).atEnd()) {
          return pr;
        }
        return new Parsers.Failure(String.format("expected end of input at [%d, %d]", new Object[] { Integer.valueOf(pr.next().line()), Integer.valueOf(pr.next().column()) }), input);
      }
    };   }
  
  public <T> Parsers.ParseResult<T> parseAll(Parsers.Parser<T> p, CharSequence cs)
  {
    return phrase(p).apply(new CharSequenceReader(cs));
  }
  
  public <T> Parsers.ParseResult<T> parseAll(Parsers.Parser<T> p, Reader rdr) {
    return phrase(p).apply(rdr);
  }
}

