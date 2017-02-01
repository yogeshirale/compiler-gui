package net.gui;

import java.io.PrintWriter;
import net.api.Multiplicity;
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
import net.combinator.Utils;

public class VisitorXmlGeneration
  extends VisitorBase
{
  private PrintWriter printWriter;
  
  VisitorXmlGeneration(PrintWriter printWriter)
  {
    this.printWriter = printWriter;
  }
  
  private void commonAttributes(NodeBase n, boolean solo) 
  {
    if (n.multiplicity != Multiplicity.One)
      this.printWriter.printf("Mult=\"%s\" ", new Object[] { n.multiplicity });
    if (!n.errorMessage.isEmpty())
      this.printWriter.printf("ErrMsg=\"%s\" ", new Object[] { Utils.encode4xml(n.errorMessage) });
    if (!n.description.isEmpty())
      this.printWriter.printf("Description=\"%s\" ", new Object[] { Utils.encode4xml(n.description) });
    if (!n.actionText.isEmpty())
      this.printWriter.printf("ActionText=\"%s\" ", new Object[] { Utils.encode4xml(n.actionText) });
    if (n.isDropped)
      this.printWriter.printf("Drop=\"true\" ", new Object[0]);
    this.printWriter.println(solo ? "/>" : ">");
  }
  
  private void space() 
  {
    for (int i = 0; i < this.depth; i++) 
    {
      this.printWriter.print("  ");
    }
  }
  
  public Object visitChoice(NodeChoice n) 
  {
    space();
    this.printWriter.printf("<Choice ", new Object[0]);
    commonAttributes(n, false);
    visitAllChildNodes(n);
    space();
    this.printWriter.println("</Choice>");
    return null;
  }
  
  public Object visitLiteral(NodeLiteral n)
  {
    space();
    this.printWriter.printf("<Token Ref=\"%s\" ", new Object[] { Utils.encode4xml(n.literalName) });
    commonAttributes(n, true);
    return null;
  }
  
  public Object visitReference(NodeReference n)
  {
    space();
    this.printWriter.printf("<Reference Ref=\"%s\" ", new Object[] { n.refRuleName });
    commonAttributes(n, true);
    return null;
  }
  
  public Object visitRegex(NodeRegex n)
  {
    space();
    this.printWriter.printf("<Token Ref=\"%s\" ", new Object[] { Utils.encode4xml(n.regexName) });
    commonAttributes(n, true);
    return null;
  }
  
  public Object visitRepSep(NodeRepSep n)
  {
    space();
    this.printWriter.printf("<RepSep ", new Object[0]);
    commonAttributes(n, false);
    visitAllChildNodes(n);
    space();
    this.printWriter.println("</RepSep>");
    return null;
  }
  
  public Object visitRoot(NodeRoot n)
  {
    this.depth = 2;
    space();
    this.printWriter.printf("<Parser Name=\"%s\" ", new Object[] { n.ruleName });
    if (n.isPackrat)
      this.printWriter.print("Packrat=\"true\" ");
    commonAttributes(n, false);
    visitAllChildNodes(n);
    space();
    this.printWriter.println("</Parser>");
    return null;
  }
  
  public Object visitSemPred(NodeSemPred n)
  {
    space();
    this.printWriter.printf("<SemPred ", new Object[0]);
    commonAttributes(n, true);
    return null;
  }
  
  public Object visitSequence(NodeSequence n)
  {
    space();
    if (n.commitIndex != Integer.MAX_VALUE) {
      this.printWriter.printf("<Sequence Commit=\"%s\" ", new Object[] { Integer.valueOf(n.commitIndex) });
    } else
      this.printWriter.printf("<Sequence ", new Object[0]);
    commonAttributes(n, false);
    visitAllChildNodes(n);
    space();
    this.printWriter.println("</Sequence>");
    return null;
  }
  
  public Object visitWildCard(NodeWildCard n)
  {
    space();
    this.printWriter.printf("<Token ", new Object[0]);
    commonAttributes(n, true);
    return null;
  }
}
