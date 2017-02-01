/*     */ package demos;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import net.api.Vll4j;
					import net.combinator.Parsers;
/*     */ import net.combinator.Parsers.ParseResult;
/*     */ import net.combinator.Parsers.Parser;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TDARExprAST
/*     */ {
/*     */   static float evalAtom(Object ast)
/*     */   {
/*  35 */     Object[] pair = (Object[])ast;
/*  36 */     switch (((Integer)pair[0]).intValue()) {
/*     */     case 0: 
/*  38 */       return Float.parseFloat((String)pair[1]);
/*     */     case 1: 
/*  40 */       if (memory.containsKey(pair[1])) {
/*  41 */         return ((Float)memory.get(pair[1])).floatValue();
/*     */       }
/*     */       
/*  44 */       System.out.printf("Undefined variable: %s%n", new Object[] { pair[1] });
/*  45 */       return 0.0F;
/*     */     case 2: 
/*  47 */       return evalExpr(pair[1]);
/*     */     }
/*  49 */     return 0.0F;
/*     */   }
/*     */   
/*     */   static float evalMultExpr(Object ast) {
/*  53 */     Object[] arr = (Object[])ast;
/*  54 */     Float res = Float.valueOf(evalAtom(arr[0]));
/*  55 */     List<Object[]> lst = (List)arr[1];
/*  56 */     for (Object[] pair : lst) {
/*  57 */       res = Float.valueOf(res.floatValue() * evalAtom(pair[1]));
/*     */     }
/*  59 */     return res.floatValue();
/*     */   }
/*     */   
/*     */   static float evalExpr(Object ast) {
/*  63 */     Object[] arr = (Object[])ast;
/*  64 */     Float res = Float.valueOf(evalMultExpr(arr[0]));
/*  65 */     List<Object[]> lst = (List)arr[1];
/*  66 */     for (Object[] pair : lst) {
/*  67 */       Object[] discr = (Object[])pair[0];
/*  68 */       switch (((Integer)discr[0]).intValue()) {
/*  69 */       case 0:  res = Float.valueOf(res.floatValue() + evalMultExpr(pair[1])); break;
/*  70 */       case 1:  res = Float.valueOf(res.floatValue() - evalMultExpr(pair[1]));
/*     */       }
/*     */     }
/*  73 */     return res.floatValue();
/*     */   }
/*     */   
/*     */   static void evalStat(Object ast) {
/*  77 */     Object[] pair = (Object[])ast;
/*  78 */     switch (((Integer)pair[0]).intValue()) {
/*     */     case 0: 
/*  80 */       Object[] arr = (Object[])pair[1];
/*  81 */       String id = (String)arr[0];
/*  82 */       Float res = Float.valueOf(evalExpr(arr[1]));
/*  83 */       memory.put(id, res);
/*  84 */       break;
/*     */     case 1: 
/*  86 */       System.out.println(evalExpr(pair[1]));
/*  87 */       break;
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */   static void evalProg(Object ast)
/*     */   {
/*  94 */     List listOfStat = (List)ast;
/*  95 */     for (Object stat : listOfStat) {
/*  96 */       evalStat(stat);
/*     */     }
/*     */   }
/*     */   
/* 100 */   static Map<String, Float> memory = new HashMap();
/*     */   
/*     */   public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException
/*     */   {
/* 104 */     String input = "a=3\nb=4\n2+a*b\n";
/* 105 */     Vll4j vll = Vll4j.fromFile(new File("TDAR-Expr-AST.vll"));
/* 106 */     Parsers.Parser exprParser = vll.getParserFor("Prog");
/* 107 */     Parsers.ParseResult parseResult = vll.parseAll(exprParser, input);
/* 108 */     if (parseResult.successful()) {
/* 109 */       Object ast = parseResult.get();
/* 110 */       evalProg(ast);
/*     */     } else {
/* 112 */       System.out.println(parseResult);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/yogesh/Downloads/VLL4J.jar!/demos/TDARExprAST.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */
