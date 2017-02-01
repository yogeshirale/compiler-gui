package net.api;

public class NodeRegex
  extends NodeBase
{
  public String regexName;
  

  public NodeRegex(String name)
  {
    this.regexName = name;
  }
  
  public String nodeType() {
    return "Regex";
  }
  
  public NodeRegex clone()
  {
    NodeRegex n = new NodeRegex(this.regexName);
    n.copyFrom(this);
    return n;
  }
  
  public boolean getAllowsChildren()
  {
    return false;
  }
  
  public Object accept(VisitorBase v)
  {
    return v.visitRegex(this);
  }
  
  public String getName()
  {
    return String.format(this.description.isEmpty() ? "%s %s %s" : "%s %s %s (%s)", new Object[] { this.multiplicity, this.regexName, getAttributes(), this.description });
  }
}