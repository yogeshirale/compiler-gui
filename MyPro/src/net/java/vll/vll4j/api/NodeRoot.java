package net.java.vll.vll4j.api;









public class NodeRoot
  extends NodeBase
{
  public String ruleName;
  








  public NodeRoot(String ruleName)
  {
    this.ruleName = ruleName;
  }
  
  public String nodeType() {
    return "Root";
  }
  
  public NodeRoot clone()
  {
    NodeRoot n = new NodeRoot(this.ruleName);
    for (int i = 0; i < getChildCount(); i++) {
      n.add((NodeBase)((NodeBase)getChildAt(i)).clone());
    }
    n.copyFrom(this);
    return n;
  }
  
  public String nodeName() {
    return this.ruleName;
  }
  
  public Object accept(VisitorBase v)
  {
    return v.visitRoot(this);
  }
  
  public String getName()
  {
    return String.format(this.description.isEmpty() ? "%s %s" : "%s %s (%s)", new Object[] { this.ruleName, getAttributes(), this.description });
  }
  


  public boolean isPackrat = false;
}

