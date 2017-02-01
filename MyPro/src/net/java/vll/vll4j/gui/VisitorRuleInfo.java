package net.java.vll.vll4j.gui;

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
import net.java.vll.vll4j.api.VisitorValidation;

public class VisitorRuleInfo
  extends VisitorBase
{
  public Object visitChoice(NodeChoice n)
  {
    if (!n.actionText.trim().isEmpty())
      this.hasActions = true;
    if (this.visitorValidation.visitChoice(n) != null)
      this.hasErrors = true;
    visitAllChildNodes(n);
    return null;
  }
  
  public Object visitLiteral(NodeLiteral n)
  {
    if (!n.actionText.trim().isEmpty())
      this.hasActions = true;
    if (this.visitorValidation.visitLiteral(n) != null)
      this.hasErrors = true;
    return null;
  }
  
  public Object visitReference(NodeReference n)
  {
    if (!n.actionText.trim().isEmpty())
      this.hasActions = true;
    if (this.visitorValidation.visitReference(n) != null)
      this.hasErrors = true;
    return null;
  }
  
  public Object visitRegex(NodeRegex n)
  {
    if (!n.actionText.trim().isEmpty())
      this.hasActions = true;
    if (this.visitorValidation.visitRegex(n) != null)
      this.hasErrors = true;
    return null;
  }
  
  public Object visitRepSep(NodeRepSep n)
  {
    if (!n.actionText.trim().isEmpty())
      this.hasActions = true;
    if (this.visitorValidation.visitRepSep(n) != null)
      this.hasErrors = true;
    visitAllChildNodes(n);
    return null;
  }
  
  public Object visitRoot(NodeRoot n)
  {
    this.hasErrors = (this.hasActions = this.isTester = false);
    if (n != null) 
    {
      if (!n.actionText.trim().isEmpty())
        this.hasActions = true;
      if (this.visitorValidation.visitRoot(n) != null)
        this.hasErrors = true;
      if (n.getChildCount() != 0) 
      {
        NodeBase c = (NodeBase)n.getChildAt(0);
        String action = c.actionText.trim();
        if ((!action.isEmpty()) && (
          (action.contains("vllParserTestInput")) || (action.contains("vllParserLog")))) 
        {
          this.isTester = true;
        }
      }
      visitAllChildNodes(n);
    }
    return null;
  }
  
  public Object visitSemPred(NodeSemPred n)
  {
    if (this.visitorValidation.visitSemPred(n) != null)
      this.hasErrors = true;
    return null;
  }
  
  public Object visitSequence(NodeSequence n)
  {
    if (!n.actionText.trim().isEmpty())
      this.hasActions = true;
    if (this.visitorValidation.visitSequence(n) != null)
      this.hasErrors = true;
    visitAllChildNodes(n);
    return null;
  }
  
  public Object visitWildCard(NodeWildCard n)
  {
    if (!n.actionText.trim().isEmpty())
      this.hasActions = true;
    if (this.visitorValidation.visitWildCard(n) != null)
      this.hasErrors = true;
    return null;
  }
  
  public boolean hasErrors = false; public boolean hasActions = false;
  private VisitorValidation visitorValidation = new VisitorValidation();
  public boolean isTester;
}
