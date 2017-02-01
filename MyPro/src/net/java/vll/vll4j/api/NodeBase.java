package net.java.vll.vll4j.api;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class NodeBase
  extends DefaultMutableTreeNode
{
  public abstract Object accept(VisitorBase paramVisitorBase);
  
  public abstract String getName();
  
  public abstract String nodeType();
  
  protected void copyFrom(NodeBase src)
{
  this.multiplicity = src.multiplicity;
  this.errorMessage = src.errorMessage;
  this.description = src.description;
  this.actionText = src.actionText;
  this.isTraced = false;
  this.isDropped = src.isDropped;
  this.actionFunction = src.actionFunction;
}

public String nodeName() {
  NodeBase parentNode = (NodeBase)getParent();
  if (parentNode == null)
    return "";
     int idx = parentNode.getIndex(this);
     return String.format("%s.%d", new Object[] { parentNode.nodeName(), Integer.valueOf(idx) });
   }
   
   public String getAttributes() {
     NodeBase parent = (NodeBase)getParent();
     boolean isCommitted = (parent != null) && ((parent instanceof NodeSequence)) && (((NodeSequence)parent).commitIndex == parent.getIndex(this));
     
     if ((this.isTraced) || (this.isDropped) || (!this.errorMessage.trim().isEmpty()) || (!this.actionText.trim().isEmpty()) || (isCommitted) || (((this instanceof NodeRoot)) && (((NodeRoot)this).isPackrat)))
     {
       StringBuilder sb = new StringBuilder();
       sb.append("[");
       if (!this.actionText.trim().isEmpty())
         sb.append("action");
       if (isCommitted) {
         if (sb.length() != 1)
           sb.append(" ");
         sb.append("commit");
       }
       if (this.isDropped) {
         if (sb.length() != 1)
           sb.append(" ");
         sb.append("drop");
       }
       if (!this.errorMessage.trim().isEmpty()) {
         if (sb.length() != 1)
           sb.append(" ");
         sb.append("message");
       }
       if (((this instanceof NodeRoot)) && (((NodeRoot)this).isPackrat)) {
         if (sb.length() != 1)
           sb.append(" ");
         sb.append("packrat");
       }
       if (this.isTraced) {
         if (sb.length() != 1)
           sb.append(" ");
         sb.append("trace");
       }
       sb.append("]");
       return sb.toString();
     }
     return "";
   }
   
 
   public boolean getAllowsChildren()
   {
     return true;
   }
   
   public String toString()
   {
     return getClass().getSimpleName() + ": " + nodeName();
   }
   
   public Multiplicity multiplicity = Multiplicity.One;
   public String errorMessage = "";
   public String description = "";
   public String actionText = "";
   public boolean isTraced = false;
   public boolean isDropped = false;
   public ActionFunction actionFunction = null;
 }
