package net.gui;

import java.util.TreeSet;
import net.api.NodeBase;
import net.api.NodeChoice;
import net.api.NodeLiteral;
import net.api.NodeReference;
import net.api.NodeRegex;
import net.api.NodeRepSep;
import net.api.NodeRoot;
import net.api.NodeSemPred;
import net.api.NodeSequence;
import net.api.NodeWildCard;
import net.api.VisitorBase;

public class VisitorTokenSearch
  extends VisitorBase
{
  public VisitorTokenSearch(String tokenToFind)
  {
    this.tokenToFind = tokenToFind;
  }
  
  private String getRuleName(NodeBase node) 
  {
    while (!(node instanceof NodeRoot)) 
    {
      node = (NodeBase)node.getParent();
    }
    return ((NodeRoot)node).ruleName;
  }
  
  public Object visitChoice(NodeChoice n)
  {
    visitAllChildNodes(n);
    return null;
  }
  
  public Object visitLiteral(NodeLiteral n)
  {
    if (n.literalName.equals(this.tokenToFind))
      this.ruleSet.add(getRuleName(n));
    return null;
  }
  
  public Object visitReference(NodeReference n)
  {
    return null;
  }
  
  public Object visitRegex(NodeRegex n)
  {
    if (n.regexName.equals(this.tokenToFind))
      this.ruleSet.add(getRuleName(n));
    return null;
  }
  
  public Object visitRepSep(NodeRepSep n)
  {
    visitAllChildNodes(n);
    return null;
  }
  
  public Object visitRoot(NodeRoot n)
  {
    visitAllChildNodes(n);
    return null;
  }
  
  public Object visitSemPred(NodeSemPred n)
  {
    return null;
  }
  
  public Object visitSequence(NodeSequence n)
  {
    visitAllChildNodes(n);
    return null;
  }
  
  public Object visitWildCard(NodeWildCard n)
  {
    return null;
  }
  
  TreeSet<String> ruleSet = new TreeSet();
  private String tokenToFind;
}
