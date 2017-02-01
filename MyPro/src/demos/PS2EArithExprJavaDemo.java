/*    */ package demos;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.PrintStream;
/*    */ import java.util.List;
/*    */ import javax.xml.parsers.ParserConfigurationException;
/*    */ import net.java.vll.vll4j.api.Vll4j;
import net.java.vll.vll4j.combinator.Parsers;
/*    */ import net.java.vll.vll4j.combinator.Parsers.ParseResult;
/*    */ import net.java.vll.vll4j.combinator.Parsers.Parser;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ 
/*    */ public class PS2EArithExprJavaDemo
/*    */ {
/*    */   static Float evalExprAST(Object ast)
{
     Object[] array = (Object[])ast;
    Float term = evalTermAST(array[0]);
  //for (Object[] pair : Arrays.asList((int [])array[0]))   ///////////////////////////////////////////////////////////////////////
//{
//     if (pair[0].equals(Integer.valueOf(0))) 
//{
//      term = Float.valueOf(term.floatValue() + evalTermAST(pair[1]).floatValue());
//     }
//else if (pair[0].equals(Integer.valueOf(1)))
//{
  //     term = Float.valueOf(term.floatValue() - evalTermAST(pair[1]).floatValue());
  //   }
  //  }
   return term;
  }
  
  static Float evalTermAST(Object ast) {
    Object[] array = (Object[])ast;
     Float factor = evalFactorAST(array[0]);
  //   for (Object[] pair : (List)array[1]) {///////////////////////////////////////////////////////////////////
  //    if (pair[0].equals(Integer.valueOf(0))) {
  //     factor = Float.valueOf(factor.floatValue() * evalFactorAST(pair[1]).floatValue());
  //     } else if (pair[0].equals(Integer.valueOf(1))) {
  //      factor = Float.valueOf(factor.floatValue() / evalFactorAST(pair[1]).floatValue());
  //   }
  // }
    return factor;
  }
  
  static Float evalFactorAST(Object ast)
  /*    */   {
  /* 33 */     Object[] pair = (Object[])ast;
  /* 34 */     Float factorResult = Float.valueOf(-1.0F);
  /* 35 */     if (pair[0].equals(Integer.valueOf(0))) {
  /* 36 */       factorResult = Float.valueOf(Float.parseFloat((String)pair[1]));
  /* 37 */     } else if (pair[0].equals(Integer.valueOf(1))) {
  /* 38 */       factorResult = evalExprAST(pair[1]);
  /*    */     }
  /* 40 */     return factorResult;
  /*    */   }
  /*    */   
/*    */   
/*    */   public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException
/*    */   {
/* 71 */     String input = "(3 + 5) * (8 - 4)";
/* 72 */     Vll4j vll = Vll4j.fromFile(new File("PS2E-ArithExpr.vll"));
/* 73 */     Parsers.Parser exprParser = vll.getParserFor("Expr");
/* 74 */     Parsers.ParseResult parseResult = vll.parseAll(exprParser, input);
/* 75 */     if (parseResult.successful()) {
/* 76 */       Object ast = parseResult.get();
/* 77 */       Float result = evalExprAST(ast);
/* 78 */       System.out.println(result);
/*    */     } else {
/* 80 */       System.out.println(parseResult);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/yogesh/Downloads/VLL4J.jar!/demos/PS2EArithExprJavaDemo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */