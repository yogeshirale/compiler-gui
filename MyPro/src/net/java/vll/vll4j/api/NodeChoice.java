 package net.java.vll.vll4j.api;
 
 
 public class NodeChoice
   extends NodeBase
 {
   public String nodeType()
   {
     return "Choice";
   }
   
   public NodeChoice clone()
   {
     NodeChoice n = new NodeChoice();
     for (int i = 0; i < getChildCount(); i++) {
       n.add((NodeBase)((NodeBase)getChildAt(i)).clone());
     }
     n.copyFrom(this);
     return n;
   }
   
   public Object accept(VisitorBase v)
   {
     return v.visitChoice(this);
   }
   
   public String getName()
   {
     return String.format(this.description.isEmpty() ? "%s %s" : "%s %s (%s)", new Object[] { this.multiplicity, getAttributes(), this.description });
   }
 }
