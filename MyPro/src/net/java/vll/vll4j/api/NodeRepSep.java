package net.java.vll.vll4j.api;



















public class NodeRepSep
  extends NodeBase
{
  public NodeRepSep()
  {
    this.multiplicity = Multiplicity.ZeroOrMore;
  }
  
  public String nodeType() {
    return "RepSep";
  }
  
  public NodeRepSep clone()
  {
    NodeRepSep n = new NodeRepSep();
    for (int i = 0; i < getChildCount(); i++) {
      n.add((NodeBase)((NodeBase)getChildAt(i)).clone());
    }
    n.copyFrom(this);
    return n;
  }
  
  public Object accept(VisitorBase v)
  {
    return v.visitRepSep(this);
  }
  
  public String getName()
  {
    return String.format(this.description.isEmpty() ? "%s %s" : "%s %s (%s)", new Object[] { this.multiplicity, getAttributes(), this.description });
  }
}