package net.combinator;

import java.util.ArrayList;
import java.util.List;



public class Parsers
{
  public static abstract class Parser<T>
  {
    public String name;
    
    public abstract Parsers.ParseResult<T> apply(Reader paramReader);
  }
  
  public static abstract interface ParseResult<T>
  {
    public abstract T get();
    
    public abstract boolean successful();
    
    public abstract Reader next();
  }
  
  public static class NoSuccess<T>
    implements Parsers.ParseResult<T>
  {
    public final String msg;
    
    public NoSuccess(String msg, Reader next) { this(msg, next, null); }
    
    public NoSuccess(String msg, Reader next, NoSuccess reason) {
      this.msg = msg;
      this.next = next;
      this.reason = reason;
      Parsers.lastNoSuccess = this;
    }
    
    public T get() { return null; }
    
    public boolean successful() { return false; }
    
    public Reader next() { return this.next; }
    
    public NoSuccess reason = null;
    private Reader next; }
  
  public static class Success<T> implements Parsers.ParseResult<T> { private final T result;
    private Reader next;
    
    public Success(T result, Reader next) { this.result = result;
      this.next = next;
    }
    
    public T get() { return (T)this.result; }
    
    public boolean successful() { return true; }
    
    public Reader next() { return this.next; }
  }
  
  public static class Failure<T> extends Parsers.NoSuccess<T>
  {
    public Failure(String msg, Reader next)
    {
      super(msg, next);
    }
    
    public Failure(String msg, Reader next, Parsers.NoSuccess reason)
(msg, next, reason);
  }
  
  public static class Error extends Parsers.NoSuccess
  {
    public Error(String msg, Reader next) {
      super(msg,next);
    }
    
