package net.gui;

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
import net.api.VisitorValidation;

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
