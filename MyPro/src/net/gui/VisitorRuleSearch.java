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

public class VisitorRuleSearch
  extends VisitorBase
{
  public VisitorRuleSearch(String ruleToFind)
  {
    this.ruleToFind = ruleToFind;
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
    return null;
  }
  
  public Object visitReference(NodeReference n)
  {
    if (n.refRuleName.equals(this.ruleToFind))
      this.ruleSet.add(getRuleName(n));
    return null;
  }
  
  public Object visitRegex(NodeRegex n)
  {
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
  private String ruleToFind;
}
