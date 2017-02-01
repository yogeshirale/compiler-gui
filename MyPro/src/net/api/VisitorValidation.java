package net.api;

public class VisitorValidation
  extends VisitorBase
{
  private String checkAction(NodeBase n)
  {
    if ((n.actionText.trim().isEmpty()) || (n.actionFunction != null)) {
      return null;
    }
    return "error in action-code";
  }
  
  public String visitChoice(NodeChoice n)
  {
    if (n.getChildCount() >= 2) {
      return checkAction(n);
    }
    return "needs 2 or more child nodes, " + checkAction(n);
  }
  


  public String visitLiteral(NodeLiteral n)
  {
    return checkAction(n);
  }
  
  public String visitReference(NodeReference n)
  {
    return checkAction(n);
  }
  
  public String visitRegex(NodeRegex n)
  {
    return checkAction(n);
  }
  
  public String visitRepSep(NodeRepSep n)
  {
    if (n.getChildCount() == 2) {
      return checkAction(n);
    }
    return "needs 2 child nodes, " + checkAction(n);
  }
  


  public String visitRoot(NodeRoot n)
  {
    if (n.getChildCount() == 1) {
      return checkAction(n);
    }
    return "needs 1 child node, " + checkAction(n);
  }
  


  public String visitSemPred(NodeSemPred n)
  {
    if (n.actionText.trim().isEmpty()) {
      return "no predicate code";
    }
    return checkAction(n);
  }
  
  public String visitSequence(NodeSequence n)
  {
    if (n.getChildCount() > 0) {
      return checkAction(n);
    }
    return "needs 1 or more child nodes, " + checkAction(n);
  }
  


  public String visitWildCard(NodeWildCard n)
  {
    return null;
  }
}
