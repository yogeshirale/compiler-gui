package net.api;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import javax.script.ScriptException;
import net.combinator.PackratParsers;
import net.combinator.Parsers;
import net.combinator.Parsers.Failure;
import net.combinator.Parsers.ParseResult;
import net.combinator.Parsers.Parser;
import net.combinator.Parsers.Success;
import net.combinator.Reader;
import net.combinator.Utils;



public class VisitorParserGeneration
  extends VisitorBase
{
  private PackratParsers parsersInstance;
  private boolean traceAll;
  
  public VisitorParserGeneration(Forest theForest, PackratParsers parsersInstance, boolean traceAll, boolean[] stopFlag)
  {
    parsersInstance.reset();
    this.theForest = theForest;
    this.parsersInstance = parsersInstance;
    this.traceAll = traceAll;
    createTokenParsers();
    this.visitorNodeValidation = new VisitorValidation();
    this.parserGeneratedOk = true;
    this.stopFlag = stopFlag;
  }
  
  public VisitorParserGeneration(Forest theForest, PackratParsers parsersInstance, boolean traceAll) {
    this(theForest, parsersInstance, traceAll, new boolean[] { Boolean.FALSE.booleanValue() });
  }
  





  private void createTokenParsers()
  {
    for (Map.Entry<String, String> e : this.theForest.tokenBank.entrySet())
      if (!((String)e.getKey()).endsWith("_"))
      {
        String tokenName = (String)e.getKey();
        String pat = (String)e.getValue();
        if (pat.startsWith("L")) {
          this.parsersInstance.defineLiteral(tokenName, Utils.unEscape(pat.substring(1)));
        } else if (pat.startsWith("R")) {
          this.parsersInstance.defineRegex(tokenName, Pattern.compile(Utils.unEscape(pat.substring(1))));
        } else
          throw new IllegalArgumentException("Bad token");
      }
  }
  
  private void traceIndent() {
    for (int i = 0; i < this.traceLevel; i++) {
      System.out.print("|  ");
    }
  }
  
  Parsers.Parser<? extends Object> withAction(final Parsers.Parser<? extends Object> p, final NodeBase node) {
    if ((!(node instanceof NodeSemPred)) && (node.actionFunction != null)) {
      Parsers.Parser<? extends Object> pm = new Parsers.Parser()
      {
        public Parsers.ParseResult<? extends Object> apply(Reader input) {
          try {
            node.actionFunction.run(null, input, input.source().length());
            Parsers.ParseResult<? extends Object> res = p.apply(input);
            if (res.successful()) {
              int postWhitespace = res.next().offset() == input.offset() ? input.offset() : VisitorParserGeneration.this.parsersInstance.handleWhiteSpace(input.source(), input.offset());
              
              Reader forAction = input.drop(postWhitespace - input.offset());
              Object r2 = node.actionFunction.run(res.get(), forAction, res.next().offset());
              return new Parsers.Success(r2, res.next());
            }
            return res;
          } catch (ScriptException exc) {
            throw new IllegalArgumentException(String.format("Error in action-code @ %s", new Object[] { node.nodeName() }), exc);
          } catch (IllegalArgumentException exc) {
            throw new IllegalArgumentException(String.format("Error in 'withAction' @ %s", new Object[] { node.nodeName() }), exc);
          }
        }
      };
      return pm;
    }
    return p;
  }
  
  Parsers.Parser<? extends Object> withTrace(final Parsers.Parser<? extends Object> p, final NodeBase node)
  {
    if ((node.isTraced) || (((node instanceof NodeRoot)) && (this.traceAll))) {
      Parsers.Parser<? extends Object> pm = new Parsers.Parser()
      {
        public Parsers.ParseResult<? extends Object> apply(Reader input) {
          VisitorParserGeneration.this.traceIndent();
          System.out.print(String.format(">> %s (line=%d, col=%d)%n", new Object[] { node.nodeName(), Integer.valueOf(input.line()), Integer.valueOf(input.column()) }));
         // VisitorParserGeneration.access$204(VisitorParserGeneration.this);////////////////////////////////////////////////////////////////////////////////////////////////
          int postWhitespace = VisitorParserGeneration.this.parsersInstance.handleWhiteSpace(input.source(), input.offset());
          String sample = Utils.reEscape(input.source().subSequence(postWhitespace, Math.min(input.source().length(), postWhitespace + 20)).toString());
          
          Parsers.ParseResult<? extends Object> res = p.apply(input);
        //  VisitorParserGeneration.access$206(VisitorParserGeneration.this);//////////////////////////////////////////////////////////////////////////////////////////////////
          VisitorParserGeneration.this.traceIndent();
          System.out.print(String.format("<< %s: %s (line=%d, col=%d, input=%s)%n", new Object[] { node.nodeName(), res.getClass().getSimpleName(), Integer.valueOf(res.next().line()), Integer.valueOf(res.next().column()), sample }));
          
          return res;
        }
      };
      return withAction(pm, node);
    }
    return withAction(p, node);
  }
  
  Parsers.Parser<? extends Object> withMultiplicity(Parsers.Parser<? extends Object> p, NodeBase node)
  {
    Parsers.Parser<? extends Object> pm = p;
    if (node.multiplicity == Multiplicity.ZeroOrMore) {
      pm = this.parsersInstance.rep(p);
    } else if (node.multiplicity == Multiplicity.OneOrMore) {
      pm = this.parsersInstance.rep1(String.format("rep1(%s)", new Object[] { node.nodeName() }), p);
    } else if (node.multiplicity == Multiplicity.ZeroOrOne) {
      pm = this.parsersInstance.opt(p);
    } else if (node.multiplicity == Multiplicity.Not) {
      pm = this.parsersInstance.not(p);
    } else if (node.multiplicity == Multiplicity.Guard) {
      pm = this.parsersInstance.guard(p);
    }
    return withTrace(pm, node);
  }
  
  Parsers.Parser<CharSequence> withStopTest(final Parsers.Parser<CharSequence> p) {
return new Parsers.Parser()
    {
      public Parsers.ParseResult<CharSequence> apply(Reader input) {
        if (VisitorParserGeneration.this.stopFlag[0]==false)//////////////////////////////////////////////
          throw new IllegalArgumentException("User-Requested STOP", new IOException());
        return p.apply(input);
      }
    };   }
  
  public Parsers.Parser<? extends Object> visitChoice(NodeChoice n)
  {
    int childCount = n.getChildCount();
    if (n.accept(this.visitorNodeValidation) == null) {
      Parsers.Parser<? extends Object>[] cp = (Parsers.Parser[])new Parsers.Parser[childCount];
      for (int i = 0; i < childCount; i++) {
        cp[i] = ((Parsers.Parser)((NodeBase)n.getChildAt(i)).accept(this));
      }
      return withMultiplicity(this.parsersInstance.choice(n.errorMessage.isEmpty() ? String.format("choice(%s)", new Object[] { n.nodeName() }) : n.errorMessage, cp), n);
    }
    
    this.parserGeneratedOk = false;
    return null;
  }
  

  public Parsers.Parser<? extends Object> visitLiteral(NodeLiteral n)
  {
    if (n.accept(this.visitorNodeValidation) == null) {
      String litString = Utils.unEscape(((String)this.theForest.tokenBank.get(n.literalName)).substring(1));
      String errMsg = n.errorMessage.isEmpty() ? String.format("%s expects literal %s, found ", new Object[] { n.nodeName(), n.literalName }) : n.errorMessage;
      
      return withMultiplicity(withStopTest(n.literalName.endsWith("_") ? this.parsersInstance.literal(errMsg, litString) : this.parsersInstance.literal2(errMsg, litString)), n);
    }
    
    this.parserGeneratedOk = false;
    return null;
  }
  

  public Parsers.Parser<? extends Object> visitReference(NodeReference n)
  {
    if (n.accept(this.visitorNodeValidation) == null) {
      final String referredRuleName = n.refRuleName;
      NodeBase referredRule = (NodeBase)this.theForest.ruleBank.get(referredRuleName);
      if (!this.parserCache.containsKey(referredRuleName)) {
        referredRule.accept(this);
      }
      final Parsers.Parser<? extends Object>[] holder = (Parsers.Parser[])this.parserCache.get(referredRuleName);
      Parsers.Parser<? extends Object> p = new Parsers.Parser()
      {
        public Parsers.ParseResult<? extends Object> apply(Reader input) {
          try {
            return holder[0].apply(input);
          } catch (StackOverflowError soe) {
            throw new RuntimeException(String.format("Possible left-recursion in '%s'", new Object[] { referredRuleName }), soe);
          }
        }
      };
      return withMultiplicity(p, n);
    }
    this.parserGeneratedOk = false;
    return null;
  }
  

  public Parsers.Parser<? extends Object> visitRegex(NodeRegex n)
  {
    if (n.accept(this.visitorNodeValidation) == null) {
      String regString = Utils.unEscape(((String)this.theForest.tokenBank.get(n.regexName)).substring(1));
      String errMsg = n.errorMessage.isEmpty() ? String.format("%s expects regex %s, found ", new Object[] { n.nodeName(), n.regexName }) : n.errorMessage;
      
      return withMultiplicity(withStopTest(n.regexName.endsWith("_") ? this.parsersInstance.regex(errMsg, Pattern.compile(regString)) : this.parsersInstance.regex2(errMsg, Pattern.compile(regString))), n);
    }
    

    this.parserGeneratedOk = false;
    return null;
  }
  

  public Parsers.Parser<? extends Object> visitRepSep(NodeRepSep n)
  {
    if (n.accept(this.visitorNodeValidation) == null) {
      Parsers.Parser<Object> rep = (Parsers.Parser)((NodeBase)n.getChildAt(0)).accept(this);
      Parsers.Parser<Object> sep = (Parsers.Parser)((NodeBase)n.getChildAt(1)).accept(this);
      if (n.multiplicity == Multiplicity.ZeroOrMore)
        return withTrace(this.parsersInstance.repSep(rep, sep), n);
      if (n.multiplicity == Multiplicity.OneOrMore) {
        return withTrace(this.parsersInstance.rep1Sep(n.errorMessage.isEmpty() ? String.format("rep1sep(%s) error", new Object[] { n.nodeName() }) : n.errorMessage, rep, sep), n);
      }
      
      this.parserGeneratedOk = false;
      return null;
    }
    
    this.parserGeneratedOk = false;
    return null;
  }
  


  public Parsers.Parser<? extends Object> visitRoot(NodeRoot n)
  {
    Parsers.Parser[] holder = new Parsers.Parser[1];
    this.parserCache.put(n.ruleName, holder);
    Parsers.Parser<? extends Object> p;
		if (n.accept(this.visitorNodeValidation) == null) {
      p = (Parsers.Parser)((NodeBase)n.getChildAt(0)).accept(this);
    } else {
      this.parserGeneratedOk = false;
      p = null;
    }
    if (n.isPackrat) {
      p = this.parsersInstance.parser2packrat(p);
    }
    holder[0] = withTrace(p, n);
    return holder[0];
  }
  
  public Parsers.Parser<? extends Object> visitSemPred(final NodeSemPred n)
  {
    if (n.accept(this.visitorNodeValidation) == null) {
      Parsers.Parser<? extends Object> parser = new Parsers.Parser()
      {
        public Parsers.ParseResult<? extends Object> apply(Reader input) {
          Object result = null;
          try {
            result = n.actionFunction.run(null, input, input.source().length());
          }
          catch (ScriptException ex) {}
          if (result == Boolean.TRUE) {
            return new Parsers.Success(null, input);
          }
          return new Parsers.Failure(n.errorMessage.isEmpty() ? String.format("SemPred(%s)", new Object[] { n.nodeName() }) : n.errorMessage, input);
        }
        

      };
      return withTrace(parser, n);
    }
    this.parserGeneratedOk = false;
    return null;
  }
  

  public Parsers.Parser<? extends Object> visitSequence(NodeSequence n)
  {
    int childCount = n.getChildCount();
    if (n.accept(this.visitorNodeValidation) == null) {
      int dropMap = 0;
      Parsers.Parser<? extends Object>[] cp = (Parsers.Parser[])new Parsers.Parser[childCount];
      int i = 0; for (int mask = 1; i < childCount; mask <<= 1) {
        NodeBase child = (NodeBase)n.getChildAt(i);
        cp[i] = ((Parsers.Parser)child.accept(this));
        if ((child.isDropped) || (child.multiplicity == Multiplicity.Guard) || (child.multiplicity == Multiplicity.Not) || ((child instanceof NodeSemPred)))
        {
          dropMap |= mask;
        }
        i++;
      }
      




      return withMultiplicity(this.parsersInstance.sequence(n.errorMessage.isEmpty() ? String.format("sequence(%s)", new Object[] { n.nodeName() }) : n.errorMessage, n.commitIndex, dropMap, cp), n);
    }
    
    this.parserGeneratedOk = false;
    return null;
  }
  

  public Parsers.Parser<? extends Object> visitWildCard(NodeWildCard n)
  {
    if (n.accept(this.visitorNodeValidation) == null) {
      String errMsg = n.errorMessage.isEmpty() ? String.format("wildCard(%s)", new Object[] { n.nodeName() }) : n.errorMessage;
      
      return withMultiplicity(this.parsersInstance.wildCard(errMsg), n);
    }
    this.parserGeneratedOk = false;
    return null;
  }
  



  private Map<String, Parsers.Parser<? extends Object>[]> parserCache = new HashMap();
  public boolean parserGeneratedOk;
  private int traceLevel = 0;
  private VisitorValidation visitorNodeValidation;
  private Forest theForest;
  public boolean[] stopFlag;
}