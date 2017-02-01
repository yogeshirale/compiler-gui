package net.java.vll.vll4j.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import net.java.vll.vll4j.api.Forest;
import net.java.vll.vll4j.api.Multiplicity;
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

public class PanelRuleTree extends JPanel implements javax.swing.event.TreeSelectionListener
{
  PanelRuleTree(Vll4jGui gui)
  {
    setLayout(new BorderLayout());
    this.gui = gui;
    this.selectedNode = (this.rootNode = (NodeRoot)gui.theForest.ruleBank.get("Main"));
    this.theModel = new DefaultTreeModel(this.rootNode);
    this.theTree = new JTree(this.theModel);
    ToolTipManager.sharedInstance().registerComponent(this.theTree);
    this.theTree.setDragEnabled(true);
    this.theTree.setTransferHandler(new TransferHandlerTree());
    this.theTree.setSelectionRow(0);
    this.theTree.getSelectionModel().setSelectionMode(1);
    this.theTree.setDragEnabled(true);
    this.theTree.setDropMode(javax.swing.DropMode.INSERT);
    this.theTree.setTransferHandler(new TransferHandlerTree());
    this.theTree.addTreeSelectionListener(this);
    this.theTree.addMouseListener(this.treePopupListener);
    this.theTree.setShowsRootHandles(true);
    this.theTree.setCellRenderer(new RendererRuleNode());
    add(new javax.swing.JScrollPane(this.theTree), "Center");
    this.treePopupMenu = new PopupMenuTree(this);
    JPanel btnPanel = new JPanel();
    btnPanel.setLayout(new BorderLayout());
    btnPanel.add(this.statusLabel, "Center");
    this.helpButton = new JButton(gui.theHelpFunctionsManager.displayHelpRuleTree)
    {
      public Dimension getPreferredSize() 
      {
        int h = super.getPreferredSize().height;
        return new Dimension(h, h);
      }
      
      public Dimension getMinimumSize() 
      {
        return getPreferredSize();
      }
    };
    btnPanel.add(this.helpButton, "East");
    add(btnPanel, "South");
  }
  
  void setRuleName(String ruleName) 
  {
    this.selectedNode = ((NodeBase)this.gui.theForest.ruleBank.get(ruleName));
    this.rootNode = ((NodeRoot)this.selectedNode);
    javax.swing.tree.TreeNode[] p = this.selectedNode.getPath();
    TreePath[] tp = new TreePath[p.length];
    for (int i = 0; i < tp.length; i++)
      tp[i] = new TreePath(p[i]);
    this.theTree.removeTreeSelectionListener(this);
    this.theModel.setRoot(this.rootNode);
    this.theTree.setSelectionPaths(tp);
    this.theTree.addTreeSelectionListener(this);
    this.theModel.nodeChanged(this.selectedNode);
    for (int i = 0; i < this.theTree.getRowCount(); i++) 
    {
      this.theTree.expandRow(i);
    }
    this.gui.theAstPanel.resetView();
    this.gui.theActionCodePanel.resetView();
    this.statusLabel.setText(String.format(" %s/%s", new Object[] { this.selectedNode.nodeType(), this.selectedNode.nodeName() }));
  }
  
  void resetNodeDisplay(final NodeBase node) 
  {
    javax.swing.SwingUtilities.invokeLater(new Runnable()
    {
      public void run() 
      {
        PanelRuleTree.this.theModel.nodeChanged(PanelRuleTree.this.selectedNode);
        if (PanelRuleTree.this.associatedNode != null) 
        {
          PanelRuleTree.this.theModel.nodeChanged(PanelRuleTree.this.associatedNode);
          PanelRuleTree.this.associatedNode = null;
        }
        PanelRuleTree.this.theTree.expandPath(new TreePath(PanelRuleTree.this.selectedNode.getPath()));
        PanelRuleTree.this.theTree.scrollPathToVisible(new TreePath(node));
        PanelRuleTree.this.gui.theAstPanel.resetView();
      }
    });
  }
  
  Action addChoiceAction = new AbstractAction("Choice", Resources.choice)
  {
    public void actionPerformed(ActionEvent e) 
    {
      NodeChoice newNode = new NodeChoice();
      PanelRuleTree.this.theModel.insertNodeInto(newNode, PanelRuleTree.this.selectedNode, PanelRuleTree.this.selectedNode.getChildCount());
      PanelRuleTree.this.resetNodeDisplay(newNode);
    }
  };
  
