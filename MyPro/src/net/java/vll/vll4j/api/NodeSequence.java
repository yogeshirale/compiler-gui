package net.java.vll.vll4j.api;





public class NodeSequence
  extends NodeBase
{
  public String nodeType()
  {
    return "Sequence";
  }
  
  public Object accept(VisitorBase v)
  {
    return v.visitSequence(this);
  }
  
  public NodeSequence clone()
  {
    NodeSequence n = new NodeSequence();
    for (int i = 0; i < getChildCount(); i++) {
      n.add((NodeBase)((NodeBase)getChildAt(i)).clone());
    }
    n.copyFrom(this);
    return n;
  }
  
  public String getName()
  {
    return String.format(this.description.isEmpty() ? "%s %s" : "%s %s (%s)", new Object[] { this.multiplicity, getAttributes(), this.description });
  }
  

  public int commitIndex = Integer.MAX_VALUE;
  long dropMap;
}