package net.java.vll.vll4j.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import net.java.vll.vll4j.api.NodeBase;

public class PopupListenerTree
  extends MouseAdapter
{
  PanelRuleTree treePanel;
  
  PopupListenerTree(PanelRuleTree treePanel)
  {
    this.treePanel = treePanel;
  }
  
  public void mousePressed(MouseEvent me)
  {
    popup(me);
  }
  
  public void mouseReleased(MouseEvent me)
  {
    popup(me);
  }
  
  void popup(MouseEvent e) 
  {
    if (e.isPopupTrigger()) 
    {
      TreePath path = this.treePanel.theTree.getPathForLocation(e.getX(), e.getY());
      if (path != null) {
        this.treePanel.theTree.setSelectionPath(path);
        this.treePanel.selectedNode = ((NodeBase)path.getLastPathComponent());
        this.treePanel.treePopupMenu.adjustMenu();
        this.treePanel.treePopupMenu.show(e.getComponent(), e.getX(), e.getY());
      }
    }
  }
}