  Action addSequenceAction = new AbstractAction("Sequence", Resources.sequence)
  {
    public void actionPerformed(ActionEvent e) 
    {
      NodeSequence newNode = new NodeSequence();
      PanelRuleTree.this.theModel.insertNodeInto(newNode, PanelRuleTree.this.selectedNode, PanelRuleTree.this.selectedNode.getChildCount());
      PanelRuleTree.this.resetNodeDisplay(newNode);
    }
  };
  
  Action addRepSepAction = new AbstractAction("RepSep", Resources.repSep)
  {
    public void actionPerformed(ActionEvent e) 
    {
      NodeRepSep newNode = new NodeRepSep();
      PanelRuleTree.this.theModel.insertNodeInto(newNode, PanelRuleTree.this.selectedNode, PanelRuleTree.this.selectedNode.getChildCount());
      PanelRuleTree.this.resetNodeDisplay(newNode);
    }
  };
  
  Action addSemPredAction = new AbstractAction("SemPred", Resources.predicate)
  {
    public void actionPerformed(ActionEvent e) 
    {
      NodeSemPred newNode = new NodeSemPred();
      PanelRuleTree.this.theModel.insertNodeInto(newNode, PanelRuleTree.this.selectedNode, PanelRuleTree.this.selectedNode.getChildCount());
      PanelRuleTree.this.resetNodeDisplay(newNode);
    }
  };
  
  Action addWildCardAction = new AbstractAction("WildCard", Resources.wildCard)
  {
    public void actionPerformed(ActionEvent e) 
    {
      NodeWildCard newNode = new NodeWildCard();
      PanelRuleTree.this.theModel.insertNodeInto(newNode, PanelRuleTree.this.selectedNode, PanelRuleTree.this.selectedNode.getChildCount());
      PanelRuleTree.this.resetNodeDisplay(newNode);
    }
  };
  
  Action goToAction = new AbstractAction("Go to")
  {
    public void actionPerformed(ActionEvent e) 
    {
      if ((PanelRuleTree.this.selectedNode instanceof NodeReference)) 
      {
        String ruleName = ((NodeReference)PanelRuleTree.this.selectedNode).refRuleName;
        PanelRuleTree.this.gui.theRuleManager.theComboBox.setSelectedItem(ruleName);
      } 
      else if ((PanelRuleTree.this.selectedNode instanceof NodeLiteral)) 
      {
        String ruleName = ((NodeLiteral)PanelRuleTree.this.selectedNode).literalName;
        PanelRuleTree.this.gui.theTokenManager.editToken(ruleName);
      } 
      else if ((PanelRuleTree.this.selectedNode instanceof NodeRegex)) 
      {
        String ruleName = ((NodeRegex)PanelRuleTree.this.selectedNode).regexName;
        PanelRuleTree.this.gui.theTokenManager.editToken(ruleName);
      }
    }
  };
  
  Action addTokenAction = new AbstractAction("Token", Resources.token)
  {
    public void actionPerformed(ActionEvent e) 
    {
      Object[] names = PanelRuleTree.this.gui.theForest.tokenBank.keySet().toArray();
      if (names.length == 0) 
      {
        JOptionPane.showMessageDialog(PanelRuleTree.this.treePopupMenu, "No tokens defined yet", "ERROR - Add token", 0);
        return;
      }
      String token = (String)JOptionPane.showInputDialog(PanelRuleTree.this.gui, "Select token", "Add token", 3, null, names, names[0]);
      
      if (token == null)
        return;
      String pattern = (String)PanelRuleTree.this.gui.theForest.tokenBank.get(token);
      boolean isRegex = pattern.charAt(0) == 'R';
      NodeBase newNode = isRegex ? new NodeRegex(token) : new NodeLiteral(token);
      PanelRuleTree.this.theModel.insertNodeInto(newNode, PanelRuleTree.this.selectedNode, PanelRuleTree.this.selectedNode.getChildCount());
      PanelRuleTree.this.resetNodeDisplay(newNode);
    }
  };
  
  Action addReferenceAction = new AbstractAction("Reference", Resources.reference)
  {
    public void actionPerformed(ActionEvent e) 
    {
      Object[] names = PanelRuleTree.this.gui.theForest.ruleBank.keySet().toArray();
      String rule = (String)JOptionPane.showInputDialog(PanelRuleTree.this.gui, "Select rule", "Add reference", 3, null, names, names[0]);
      
      if (rule == null)
        return;
      NodeReference newNode = new NodeReference(rule);
      PanelRuleTree.this.theModel.insertNodeInto(newNode, PanelRuleTree.this.selectedNode, PanelRuleTree.this.selectedNode.getChildCount());
      PanelRuleTree.this.resetNodeDisplay(newNode);
    }
  };
  
