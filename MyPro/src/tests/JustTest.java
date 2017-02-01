/*    */ package tests;
/*    */ 
/*    */ import java.util.Stack;
/*    */ 
/*    */ public class JustTest {
/*    */   private static void one() {
/*  7 */     String msg = "function (hello) {return;";
/*  8 */     boolean m = msg.matches("function\\s*\\([a-zA-Z][a-zA-Z0-9]*\\)\\s*\\{.+\\}");
/*  9 */     System.out.println(m);
/*    */   }
/*    */   
/* 12 */   private static void two() { Stack<String> stack = new Stack();
/* 13 */     stack.push("one");stack.push("two");stack.push("five");stack.push("ten");
/* 14 */     for (String s : stack) {
/* 15 */       System.out.println(s);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {}
/*    */ }


/* Location:              /home/yogesh/Downloads/VLL4J.jar!/tests/JustTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */