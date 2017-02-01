package net.java.vll.vll4j.api;




















public abstract class VisitorBase
{
  protected void visitAllChildNodes(NodeBase n)
  {
    int childCount = n.getChildCount();
    for (int i = 0; i < childCount; i++) {
      this.depth += 1;
      ((NodeBase)n.getChildAt(i)).accept(this);
      this.depth -= 1;
    }
  }
  










  protected int depth = 0;
  
  public abstract Object visitChoice(NodeChoice paramNodeChoice);
  
  public abstract Object visitLiteral(NodeLiteral paramNodeLiteral);
  
  public abstract Object visitReference(NodeReference paramNodeReference);
  
  public abstract Object visitRegex(NodeRegex paramNodeRegex);
  
  public abstract Object visitRepSep(NodeRepSep paramNodeRepSep);
  
  public abstract Object visitRoot(NodeRoot paramNodeRoot);
  
  public abstract Object visitSemPred(NodeSemPred paramNodeSemPred);
  
  public abstract Object visitSequence(NodeSequence paramNodeSequence);
  
  public abstract Object visitWildCard(NodeWildCard paramNodeWildCard);
}