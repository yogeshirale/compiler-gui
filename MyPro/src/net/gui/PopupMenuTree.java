package net.gui;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import net.api.NodeBase;

public class PopupMenuTree
  extends JPopupMenu
{
  PanelRuleTree treePanel;
  
  PopupMenuTree(PanelRuleTree treePanel)
  {
    this.treePanel = treePanel;
    createPopup();
  }
  
  private void createPopup() 
  {
    ButtonGroup bg = new ButtonGroup();
    this.multiplicityOneItem = new JRadioButtonMenuItem(this.treePanel.multiplicityOneAction);
    bg.add(this.multiplicityOneItem);
    this.multiplicityZeroMoreItem = new JRadioButtonMenuItem(this.treePanel.multiplicityZeroMoreAction);
    bg.add(this.multiplicityZeroMoreItem);
    this.multiplicityOneMoreItem = new JRadioButtonMenuItem(this.treePanel.multiplicityOneMoreAction);
    bg.add(this.multiplicityOneMoreItem);
    this.multiplicityZeroOneItem = new JRadioButtonMenuItem(this.treePanel.multiplicityZeroOneAction);
    bg.add(this.multiplicityZeroOneItem);
    this.multiplicityNotItem = new JRadioButtonMenuItem(this.treePanel.multiplicityNotAction);
    bg.add(this.multiplicityNotItem);
    this.multiplicityGuardItem = new JRadioButtonMenuItem(this.treePanel.multiplicityGuardAction);
    bg.add(this.multiplicityGuardItem);
    this.goToItem = new JMenuItem(this.treePanel.goToAction);
    add(this.goToItem);
    add(new JSeparator());
    add(this.addMenu);
    this.addMenu.add(this.treePanel.addTokenAction);
    this.addMenu.add(this.treePanel.addReferenceAction);
    this.addMenu.add(new JSeparator());
    this.addMenu.add(this.treePanel.addChoiceAction);
    this.addMenu.add(this.treePanel.addSequenceAction);
    this.addMenu.add(this.treePanel.addRepSepAction);
    this.addMenu.add(new JSeparator());
    this.addMenu.add(this.treePanel.addSemPredAction);
    this.addMenu.add(this.treePanel.addWildCardAction);
    add(this.multiplicityMenu);
    this.multiplicityMenu.add(this.multiplicityOneItem);
    this.multiplicityMenu.add(this.multiplicityZeroMoreItem);
    this.multiplicityMenu.add(this.multiplicityOneMoreItem);
    this.multiplicityMenu.add(this.multiplicityZeroOneItem);
    this.multiplicityMenu.add(new JSeparator());
    this.multiplicityMenu.add(this.multiplicityNotItem);
    this.multiplicityMenu.add(this.multiplicityGuardItem);
    add(this.editMenu);
    this.cutMenuItem = new JMenuItem(this.treePanel.cutAction);
    this.editMenu.add(this.cutMenuItem);
    this.copyMenuItem = new JMenuItem(this.treePanel.copyAction);
    this.editMenu.add(this.copyMenuItem);
    this.pasteMenuItem = new JMenuItem(this.treePanel.pasteAction);
    this.editMenu.add(this.pasteMenuItem);
    this.deleteMenuItem = new JMenuItem(this.treePanel.deleteAction);
    this.editMenu.add(this.deleteMenuItem);
    add(new JSeparator());
    this.dropMenuItem = new JCheckBoxMenuItem(this.treePanel.dropAction);
    add(this.dropMenuItem);
    this.commitMenuItem = new JCheckBoxMenuItem(this.treePanel.commitAction);
    add(this.commitMenuItem);
    this.packratMenuItem = new JCheckBoxMenuItem(this.treePanel.packratAction);
    add(this.packratMenuItem);
    this.errorMessageItem = new JMenuItem(this.treePanel.errorMessageAction);
    add(this.errorMessageItem);
    this.descriptionMenuItem = new JMenuItem(this.treePanel.descriptionAction);
    add(this.descriptionMenuItem);
    add(new JSeparator());
    this.traceMenuItem = new JCheckBoxMenuItem(this.treePanel.traceAction);
    add(this.traceMenuItem);
  }

  void adjustMenu()
  {
    NodeBase selectedNode = this.treePanel.selectedNode;
    selectedNode.accept(this.visitorAdjustMenu);
  }
  
  VisitorMenuAdjustment visitorAdjustMenu = new VisitorMenuAdjustment(this);
  JMenuItem goToItem;
  JMenu addMenu = new JMenu("Add");
  JMenu multiplicityMenu = new JMenu("Multiplicity");
  JRadioButtonMenuItem multiplicityOneItem = null;
  JRadioButtonMenuItem multiplicityZeroMoreItem = null;
  JRadioButtonMenuItem multiplicityOneMoreItem = null;
  JRadioButtonMenuItem multiplicityZeroOneItem = null;
  JRadioButtonMenuItem multiplicityNotItem = null;
  JRadioButtonMenuItem multiplicityGuardItem = null;
  JMenu editMenu = new JMenu("Edit");
  JMenuItem cutMenuItem;
  JMenuItem copyMenuItem;
  JMenuItem pasteMenuItem;
  JMenuItem deleteMenuItem;
  JMenuItem dropMenuItem;
  JMenuItem commitMenuItem;
  JMenuItem packratMenuItem;
  JMenuItem errorMessageItem;
  JMenuItem descriptionMenuItem;
  JMenuItem traceMenuItem;
  JMenuItem firstsMenuItem;
}