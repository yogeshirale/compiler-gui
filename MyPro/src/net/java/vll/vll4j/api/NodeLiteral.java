package net.java.vll.vll4j.api;









public class NodeLiteral
  extends NodeBase
{
  public String literalName;
  



 
 
 
 
 
   public NodeLiteral(String name)
   {
     this.literalName = name;
   }
   
   public String nodeType() {
     return "Literal";
   }
   
   public NodeLiteral clone()
   {
     NodeLiteral n = new NodeLiteral(this.literalName);
     n.copyFrom(this);
     return n;
   }
   
   public boolean getAllowsChildren()
   {
     return false;
   }
   
   public Object accept(VisitorBase v)
   {
     return v.visitLiteral(this);
   }
   
   public String getName()
   {
     return String.format(this.description.isEmpty() ? "%s %s %s" : "%s %s %s (%s)", new Object[] { this.multiplicity, this.literalName, getAttributes(), this.description });
   }
 }
