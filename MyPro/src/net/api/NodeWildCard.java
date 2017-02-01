package net.api;

public class NodeWildCard
  extends NodeBase
{
  public String nodeType()
  {
    return "WildCard";
  }
  
  public NodeWildCard clone()
  {
    NodeWildCard n = new NodeWildCard();
    n.copyFrom(this);
    return n;
  }
  
  public boolean getAllowsChildren()
  {
    return false;
  }
  
  public Object accept(VisitorBase v)
  {
    return v.visitWildCard(this);
  }
  
  public String getName()
  {
    return String.format(this.description.isEmpty() ? "%s %s" : "%s %s (%s)", new Object[] { this.multiplicity, getAttributes(), this.description });
  }
}