  Action multiplicityOneAction = new AbstractAction("1 (just one)")
  {
    public void actionPerformed(ActionEvent e) 
    {
      PanelRuleTree.this.selectedNode.multiplicity = Multiplicity.One;
      PanelRuleTree.this.resetNodeDisplay(PanelRuleTree.this.selectedNode);
    }
  };
  
  Action multiplicityZeroOneAction = new AbstractAction("? (0 or 1)")
  {
    public void actionPerformed(ActionEvent e) 
    {
      PanelRuleTree.this.selectedNode.multiplicity = Multiplicity.ZeroOrOne;
      PanelRuleTree.this.resetNodeDisplay(PanelRuleTree.this.selectedNode);
    }
  };
  
  Action multiplicityZeroMoreAction = new AbstractAction("* (0 or more)")
  {
    public void actionPerformed(ActionEvent e) 
    {
      PanelRuleTree.this.selectedNode.multiplicity = Multiplicity.ZeroOrMore;
      PanelRuleTree.this.resetNodeDisplay(PanelRuleTree.this.selectedNode);
    }
  };
  
  Action multiplicityOneMoreAction = new AbstractAction("+ (1 or more)")
  {
    public void actionPerformed(ActionEvent e) 
    {
      PanelRuleTree.this.selectedNode.multiplicity = Multiplicity.OneOrMore;
      PanelRuleTree.this.resetNodeDisplay(PanelRuleTree.this.selectedNode);
    }
  };
  
  Action multiplicityNotAction = new AbstractAction("0 (not)")
  {
    public void actionPerformed(ActionEvent e) 
    {
      PanelRuleTree.this.selectedNode.multiplicity = Multiplicity.Not;
      PanelRuleTree.this.resetNodeDisplay(PanelRuleTree.this.selectedNode);
    }
  };
  
  Action multiplicityGuardAction = new AbstractAction("= (guard)")
  {
    public void actionPerformed(ActionEvent e) 
    {
      PanelRuleTree.this.selectedNode.multiplicity = Multiplicity.Guard;
      PanelRuleTree.this.resetNodeDisplay(PanelRuleTree.this.selectedNode);
    }
  };
  
