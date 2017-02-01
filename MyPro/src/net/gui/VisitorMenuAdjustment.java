package net.gui;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
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


public class VisitorMenuAdjustment
  extends VisitorBase
{
  PopupMenuTree theMenu;
  
  public VisitorMenuAdjustment(PopupMenuTree theMenu)
  {
    this.theMenu = theMenu;
  }
  
  void commonSetting(NodeBase n) 
  {
    NodeBase parent = (NodeBase)n.getParent();
    boolean isInSequene = (parent != null) && ((parent instanceof NodeSequence));
    this.theMenu.dropMenuItem.setEnabled(isInSequene);
    this.theMenu.commitMenuItem.setEnabled(isInSequene);
    this.theMenu.commitMenuItem.setSelected((isInSequene) && (((NodeSequence)parent).commitIndex == parent.getIndex(n)));
    this.theMenu.packratMenuItem.setEnabled(false);
    this.theMenu.multiplicityOneItem.setEnabled(true);
    this.theMenu.multiplicityZeroOneItem.setEnabled((parent == null) || (!(parent instanceof NodeChoice)));
    this.theMenu.multiplicityZeroMoreItem.setEnabled((parent == null) || (!(parent instanceof NodeChoice)));
    this.theMenu.multiplicityOneMoreItem.setEnabled(true);
    this.theMenu.multiplicityNotItem.setEnabled((parent == null) || (!(parent instanceof NodeChoice)));
    this.theMenu.multiplicityGuardItem.setEnabled((parent == null) || (!(parent instanceof NodeChoice)));
    this.theMenu.multiplicityMenu.setEnabled(true);
    this.theMenu.traceMenuItem.setSelected(n.isTraced);
    this.theMenu.dropMenuItem.setSelected(n.isDropped);
    this.theMenu.packratMenuItem.setSelected(false);
    if (n.multiplicity == Multiplicity.One) 
    {
      this.theMenu.multiplicityOneItem.setSelected(true);
    } 
    else if (n.multiplicity == Multiplicity.ZeroOrOne) 
    {
      this.theMenu.multiplicityZeroOneItem.setSelected(true);
    } 
    else if (n.multiplicity == Multiplicity.ZeroOrMore) 
    {
      this.theMenu.multiplicityZeroMoreItem.setSelected(true);
    } 
    else if (n.multiplicity == Multiplicity.OneOrMore) 
    {
      this.theMenu.multiplicityOneMoreItem.setSelected(true);
    } 
    else if (n.multiplicity == Multiplicity.Not) 
    {
      this.theMenu.multiplicityNotItem.setSelected(true);
    } 
    else if (n.multiplicity == Multiplicity.Guard) 
    {
      this.theMenu.multiplicityGuardItem.setSelected(true);
    }
    this.theMenu.cutMenuItem.setEnabled(true);
    this.theMenu.copyMenuItem.setEnabled(true);
    this.theMenu.deleteMenuItem.setEnabled(true);
    this.theMenu.treePanel.addSemPredAction.setEnabled(false);
  }
  
  public Object visitChoice(NodeChoice n)
  {
    commonSetting(n);
    this.theMenu.addMenu.setEnabled(true);
    this.theMenu.pasteMenuItem.setEnabled(this.theMenu.treePanel.theClipBoard != null);
    this.theMenu.goToItem.setEnabled(false);
    return null;
  }
  
  public Object visitLiteral(NodeLiteral n)
  {
    commonSetting(n);
    this.theMenu.addMenu.setEnabled(false);
    this.theMenu.pasteMenuItem.setEnabled(false);
    this.theMenu.goToItem.setEnabled(false);
    return null;
  }
  
  public Object visitReference(NodeReference n)
  {
    commonSetting(n);
    this.theMenu.addMenu.setEnabled(false);
    this.theMenu.pasteMenuItem.setEnabled(false);
    this.theMenu.goToItem.setEnabled(true);
    return null;
  }
  
  public Object visitRegex(NodeRegex n)
  {
    commonSetting(n);
    this.theMenu.addMenu.setEnabled(false);
    this.theMenu.pasteMenuItem.setEnabled(false);
    this.theMenu.goToItem.setEnabled(true);
    return null;
  }
  
  public Object visitRepSep(NodeRepSep n)
  {
    commonSetting(n);
    this.theMenu.addMenu.setEnabled(n.getChildCount() < 2);
    this.theMenu.pasteMenuItem.setEnabled((n.getChildCount() < 2) && (this.theMenu.treePanel.theClipBoard != null));
    this.theMenu.multiplicityOneItem.setEnabled(false);
    this.theMenu.multiplicityZeroOneItem.setEnabled(false);
    this.theMenu.multiplicityNotItem.setEnabled(false);
    this.theMenu.multiplicityGuardItem.setEnabled(false);
    this.theMenu.goToItem.setEnabled(false);
    return null;
  }
  
  public Object visitRoot(NodeRoot n)
  {
    commonSetting(n);
    this.theMenu.packratMenuItem.setEnabled(true);
    this.theMenu.packratMenuItem.setSelected(n.isPackrat);
    this.theMenu.addMenu.setEnabled(n.getChildCount() == 0);
    this.theMenu.multiplicityMenu.setEnabled(false);
    this.theMenu.goToItem.setEnabled(false);
    this.theMenu.pasteMenuItem.setEnabled((n.getChildCount() == 0) && (this.theMenu.treePanel.theClipBoard != null));
    this.theMenu.cutMenuItem.setEnabled(false);
    this.theMenu.copyMenuItem.setEnabled(false);
    this.theMenu.deleteMenuItem.setEnabled(false);
    return null;
  }
  
  public Object visitSemPred(NodeSemPred n)
  {
    commonSetting(n);
    this.theMenu.addMenu.setEnabled(false);
    this.theMenu.pasteMenuItem.setEnabled(false);
    this.theMenu.goToItem.setEnabled(true);
    this.theMenu.multiplicityMenu.setEnabled(false);
    return null;
  }
  
  public Object visitSequence(NodeSequence n)
  {
    commonSetting(n);
    this.theMenu.addMenu.setEnabled(true);
    this.theMenu.goToItem.setEnabled(false);
    this.theMenu.pasteMenuItem.setEnabled(this.theMenu.treePanel.theClipBoard != null);
    this.theMenu.treePanel.addSemPredAction.setEnabled(true);
    return null;
  }
  
  public Object visitWildCard(NodeWildCard n)
  {
    commonSetting(n);
    this.theMenu.addMenu.setEnabled(false);
    this.theMenu.goToItem.setEnabled(false);
    this.theMenu.pasteMenuItem.setEnabled(false);
    this.theMenu.treePanel.addSemPredAction.setEnabled(false);
    return null;
  }
}
