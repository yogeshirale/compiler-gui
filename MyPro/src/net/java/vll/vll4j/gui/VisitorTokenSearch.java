package net.java.vll.vll4j.gui;

import java.util.TreeSet;
import net.java.vll.vll4j.api.NodeBase;
import net.java.vll.vll4j.api.NodeChoice;
import net.java.vll.vll4j.api.NodeLiteral;
import net.java.vll.vll4j.api.NodeReference;
import net.java.vll.vll4j.api.NodeRegex;
import net.java.vll.vll4j.api.NodeRepSep;
import net.java.vll.vll4j.api.NodeRoot;
import net.java.vll.vll4j.api.NodeSemPred;
import net.java.vll.vll4j.api.NodeSequence;
import net.java.vll.vll4j.api.NodeWildCard;
import net.java.vll.vll4j.api.VisitorBase;

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