  Action cutAction = new AbstractAction("Cut", Resources.cut16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      NodeBase parent = (NodeBase)PanelRuleTree.this.selectedNode.getParent();
      TreePath parentPath = PanelRuleTree.this.theTree.getSelectionPath().getParentPath();
      NodeBase nodeToCut = PanelRuleTree.this.selectedNode;
      PanelRuleTree.this.theClipBoard = nodeToCut;
      PanelRuleTree.this.theClipBoard.isDropped = false;
      PanelRuleTree.this.theClipBoard.isTraced = false;
      PanelRuleTree.this.theTree.setSelectionPath(parentPath);
      PanelRuleTree.this.theModel.removeNodeFromParent(nodeToCut);
      PanelRuleTree.this.resetNodeDisplay(parent);
    }
  };
  
  Action copyAction = new AbstractAction("Copy", Resources.copy16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      PanelRuleTree.this.theClipBoard = ((NodeBase)PanelRuleTree.this.selectedNode.clone());
      PanelRuleTree.this.theClipBoard.isDropped = false;
      PanelRuleTree.this.theClipBoard.isTraced = false;
    }
  };
  
  Action pasteAction = new AbstractAction("Paste", Resources.paste16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      NodeBase newNode = (NodeBase)PanelRuleTree.this.theClipBoard.clone();
      PanelRuleTree.this.theModel.insertNodeInto(newNode, PanelRuleTree.this.selectedNode, PanelRuleTree.this.selectedNode.getChildCount());
      PanelRuleTree.this.resetNodeDisplay(PanelRuleTree.this.selectedNode);
    }
  };
  
  Action deleteAction = new AbstractAction("Delete", Resources.delete16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      NodeBase parent = (NodeBase)PanelRuleTree.this.selectedNode.getParent();
      TreePath parentPath = PanelRuleTree.this.theTree.getSelectionPath().getParentPath();
      PanelRuleTree.this.theModel.removeNodeFromParent(PanelRuleTree.this.selectedNode);
      PanelRuleTree.this.theTree.setSelectionPath(parentPath);
      PanelRuleTree.this.resetNodeDisplay(parent);
    }
  };
  
  Action packratAction = new AbstractAction("Packrat")
  {
    public void actionPerformed(ActionEvent e) 
    {
      ((NodeRoot)PanelRuleTree.this.selectedNode).isPackrat = ((JCheckBoxMenuItem)e.getSource()).isSelected();
      PanelRuleTree.this.resetNodeDisplay(PanelRuleTree.this.selectedNode);
    }
  };
  
  Action traceAction = new AbstractAction("Trace")
  {
    public void actionPerformed(ActionEvent e) 
    {
      PanelRuleTree.this.selectedNode.isTraced = ((JCheckBoxMenuItem)e.getSource()).isSelected();
      PanelRuleTree.this.resetNodeDisplay(PanelRuleTree.this.selectedNode);
    }
  };
  
  Action dropAction = new AbstractAction("Drop")
  {
    public void actionPerformed(ActionEvent e) 
    {
      PanelRuleTree.this.selectedNode.isDropped = ((JCheckBoxMenuItem)e.getSource()).isSelected();
      PanelRuleTree.this.resetNodeDisplay(PanelRuleTree.this.selectedNode);
    }
  };
  
  Action commitAction = new AbstractAction("Commit")
  {
    public void actionPerformed(ActionEvent e) 
    {
      NodeSequence parent = (NodeSequence)PanelRuleTree.this.selectedNode.getParent();
      int myIndex = parent.getIndex(PanelRuleTree.this.selectedNode);
      int oldIndex = parent.commitIndex;
      if (parent.commitIndex == myIndex) 
      {
        parent.commitIndex = Integer.MAX_VALUE;
      } 
      else 
      {
        parent.commitIndex = myIndex;
      }
      PanelRuleTree.this.associatedNode = ((myIndex == oldIndex) || (oldIndex >= parent.getChildCount()) ? null : (NodeBase)parent.getChildAt(oldIndex));
      
      PanelRuleTree.this.resetNodeDisplay(PanelRuleTree.this.selectedNode);
    }
  };
  
  Action descriptionAction = new AbstractAction("Description")
  {
    public void actionPerformed(ActionEvent e) 
    {
      String descr = (String)JOptionPane.showInputDialog(PanelRuleTree.this.gui, "Enter description", "Description", 3, null, null, PanelRuleTree.this.selectedNode.description);
      
      if ((descr == null) || (descr.equals(PanelRuleTree.this.selectedNode.description)))
        return;
      PanelRuleTree.this.selectedNode.description = descr;
      PanelRuleTree.this.resetNodeDisplay(PanelRuleTree.this.selectedNode);
    }
  };
  
  Action errorMessageAction = new AbstractAction("Error message")
  {
    public void actionPerformed(ActionEvent e) 
    {
      String msg = (String)JOptionPane.showInputDialog(PanelRuleTree.this.gui, "Enter error message", "Error message", 3, null, null, PanelRuleTree.this.selectedNode.errorMessage);
      
      if ((msg == null) || (msg.equals(PanelRuleTree.this.selectedNode.errorMessage)))
        return;
      PanelRuleTree.this.selectedNode.errorMessage = msg;
      PanelRuleTree.this.resetNodeDisplay(PanelRuleTree.this.selectedNode);
    }
  };
  
  Action firstsAction = new AbstractAction("First-k-sets")
  {
    public void actionPerformed(ActionEvent e) 
    {
      VisitorFirstSets vfs = new VisitorFirstSets(PanelRuleTree.this.gui.theForest);
      Set<String>[] firsts = (Set[])PanelRuleTree.this.selectedNode.accept(vfs);
      StringBuilder sb = new StringBuilder();
      for (Set<String> ss : firsts) 
      {
        if (sb.length() > 0)
          sb.append(",\n");
        sb.append('{');
        for (String s : ss) 
        {
          if (sb.charAt(sb.length() - 1) != '{')
            sb.append(", ");
          sb.append(s.equals("") ? "ε" : s);
        }
        sb.append("}");
      }
      JOptionPane.showMessageDialog(PanelRuleTree.this.gui, sb.toString(), "First-k-sets", -1); } };
  PopupMenuTree treePopupMenu;
  NodeRoot rootNode;
  private DefaultTreeModel theModel;
  Vll4jGui gui;
  JTree theTree;
  
  public void valueChanged(TreeSelectionEvent e) 
  { 
  	this.selectedNode = ((NodeBase)e.getPath().getLastPathComponent());
    this.gui.theActionCodePanel.resetView();
    this.gui.theAstPanel.resetView();
    this.statusLabel.setText(String.format(" %s/%s", new Object[] { this.selectedNode.nodeType(), this.selectedNode.nodeName() }));
  }
  
  NodeBase selectedNode = null; NodeBase associatedNode = null;
  PopupListenerTree treePopupListener = new PopupListenerTree(this);
  NodeBase theClipBoard = null;
  private JButton helpButton = null;
  private JLabel statusLabel = new JLabel("  ");
}
