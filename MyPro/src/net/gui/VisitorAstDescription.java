package net.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.api.Forest;
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

public class VisitorAstDescription
  extends VisitorBase
{
  private Vll4jGui gui;
  
  public VisitorAstDescription(Vll4jGui gui)
  {
    this.gui = gui;
  }
  
  public Object visitChoice(NodeChoice n)
  {
    if (this.level > this.maxDepth)
      return getSpaces(n) + "_";
    if ((n != this.gui.theTreePanel.selectedNode) && (!n.actionText.trim().isEmpty()))
      return String.format("%saction@%s", new Object[] { getSpaces(n), n.nodeName() });
    if (n.getChildCount() == 0)
      return getSpaces(n) + "?";
    StringBuilder sb = new StringBuilder();
    sb.append(getSpaces(n)).append("Choice(\n");
    this.level += 1;
    int cc = n.getChildCount();
    for (int i = 0; i < cc; i++) 
    {
      sb.append(getSpaces(n)).append("Array(").append(i);
      this.level += 1;
      String childAst = (String)((NodeBase)n.getChildAt(i)).accept(this);
      if (childAst.contains("\n")) 
      {
        sb.append(",\n").append(childAst).append("\n");
        this.level -= 1;
        sb.append(getSpaces(n)).append(")");
      } 
      else 
      {
        sb.append(", ").append(stripSpaces(childAst)).append(")");
        this.level -= 1;
      }
      if (i == cc - 1) 
      {
        sb.append("\n");
      } 
      else
        sb.append(",\n");
    }
    this.level -= 1;
    sb.append(getSpaces(n)).append(")");
    return withMultiplicity(sb.toString(), n);
  }
  
  public Object visitLiteral(NodeLiteral n)
  {
    if (this.level > this.maxDepth)
      return getSpaces(n) + "_";
    if ((n != this.gui.theTreePanel.selectedNode) && (!n.actionText.trim().isEmpty()))
      return String.format("%saction@%s", new Object[] { getSpaces(n), n.nodeName() });
    StringBuilder sb = new StringBuilder();
    sb.append(getSpaces(n)).append("\"").append(((String)this.gui.theForest.tokenBank.get(n.literalName)).substring(1)).append("\"");
    
    return withMultiplicity(sb.toString(), n);
  }
  
  public Object visitReference(NodeReference n)
  {
    if (this.level > this.maxDepth)
      return getSpaces(n) + "_";
    if ((n != this.gui.theTreePanel.selectedNode) && (!n.actionText.trim().isEmpty()))
      return String.format("%saction@%s", new Object[] { getSpaces(n), n.nodeName() });
    if (this.maxDepth == Integer.MAX_VALUE)
    {
      String ast = withMultiplicity((String)((NodeBase)this.gui.theForest.ruleBank.get(n.refRuleName)).accept(this), n);
      
      return ast;
    }
    StringBuilder sb = new StringBuilder();
    sb.append(getSpaces(n)).append("@").append(n.refRuleName);
    return withMultiplicity(sb.toString(), n);
  }
  
  public Object visitRegex(NodeRegex n)
  {
    if (this.level > this.maxDepth)
      return getSpaces(n) + "_";
    if ((n != this.gui.theTreePanel.selectedNode) && (!n.actionText.trim().isEmpty()))
      return String.format("%saction@%s", new Object[] { getSpaces(n), n.nodeName() });
    StringBuilder sb = new StringBuilder();
    sb.append(getSpaces(n)).append("[").append(n.regexName).append("]");
    return withMultiplicity(sb.toString(), n);
  }
  
  public Object visitRepSep(NodeRepSep n)
  {
    if (this.level > this.maxDepth)
      return getSpaces(n) + "_";
    if (n.getChildCount() == 0)
      return getSpaces(n) + "?";
    if ((n != this.gui.theTreePanel.selectedNode) && (!n.actionText.trim().isEmpty()))
      return String.format("%saction@%s", new Object[] { getSpaces(n), n.nodeName() });
    return withMultiplicity((String)((NodeBase)n.getChildAt(0)).accept(this), n);
  }
  
  public Object visitRoot(NodeRoot n)
  {
    if (n.getChildCount() == 0)
      return getSpaces(n) + "?";
    if ((n != this.gui.theTreePanel.selectedNode) && (!n.actionText.trim().isEmpty()))
      return String.format("%saction@%s", new Object[] { getSpaces(n), n.nodeName() });
    if (this.activeNodes.contains(n)) 
    {
      return getSpaces(n) + "_";
    }
    this.activeNodes.add(n);

    String ast = (String)((NodeBase)n.getChildAt(0)).accept(this);
    
    this.activeNodes.remove(n);
    return ast;
  }
  

  public Object visitSemPred(NodeSemPred n)
  {
    return getSpaces(n) + "_";
  }
  
  public Object visitSequence(NodeSequence n)
  {
    if (this.level > this.maxDepth)
      return getSpaces(n) + "_";
    if ((n != this.gui.theTreePanel.selectedNode) && (!n.actionText.trim().isEmpty()))
      return String.format("%saction@%s", new Object[] { getSpaces(n), n.nodeName() });
    if (n.getChildCount() == 0)
      return getSpaces(n) + "?";
    ArrayList<NodeBase> childNodes = childNodesInAST(n);
    int cc = childNodes.size();
    StringBuilder sb = new StringBuilder();
    if (cc == 0) 
    {
      sb.append(getSpaces(n)).append("Array()");
      return sb.toString(); 
  	}
    if (cc == 1) 
    {
      sb.append(((NodeBase)childNodes.get(0)).accept(this));
      return withMultiplicity(sb.toString(), n);
    }
    sb.append(getSpaces(n)).append("Array(\n");
    this.level += 1;
    for (int i = 0; i < cc; i++) 
    {
      sb.append(((NodeBase)childNodes.get(i)).accept(this));
      if (i == cc - 1) 
      {
        sb.append("\n");
      } else
        sb.append(",\n");
    }
    this.level -= 1;
    sb.append(getSpaces(n)).append(")");
    return withMultiplicity(sb.toString(), n);
  }
  
  private ArrayList<NodeBase> childNodesInAST(NodeSequence ns)
  {
    ArrayList<NodeBase> al = new ArrayList();
    for (int i = 0; i < ns.getChildCount(); i++) 
    {
      NodeBase nc = (NodeBase)ns.getChildAt(i);
      if ((nc.multiplicity != Multiplicity.Guard) && (nc.multiplicity != Multiplicity.Not) && (!nc.isDropped) && (!(nc instanceof NodeSemPred)))
      {
        al.add(nc); 
      }
    }
    return al;
  }
  
  private String withMultiplicity(String ast, NodeBase n) 
  {
    StringBuilder sb = new StringBuilder();
    if (n.multiplicity == Multiplicity.ZeroOrOne) 
    {
      if (ast.contains("\n")) 
      {
        sb.append(getSpaces(n)).append("Option(\n");
        sb.append(pad(ast)).append("\n");
        sb.append(getSpaces(n)).append(")");
      } 
      else 
      {
        sb.append(getSpaces(n)).append("Option(").append(stripSpaces(ast)).append(")");
      }
    } else if ((n.multiplicity == Multiplicity.OneOrMore) || (n.multiplicity == Multiplicity.ZeroOrMore))
    {
      if (ast.contains("\n")) 
      {
        sb.append(getSpaces(n)).append("List(\n");
        sb.append(pad(ast)).append("\n");
        sb.append(getSpaces(n)).append(")");
      } 
      else 
      {
        sb.append(getSpaces(n)).append("List(").append(stripSpaces(ast)).append(")");
      }
    } 
    else 
    {
      sb.append(ast);
    }
    if (!n.description.isEmpty())
      sb.append(" <").append(n.description).append(">");
    return sb.toString();
  }
  
  private String getSpaces(NodeBase n) 
  {
    if (this.level == 0)
      return "";
    if (this.level == 1)
      return this.spacer;
    if (this.spacers.containsKey(Integer.valueOf(this.level))) 
    {
      return (String)this.spacers.get(Integer.valueOf(this.level));
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < this.level; i++)
      sb.append(this.spacer);
    this.spacers.put(Integer.valueOf(this.level), sb.toString());
    return sb.toString();
  }
  
  private String stripSpaces(String a)
  {
    if (a.startsWith(this.spacer)) 
    {
      return stripSpaces(a.substring(this.spacer.length()));
    }
    return a;
  }
  
  private String pad(String a) 
  {
    StringBuilder sb = new StringBuilder();
    String[] lines = a.split("\n");
    boolean first = true;
    for (String s : lines) 
    {
      if (first) 
      {
        first = false;
      } 
      else 
      {
        sb.append("\n");
      }
      sb.append(this.spacer).append(s);
    }
    return sb.toString();
  }
  
  public Object visitWildCard(NodeWildCard n)
  {
    return getSpaces(n) + "*";
  }

  private String spacer = "|  ";
  private Map<Integer, String> spacers = new HashMap();
  int level = 0;
  int maxDepth = 0;
  private Set<NodeBase> activeNodes = new HashSet();
}
