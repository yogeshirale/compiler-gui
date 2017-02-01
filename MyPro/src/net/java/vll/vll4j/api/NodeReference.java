package net.java.vll.vll4j.api;

public class NodeReference
  extends NodeBase
{
  public String refRuleName;
 
   public NodeReference(String name)
   {
     this.refRuleName = name;
   }
   
   public String nodeType() {
     return "Reference";
   }
   
   public NodeReference clone()
   {
     NodeReference n = new NodeReference(this.refRuleName);
     n.copyFrom(this);
     return n;
   }
   
   public Object accept(VisitorBase v)
   {
     return v.visitReference(this);
   }
   
   public String getName()
   {
     return String.format(this.description.isEmpty() ? "%s %s %s" : "%s %s %s (%s)", new Object[] { this.multiplicity, this.refRuleName, getAttributes(), this.description });
   }
 }