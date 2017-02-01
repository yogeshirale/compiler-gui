package net.java.vll.vll4j.api;






















public class NodeSemPred
  extends NodeBase
{
  public String nodeType()
  {
    return "SemPred";
  }
  
  public NodeSemPred clone()
  {
    NodeSemPred n = new NodeSemPred();
    n.copyFrom(this);
    return n;
  }
  
  public boolean getAllowsChildren()
  {
    return false;
  }
  
  public Object accept(VisitorBase v)
  {
    return v.visitSemPred(this);
  }
  
  public String getName()
  {
    return String.format(this.description.isEmpty() ? "%s %s" : "%s %s (%s)", new Object[] { this.multiplicity, getAttributes(), this.description });
  }
}