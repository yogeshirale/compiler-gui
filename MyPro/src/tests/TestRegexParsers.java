/*    */ package tests;
/*    */ 
/*    */ import java.util.regex.Pattern;

import net.java.vll.vll4j.combinator.Parsers;
/*    */ import net.java.vll.vll4j.combinator.Parsers.Parser;
/*    */ import net.java.vll.vll4j.combinator.RegexParsers;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TestRegexParsers
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/* 13 */     RegexParsers me = new RegexParsers();
/* 14 */     Parsers.Parser<CharSequence> nbr = me.regex(Pattern.compile("\\d+"));
/* 15 */     Parsers.Parser<CharSequence> name = me.regex(Pattern.compile("[a-z]+"));
/* 16 */     Parsers.Parser<CharSequence> semi = me.literal(";");
/*    */   }
/*    */ }


/* Location:              /home/yogesh/Downloads/VLL4J.jar!/tests/TestRegexParsers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */