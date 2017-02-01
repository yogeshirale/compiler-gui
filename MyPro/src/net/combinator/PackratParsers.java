/*     */ package net.combinator;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PackratParsers
/*     */   extends SimpleLexingParsers
/*     */ {
/*     */   static String sn(Object obj)
/*     */   {
/*  38 */     String name = obj.toString();
/*  39 */     int idx = name.lastIndexOf('.');
/*  40 */     return idx == -1 ? name : name.substring(idx + 1);
/*     */   }
/*     */   
/*     */   public static class PackratReader extends Reader { private Reader underlying;
/*     */     
/*  45 */     PackratReader(Reader underlying) { this.underlying = underlying;
/*  46 */       this.lrStack = new ArrayList();
/*  47 */       this.recursionHeads = new HashMap();
/*  48 */       this.cache = new HashMap();
/*     */     }
/*     */     
/*     */     private PackratReader() {}
/*     */     
/*     */     public boolean atEnd() {
/*  54 */       return this.underlying.atEnd();
/*     */     }
/*     */     
/*     */     public int column() {
/*  58 */       return this.underlying.column();
/*     */     }
/*     */     
/*     */     public Reader drop(int n) {
/*  62 */       PackratReader pr = new PackratReader();
/*  63 */       pr.underlying = this.outer.underlying.drop(n);
/*  64 */       pr.cache = this.outer.cache;
/*  65 */       pr.lrStack = this.outer.lrStack;
/*  66 */       pr.recursionHeads = this.outer.recursionHeads;
/*     */       
/*  68 */       return pr;
/*     */     }
/*     */     
/*     */ 
/*  72 */     public char first() { return this.underlying.first(); }
/*     */     
/*     */     Option<PackratParsers.MemoEntry> getFromCache(Parsers.Parser p) {
/*  75 */       String key = String.format("%s%d", new Object[] { p, Integer.valueOf(offset()) });
/*  76 */       if (this.cache.containsKey(key))
/*     */       {
/*  78 */         return new Some(this.cache.get(key));
/*     */       }
/*     */       
/*  81 */       return new None();
/*     */     }
/*     */     
/*     */     public int line()
/*     */     {
/*  86 */       return this.underlying.line();
/*     */     }
/*     */     
/*     */     public int offset() {
/*  90 */       return this.underlying.offset();
/*     */     }
/*     */     
/*     */     public Reader rest() {
/*  94 */       return drop(1);
/*     */     }
/*     */     
/*     */ 
/*  98 */     public CharSequence source() { return this.underlying.source(); }
/*     */     
/*     */     PackratParsers.MemoEntry updateCacheAndGet(Parsers.Parser p, PackratParsers.MemoEntry w) {
/* 101 */       String key = String.format("%s%d", new Object[] { p, Integer.valueOf(offset()) });
/*     */       
/* 103 */       this.cache.put(key, w);
/* 104 */       return w;
/*     */     }
/*     */     
/* 107 */     List<PackratParsers.LR> lrStack = null;
/* 108 */     Map<Integer, PackratParsers.Head> recursionHeads = null;
/* 109 */     Map<String, PackratParsers.MemoEntry> cache = null;
/* 110 */     private PackratReader outer = this;
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> Parsers.Parser<T> phrase(Parsers.Parser<T> p)
/*     */   {
/* 116 */     final Parsers.Parser<T> q = super.phrase(p);
return new PackratParser()
/*     */     {
/*     */ 
/*     */       public Parsers.ParseResult<T> apply(Reader r) {
/* 121 */         return q.apply((r instanceof PackratParsers.PackratReader) ? r : new PackratParsers.PackratReader(r)); }
/*     */     };   }
/*     */   
/*     */   private static class MemoEntry<T> {
/*     */     Object r;
/*     */     
/* 128 */     MemoEntry(Parsers.ParseResult<T> r) { this.r = r; }
/*     */     
/*     */     MemoEntry(PackratParsers.LR r) {
/* 131 */       this.r = r;
/*     */     }
/*     */     
/* 134 */     Parsers.ParseResult<T> getResult() { return (this.r instanceof PackratParsers.LR) ? ((PackratParsers.LR)this.r).seed : (Parsers.ParseResult)this.r; }
/*     */   }
/*     */   
/*     */   static class LR { Parsers.ParseResult seed;
/*     */     Parsers.Parser rule;
/*     */     Option<PackratParsers.Head> head;
/*     */     
/* 141 */     LR(Parsers.ParseResult seed, Parsers.Parser rule, Option<PackratParsers.Head> head) { this.seed = seed;
/* 142 */       this.rule = rule;
/* 143 */       this.head = head;
/*     */     }
/*     */     
/* 146 */     int getPos() { return this.seed.next().offset(); }
/*     */   }
/*     */   
/*     */   static class Head
/*     */   {
/*     */     Parsers.Parser headParser;
/*     */     
/*     */     Head(Parsers.Parser headParser, List<Parsers.Parser> involvedSet, List<Parsers.Parser> evalSet)
/*     */     {
/* 155 */       this.headParser = headParser;
/* 156 */       this.involvedSet = involvedSet;
/* 157 */       this.evalSet = evalSet;
/*     */     }
/*     */     
/* 160 */     Parsers.Parser getHead() { return this.headParser; }
/*     */     
/*     */ 
/* 163 */     List<Parsers.Parser> involvedSet = new ArrayList();
/* 164 */     List<Parsers.Parser> evalSet = new ArrayList();
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> PackratParser<T> parser2packrat(Parsers.Parser<T> p)
/*     */   {
/* 170 */     PackratParser<T> pp = memo(p);
/*     */     
/* 172 */     return pp;
/*     */   }
/*     */   
/*     */   private Option<MemoEntry> recall(Parsers.Parser p, PackratReader in) {
/* 176 */     Option<MemoEntry> cached = in.getFromCache(p);
/* 177 */     Option<Head> head = in.recursionHeads.containsKey(Integer.valueOf(in.offset())) ? new Some(in.recursionHeads.get(Integer.valueOf(in.offset()))) : new None();
/*     */     
/*     */ 
/*     */ 
/* 181 */     if (head.isEmpty())
/*     */     {
/* 183 */       return cached;
/*     */     }
/* 185 */     Parsers.Parser hp = ((Head)head.get()).headParser;
/* 186 */     List<Parsers.Parser> involved = ((Head)head.get()).involvedSet;
/* 187 */     List<Parsers.Parser> evalSet = ((Head)head.get()).evalSet;
/* 188 */     if ((cached.isEmpty()) && (!hp.equals(p)) && (!involved.contains(p))) {
/* 189 */       return new Some(new MemoEntry(new Parsers.Failure("dummy ", in, null)));
/*     */     }
/* 191 */     if (evalSet.contains(p)) {
/* 192 */       for (int i = evalSet.size() - 1; i >= 0; i--) {
/* 193 */         Parsers.Parser pi = (Parsers.Parser)evalSet.get(i);
/* 194 */         if (pi.equals(p))
/* 195 */           evalSet.remove(pi);
/*     */       }
/* 197 */       Parsers.ParseResult tempRes = p.apply(in);
/* 198 */       Option<MemoEntry> tempEntry = cached;
/* 199 */       ((MemoEntry)tempEntry.get()).r = tempRes;
/*     */     }
/*     */     
/* 202 */     return cached;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void setupLR(Parsers.Parser p, PackratReader in, LR recDetect)
/*     */   {
/* 209 */     if (recDetect.head.isEmpty())
/* 210 */       recDetect.head = new Some(new Head(p, new ArrayList(), new ArrayList()));
/* 211 */     for (LR lr : in.lrStack) {
/* 212 */       if (lr.rule.equals(p))
/*     */         break;
/* 214 */       lr.head = recDetect.head;
/* 215 */       if (!recDetect.head.isEmpty())
/* 216 */         ((Head)recDetect.head.get()).involvedSet.add(0, lr.rule);
/*     */     }
/*     */   }
/*     */   
/*     */   <T> Parsers.ParseResult<T> lrAnswer(Parsers.Parser<T> p, PackratReader in, LR growable) {
/* 221 */     Parsers.ParseResult seed = growable.seed;
/* 222 */     Parsers.Parser rule = growable.rule;
/* 223 */     Option<Head> head = growable.head;
/*     */     
/*     */ 
/* 226 */     if (head.isEmpty())
/* 227 */       throw new IllegalArgumentException("lrAnswer with no head !!");
/* 228 */     if (!((Head)head.get()).getHead().equals(p)) {
/* 229 */       return seed;
/*     */     }
/* 231 */     in.updateCacheAndGet(p, new MemoEntry(seed));
/* 232 */     if (seed.successful()) {
/* 233 */       return grow(p, in, (Head)head.get());
/*     */     }
/* 235 */     return seed;
/*     */   }
/*     */   
/*     */   <T> PackratParser<T> memo(final Parsers.Parser<T> p)
/*     */   {
return new PackratParser()
/*     */     {
/*     */       public Parsers.ParseResult<T> apply(Reader in) {
/* 243 */         PackratParsers.PackratReader inMem = (in instanceof PackratParsers.PackratReader) ? (PackratParsers.PackratReader)in : new PackratParsers.PackratReader(in);
/*     */         
/* 245 */         Option<PackratParsers.MemoEntry> m = PackratParsers.this.recall(p, inMem);
/* 246 */         if (m.isEmpty())
/*     */         {
/*     */ 
/* 249 */           PackratParsers.LR base = new PackratParsers.LR(new Parsers.Failure("Base failure", in, null), p, new None());
/* 250 */           inMem.lrStack.add(0, base);
/* 251 */           inMem.updateCacheAndGet(p, new PackratParsers.MemoEntry(base));
/* 252 */           Parsers.ParseResult tempRes = p.apply(in);
/* 253 */           inMem.lrStack.remove(0);
/* 254 */           if (base.head.isEmpty()) {
/* 255 */             inMem.updateCacheAndGet(p, new PackratParsers.MemoEntry(tempRes));
/* 256 */             return tempRes;
/*     */           }
/* 258 */           base.seed = tempRes;
/* 259 */           return PackratParsers.this.lrAnswer(p, inMem, base);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 264 */         Object mEntry = ((PackratParsers.MemoEntry)m.get()).r;
/* 265 */         if ((mEntry instanceof PackratParsers.LR)) {
/* 266 */           PackratParsers.LR recDetect = (PackratParsers.LR)mEntry;
/* 267 */           PackratParsers.this.setupLR(p, inMem, recDetect);
/* 268 */           return recDetect.seed;
/*     */         }
/* 270 */         Parsers.ParseResult res = (Parsers.ParseResult)mEntry;
/* 271 */         return res;
/*     */       }
/*     */     };   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   Parsers.ParseResult grow(Parsers.Parser p, PackratReader rest, Head head)
/*     */   {
/* 281 */     rest.recursionHeads.put(Integer.valueOf(rest.offset()), head);
/* 282 */     Parsers.ParseResult oldRes = (Parsers.ParseResult)((MemoEntry)rest.getFromCache(p).get()).r;
/* 283 */     head.evalSet = head.involvedSet;
/* 284 */     Parsers.ParseResult tempRes = p.apply(rest);
/* 285 */     if (tempRes.successful()) {
/* 286 */       if (oldRes.next().offset() < tempRes.next().offset()) {
/* 287 */         rest.updateCacheAndGet(p, new MemoEntry(tempRes));
/* 288 */         return grow(p, rest, head);
/*     */       }
/* 290 */       rest.recursionHeads.remove(Integer.valueOf(rest.offset()));
/* 291 */       MemoEntry m = (MemoEntry)rest.getFromCache(p).get();
/*     */       
/* 293 */       return (Parsers.ParseResult)m.r;
/*     */     }
/*     */     
/* 296 */     rest.recursionHeads.remove(Integer.valueOf(rest.offset()));
/* 297 */     return oldRes;
/*     */   }
/*     */   
/*     */ 
/*     */   public int handleWhiteSpace(CharSequence cs, int offset)
/*     */   {
/* 303 */     return super.handleWhiteSpace(cs, offset);
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 307 */     PackratParsers pp = new PackratParsers();
/* 308 */     Parsers.Parser nbr = pp.regex2("nbr", Pattern.compile("[0-9]+"));
/* 309 */     Parsers.Parser plus = pp.literal2("PLUS", "+");
/* 310 */     Parsers.Parser minus = pp.literal2("MINUS", "-");
/* 311 */     Parsers.Parser plusOrMinus = pp.choice("plusOrMinus", new Parsers.Parser[] { plus, minus });
/* 312 */     Parsers.Parser suffix = pp.rep(pp.sequence(Integer.MAX_VALUE, 0, new Parsers.Parser[] { plusOrMinus, nbr }));
/* 313 */     Parsers.Parser expr = pp.sequence(Integer.MAX_VALUE, 0, new Parsers.Parser[] { nbr, suffix });
/* 314 */     Parsers.ParseResult pr = pp.parseAll(expr, "123 + 5 - 7 + 23 - 567");
/* 315 */     if (pr.successful()) {
/* 316 */       System.out.println(Utils.dumpValue(pr.get(), true));
/*     */     } else {
/* 318 */       System.out.println(pp.dumpResult(pr));
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract class PackratParser<T>
/*     */     extends Parsers.Parser<T>
/*     */   {}
/*     */ }