    public Error(String msg, Reader next, Parsers.NoSuccess reason)
,next, reason);
  }
  









  public <T> Parser<T> phrase(final Parser<T> p)
  {
    lastNoSuccess = null;
 Parser()
    {
      public Parsers.ParseResult<T> apply(Reader input) {
        Parsers.ParseResult<T> pr = p.apply(input);
        if (pr.next().atEnd()) {
          return pr;
        }
        return new Parsers.Failure("expected end of input", input);
      }
    };   }
  
  public Parser<Object> failure(final String msg) {
 Parser()
    {
      public Parsers.ParseResult<Object> apply(Reader input) {
        return new Parsers.Failure(msg, input);
      }
    };   }
  
  public <T> Parser<T> success(final T value) {
 Parser()
    {
      public Parsers.ParseResult<T> apply(Reader input) {
        return new Parsers.Success(value, input);
      }
    };   }
  
  public <T> Parser<T> guard(final Parser<T> p) {
 Parser()
    {
      public Parsers.ParseResult<T> apply(Reader input) {
        Parsers.ParseResult<T> pr = p.apply(input);
        if (pr.successful()) {
          return new Parsers.Success(pr.get(), input);
        }
        return new Parsers.Failure("??guard??", input);
      }
    };   }
  
  public <T> Parser<T> not(final Parser<T> p) {
return new Parser()
    {
      public Parsers.ParseResult<T> apply(Reader input) {
        Parsers.ParseResult<T> pr = p.apply(input);
        if (pr.successful()) {
          return new Parsers.Failure("??not??", input);
        }
        return new Parsers.Success(pr.get(), input);
      }
    };   }
  
  public <T> Parser<Object[]> opt(final Parser<T> p) {
return new Parser()
    {
      public Parsers.ParseResult<Object[]> apply(Reader input) {
        Parsers.ParseResult<T> pr = p.apply(input);
        if (pr.successful()) {
          return new Parsers.Success(new Object[] { pr.get() }, pr.next());
        }
        return new Parsers.Success(new Object[] { null }, input);
      }
    };   }
  
  public <T> Parser<List<T>> rep(final Parser<T> p) {
return new Parser()
    {
      public Parsers.ParseResult<List<T>> apply(Reader input) {
        List<T> list = new ArrayList();
        Parsers.ParseResult<T> pr;
        while ((pr = p.apply(input)).successful()) {
          list.add(pr.get());
          input = pr.next();
        }
        return new Parsers.Success(list, input);
      }
    };   }
  
  public <T> Parser<List<T>> repSep(final Parser<T> rep, final Parser<T> sep) {
return new Parser()
    {
      public Parsers.ParseResult<List<T>> apply(Reader input) {
        List<T> list = new ArrayList();
        Parsers.ParseResult<T> pr;
        if ((pr = rep.apply(input)).successful()) {
          list.add(pr.get());
          input = pr.next();
          while (((pr = sep.apply(input)).successful()) && ((pr = rep.apply(pr.next())).successful()))
          {
            list.add(pr.get());
            input = pr.next();
          }
        }
        return new Parsers.Success(list, input);
      }
    };   }
  
  public <T> Parser<List<T>> rep1(Parser<T> p) {
    return rep1("??rep1??", p);
  }
  
  public <T> Parser<List<T>> rep1(final String errMsg, final Parser<T> p) 
      {
	
return new Parser()
/*     */     {
/*     */       public Parsers.ParseResult<List<T>> apply(Reader input)
{
/* 222 */         List<T> list = new ArrayList();
/*     */         Parsers.ParseResult<T> pr;
/* 224 */         while ((pr = p.apply(input)).successful()) 
{
/* 225 */           list.add(pr.get());
/* 226 */           input = pr.next();
/*     */         }
/* 228 */         if (list.isEmpty()) 
{
/* 229 */           return new Parsers.Failure(errMsg, input, (Parsers.NoSuccess)pr);
/*     */         }
/* 231 */         return new Parsers.Success(list, input);
/*     */       }
/*     */     };   }
/*     */   
/*     */   public <T> Parser<List<T>> rep1Sep(Parser<T> rep, Parser<T> sep) {
/* 237 */     return rep1Sep("??rep1sep??", rep, sep);
/*     */   }
/*     */   
/*     */   public <T> Parser<List<T>> rep1Sep(final String errMsg, final Parser<T> rep, final Parser<T> sep) {
return new Parser()
/*     */     {
/*     */       public Parsers.ParseResult<List<T>> apply(Reader input) {
/* 244 */         List<T> list = new ArrayList();
/*     */         Parsers.ParseResult<T> pr;
/* 246 */         if ((pr = rep.apply(input)).successful()) {
/* 247 */           list.add(pr.get());
/* 248 */           input = pr.next();
/* 249 */           while (((pr = sep.apply(input)).successful()) && ((pr = rep.apply(pr.next())).successful()))
/*     */           {
/* 251 */             list.add(pr.get());
/* 252 */             input = pr.next();
/*     */           }
/*     */         }
/* 255 */         if (list.isEmpty()) {
/* 256 */           return new Parsers.Failure(errMsg, input, (Parsers.NoSuccess)pr);
/*     */         }
/* 258 */         return new Parsers.Success(list, input);
/*     */       }
/*     */     };   }
/*     */   
/*     */   public Parser<? extends Object> sequence(int commitIndex, int dropMap, Parser<Object>... p) {
/* 264 */     return sequence("??sequence??", commitIndex, dropMap, p);
/*     */   }
/*     */   
/*     */   public Parser<? extends Object> sequence(final String errMsg, final int commitIndex, final int dropMap, final Parser<? extends Object>... p) {
return new Parser()
/*     */     {
/*     */       public Parsers.ParseResult<Object> apply(Reader input) {
/* 271 */         int bc = Integer.bitCount(dropMap);
/* 272 */         Object[] res = new Object[p.length - bc];
/* 273 */         Reader inputOriginal = input;
/* 274 */         Parsers.ParseResult<? extends Object> pr = null;
/* 275 */         int i = 0;int j = 0;
/* 276 */         for (int mask = 1; i < p.length; mask <<= 1) {
/* 277 */           pr = p[i].apply(input);
/* 278 */           if (!pr.successful()) {
/* 279 */             if (i <= commitIndex) break;
/* 280 */             Parsers.NoSuccess<? extends Object> ns = (Parsers.NoSuccess)pr;
/* 281 */             break;
/*     */           }
/*     */           
/* 284 */           if ((dropMap & mask) == 0) {
/* 285 */             res[(j++)] = pr.get();
/*     */           }
/* 287 */           input = pr.next();i++;
/*     */         }
/* 289 */         if ((pr != null) && (pr.successful())) {
/* 290 */           if (res.length == 1) {
/* 291 */             return new Parsers.Success(res[0], input);
/*     */           }
/* 293 */           return new Parsers.Success(res, input);
/*     */         }
/* 295 */         return i > commitIndex ? new Parsers.Error(errMsg, inputOriginal, (Parsers.NoSuccess)pr) : new Parsers.Failure(errMsg, inputOriginal, (Parsers.NoSuccess)pr);
/*     */       }
/*     */     };   }
/*     */   
/*     */   public Parser<Object[]> choice(Parser<? extends Object>... p)
/*     */   {
/* 302 */     return choice("??choice??", p);
/*     */   }
/*     */   
/*     */   public Parser<Object[]> choice(final String errMsg, final Parser<? extends Object>... p) {
return new Parser()
   {
     public Parsers.ParseResult<Object[]> apply(Reader input) {
       Parsers.ParseResult<? extends Object> pr = null;
       int n;
       for (n = 0; n < p.length; n++) {
         pr = p[n].apply(input);
         if ((pr.successful()) || ((pr instanceof Parsers.Error)))
           break;
       }
       if ((pr != null) && (pr.successful())) {
         return new Parsers.Success(new Object[] { Integer.valueOf(n), pr.get() }, pr.next());
       }
       return new Parsers.Failure(errMsg, input, (Parsers.NoSuccess)pr);
     }
   };   }
  
  public static NoSuccess lastNoSuccess = null;
  
  public String dumpResult(ParseResult pr) {
    if ((pr instanceof Success)) {
      Success s = (Success)pr;
      return String.format("Success(%d, %d): %s", new Object[] { Integer.valueOf(s.next().line()), Integer.valueOf(s.next().column()), s.get() }); }
    if ((pr instanceof NoSuccess)) {
      StringBuilder sb = new StringBuilder();
      NoSuccess f = (NoSuccess)pr;
      while (f != null) {
        Reader str = f.next;
        String sample = str.source().subSequence(str.offset(), Math.min(str.source().length(), str.offset() + 20)).toString();
        sb.append(String.format("%s(%d, %d): %s <%s>%n", new Object[] { f.getClass().getSimpleName(), Integer.valueOf(f.next().line()), Integer.valueOf(f.next().column()), f.msg, sample }));
        
        f = f.reason;
      }
      return sb.toString();
    }
    return String.format("Unknown: [%s] %s", new Object[] { pr.getClass().getName(), pr });
  }
}

