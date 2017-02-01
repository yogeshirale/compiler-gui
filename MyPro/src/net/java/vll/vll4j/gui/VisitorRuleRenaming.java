package net.java.vll.vll4j.gui;

import java.util.ArrayList;
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

public class VisitorRuleRenaming
  extends VisitorBase
{
  public VisitorRuleRenaming(String currentName, String newName)
  {
    this.currentName = currentName;
    this.newName = newName;
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
    if (n.refRuleName.equals(this.currentName))
      n.refRuleName = this.newName;
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
    if (n.ruleName.equals(this.currentName))
      n.ruleName = this.newName;
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
  
  ArrayList<String> ruleList = new ArrayList();
  private String currentName;
  private String newName;
}
