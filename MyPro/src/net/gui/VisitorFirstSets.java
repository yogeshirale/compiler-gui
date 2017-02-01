package net.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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

public class VisitorFirstSets
  extends VisitorBase
{
  private Forest theForest;
  
  public VisitorFirstSets(Forest theForest)
  {
    this.theForest = theForest;
  }
  
  public Set<String>[] visitChoice(NodeChoice n)
  {
    int cc = n.getChildCount();
    NodeBase[] cn = new NodeBase[cc];
    ArrayList<Set<String>> firsts = new ArrayList();
    Set<String>[][] cFirsts = new Set[cc][];
    for (int i = 0; i < cc; i++) 
    {
      cn[i] = ((NodeBase)n.getChildAt(i));
      cFirsts[i] = ((Set[])(Set[])cn[i].accept(this));
      while (firsts.size() < cFirsts[i].length)
        firsts.add(new HashSet());
    }
    for (int i = 0; i < cc; i++) 
    {
      int cfLen = cFirsts[i].length;
      for (int j = 0; j < cfLen; j++) 
      {
        ((Set)firsts.get(j)).addAll(cFirsts[i][j]);
        if ((cn[i].multiplicity == Multiplicity.ZeroOrMore) || (cn[i].multiplicity == Multiplicity.OneOrMore)) 
        {
          while (firsts.size() < 5)
            firsts.add(new HashSet());
          for (int k = 1; j + cfLen * k < firsts.size(); k++) 
          {
            ((Set)firsts.get(j + cfLen * k)).addAll(cFirsts[i][j]);
            if (j == 0)
              ((Set)firsts.get(cfLen * k)).add("");
          }
        }
      }
      if (cfLen < firsts.size())
        ((Set)firsts.get(cfLen)).add("");
    }
    return (Set[])firsts.toArray(new Set[firsts.size()]);
  }
  
  public Set<String>[] visitLiteral(NodeLiteral n)
  {
    Set<String> ss = new HashSet();
    ss.add(n.literalName);
    return new Set[] { ss };
  }
  
  public Set<String>[] visitReference(NodeReference n)
  {
    String referredRuleName = n.refRuleName;
    NodeBase referredRule = (NodeBase)this.theForest.ruleBank.get(referredRuleName);
    return (Set[])referredRule.accept(this);
  }
  
  public Set<String>[] visitRegex(NodeRegex n)
  {
    Set<String> ss = new HashSet();
    ss.add(n.regexName);
    return new Set[] { ss };
  }
  
  public Set<String>[] visitRepSep(NodeRepSep n)
  {
    if (n.getChildCount() == 2) 
    {
      Set<String>[] rep = (Set[])((NodeBase)n.getChildAt(0)).accept(this);
      Set<String>[] sep = (Set[])((NodeBase)n.getChildAt(1)).accept(this);
      Set<String>[] retVal = (Set[])Arrays.copyOf(rep, 5);
      for (int i = 0; i < 5; i++) 
      {
        if (retVal[i] == null)
          retVal[i] = new HashSet();
      }
      for (int k = 0; rep.length + k * (rep.length + sep.length) < 5; k++) 
      {
        int off = rep.length + k * (rep.length + sep.length);
        retVal[off].add("");
        for (int i = 0; (i < sep.length) && (off + i < 5); i++)
          retVal[(off + i)].addAll(sep[i]);
        off += sep.length;
        for (int i = 0; (i < rep.length) && (off + i < 5); i++)
          retVal[(off + i)].addAll(rep[i]);
      }
      if (n.multiplicity == Multiplicity.ZeroOrMore)
        retVal[0].add("");
      return retVal;
    }
    return new Set[0];
  }
  

  public Set<String>[] visitRoot(NodeRoot n)
  {
    if (this.activeRules.contains(n.getName())) 
    {
      return new Set[0];
    }
    this.activeRules.add(n.getName());
    NodeBase child = (NodeBase)n.getFirstChild();
    Set<String>[] retVal = (Set[])child.accept(this);
    this.activeRules.remove(n.getName());
    return retVal;
  }
  
  public Set<String>[] visitSemPred(NodeSemPred n)
  {
    return new Set[0];
  }
  
  private boolean mergeSequence(Set<String>[] from, List<Set<String>> to, NodeBase n) 
  {
    int oldLength = to.size();
    int newLength = (n.multiplicity == Multiplicity.ZeroOrMore) || (n.multiplicity == Multiplicity.OneOrMore) ? 5 : Math.min(5, oldLength + from.length);  
    for (int i = oldLength; i < newLength; i++) 
    {
      HashSet<String> hs = new HashSet();
      if (i == oldLength)
        hs.add("");
      to.add(hs);
    }
    for (int i = newLength - 1; i >= 0; i--) 
    {
      if (((Set)to.get(i)).contains(""))
      {
        if ((n.multiplicity != Multiplicity.ZeroOrOne) && (n.multiplicity != Multiplicity.ZeroOrMore)) 
        {
          ((Set)to.get(i)).remove("");
        }
        if (i + from.length < newLength) 
        {
          ((Set)to.get(i + from.length)).add("");
        }
        for (int j = 0; (j < from.length) && (i + j < newLength); j++) 
        {
          int offs = i + j;
          ((Set)to.get(offs)).addAll(from[j]);
          if ((n.multiplicity == Multiplicity.OneOrMore) || (n.multiplicity == Multiplicity.ZeroOrMore))
            for (int offs2 = offs + from.length; offs2 < newLength; offs2 += from.length) 
            {
              ((Set)to.get(offs2)).addAll(from[j]);
              if (j == 0)
                ((Set)to.get(offs2)).add("");
            }
        }
      }
    }
    boolean more = false;
    for (int i = 0; (!more) && (i < newLength); i++)
      more = ((Set)to.get(i)).contains("");
    return (more) || (newLength < 5);
  }
  
  public Set<String>[] visitSequence(NodeSequence n)
  {
    List<Set<String>> firsts = new ArrayList();
    for (int i = 0; i < n.getChildCount(); i++) 
    {
      NodeBase c = (NodeBase)n.getChildAt(i);
      Set<String>[] f = (Set[])c.accept(this);
      if (!mergeSequence(f, firsts, c))
        break;
    }
    return (Set[])firsts.toArray(new Set[firsts.size()]);
  }
  
  public Set<String>[] visitWildCard(NodeWildCard n)
  {
    Set<String> ss = new HashSet();
    ss.add("WildCard");
    return new Set[] { ss };
  }

  private final int LA = 5;
  private Set<String> activeRules = new HashSet();
}
