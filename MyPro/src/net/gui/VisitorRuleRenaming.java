package net.gui;

import java.util.ArrayList;
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
