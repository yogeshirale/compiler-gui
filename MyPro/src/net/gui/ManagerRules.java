package net.gui;

import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.Stack;
import java.util.TreeSet;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import net.api.Forest;
import net.api.NodeBase;
import net.api.NodeRoot;

public class ManagerRules
{
  ManagerRules(Vll4jGui gui)
  {
    this.gui = gui;
  }
  
  void reset() 
  {
    this.ruleStack.clear();
    this.ruleBackAction.setEnabled(false);
    this.lastRulePopped = "";
  }
  
  Action ruleBackAction = new AbstractAction("Back", Resources.back16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      ManagerRules.this.lastRulePopped = ((String)ManagerRules.this.ruleStack.pop());
      ManagerRules.this.theComboBox.setSelectedItem(ManagerRules.this.lastRulePopped);
      setEnabled(!ManagerRules.this.ruleStack.isEmpty());
    }
  };
  
  void addRuleToComboBox(String ruleName) 
  {
    this.theComboBox.setAction(null);
    int oldLength = this.theComboBox.getItemCount();
    for (int i = this.theComboBox.getItemCount() - 1; i >= 0; i--) 
    {
      String item = (String)this.theComboBox.getItemAt(i);
      if (ruleName.compareTo(item) > 0) 
      {
        this.theComboBox.insertItemAt(ruleName, i + 1);
        break; 
      }
      if (ruleName.compareTo(item) == 0) 
      {
        JOptionPane.showMessageDialog(this.gui, "A rule with this name already exists", "WARNING - New rule", 2);
        
        break; 
      }
      if (i == 0) 
      {
        this.theComboBox.insertItemAt(ruleName, 0);
      }
    }
    if (oldLength != this.theComboBox.getItemCount()) 
    {
      this.theComboBox.setMaximumSize(this.theComboBox.getPreferredSize());
      this.theComboBox.setSelectedItem(ruleName);
      this.gui.theTreePanel.setRuleName(ruleName);
    }
    this.theComboBox.setAction(this.comboBoxAction);
  }
  
  void removeRuleFromComboBox(String ruleName) 
  {
    int idx = this.theComboBox.getSelectedIndex();
    this.theComboBox.setAction(null);
    this.theComboBox.removeItemAt(idx);
    this.theComboBox.setAction(this.comboBoxAction);
    String nextItem = idx >= this.theComboBox.getItemCount() ? (String)this.theComboBox.getItemAt(idx == 0 ? 0 : idx - 1) : (String)this.theComboBox.getItemAt(idx);
    
    this.gui.theTreePanel.setRuleName(nextItem);
  }
  
  Action ruleNewAction = new AbstractAction("New rule", Resources.newReference)
  {
    public void actionPerformed(ActionEvent e) 
    {
      String ruleName = JOptionPane.showInputDialog(ManagerRules.this.gui, "Enter rule name", "New rule", 3);
      if (ruleName == null)
        return;
      ruleName = ruleName.trim();
      if (ManagerRules.this.gui.theForest.ruleBank.containsKey(ruleName)) 
      {
        JOptionPane.showMessageDialog(ManagerRules.this.gui, "Rule name already used", "WARNING - New rule", 2);
        return;
      }
      if (!ruleName.matches("[a-zA-Z$_][a-zA-Z$_0-9-]*")) 
      {
        JOptionPane.showMessageDialog(ManagerRules.this.gui, "Illegal rule name", "WARNING - New rule", 2);
        return;
      }
      ManagerRules.this.ruleStack.add((String)ManagerRules.this.theComboBox.getSelectedItem());
      ManagerRules.this.ruleBackAction.setEnabled(!ManagerRules.this.ruleStack.isEmpty());
      ManagerRules.this.gui.theForest.ruleBank.put(ruleName, new NodeRoot(ruleName));
      ManagerRules.this.addRuleToComboBox(ruleName);
    }
  };
  
  private String[] findRuleInRules(String rule) 
  {
    VisitorRuleSearch v = new VisitorRuleSearch(rule);
    for (NodeBase n : this.gui.theForest.ruleBank.values()) 
    {
      n.accept(v);
    }
    return (String[])v.ruleSet.toArray(new String[v.ruleSet.size()]);
  }
  
  Action ruleFindAction = new AbstractAction("Find rule", Resources.search16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      String ruleName = (String)ManagerRules.this.theComboBox.getSelectedItem();
      String[] rules = ManagerRules.this.findRuleInRules(ruleName);
      if (rules.length == 0) 
      {
        JOptionPane.showMessageDialog(ManagerRules.this.gui, String.format("Rule '%s' isn't referred in any other rule", new Object[] { ruleName }), "WARNING - Find rule", 2);
        return;
      }
      String selectedRule = (String)JOptionPane.showInputDialog(ManagerRules.this.gui, String.format("Rule '%s' is referred in rules listed below\nClick OK to display selected rule", new Object[] { ruleName }), "Find rule", 3, null, rules, rules[0]);
      

      if (selectedRule != null) 
      {
        ManagerRules.this.theComboBox.setSelectedItem(selectedRule);
      }
    }
  };
  
  Action ruleRenameAction = new AbstractAction("Rename rule", Resources.refresh16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      String currentName = (String)ManagerRules.this.theComboBox.getSelectedItem();
      String newName = (String)JOptionPane.showInputDialog(ManagerRules.this.gui, "Enter new name", "Rename rule", 3, null, null, currentName);
      
      if (newName == null)
        return;
      newName = newName.trim();
      if (!newName.matches("[a-zA-Z$_][a-zA-Z$_0-9-]*")) 
      {
        JOptionPane.showMessageDialog(ManagerRules.this.gui, "Illegal rule name", "WARNING - Rename rule", 2);
        return;
      }
      if (ManagerRules.this.gui.theForest.ruleBank.containsKey(newName)) 
      {
        JOptionPane.showMessageDialog(ManagerRules.this.gui, "Rule name already used", "WARNING - Rename rule", 2);
        return;
      }
      VisitorRuleRenaming v = new VisitorRuleRenaming(currentName, newName);
      for (NodeBase n : ManagerRules.this.gui.theForest.ruleBank.values()) 
      {
        n.accept(v);
      }
      
      NodeBase n = (NodeBase)ManagerRules.this.gui.theForest.ruleBank.get(currentName);
      ManagerRules.this.gui.theForest.ruleBank.put(newName, n);
      ManagerRules.this.gui.theForest.ruleBank.remove(currentName);
      if (ManagerRules.this.theComboBox.getItemCount() == 1) 
      {
        ManagerRules.this.theComboBox.setAction(null);
        ManagerRules.this.theComboBox.removeAllItems();
        ManagerRules.this.theComboBox.addItem(newName);
        ManagerRules.this.theComboBox.setAction(ManagerRules.this.comboBoxAction);
        ManagerRules.this.theComboBox.setMaximumSize(ManagerRules.this.theComboBox.getPreferredSize());
        ManagerRules.this.gui.theTreePanel.setRuleName(newName);
      } 
      else 
      {
        ManagerRules.this.removeRuleFromComboBox(currentName);
        ManagerRules.this.addRuleToComboBox(newName);
        for (int i = 0; i < ManagerRules.this.ruleStack.size(); i++) 
        {
          if (((String)ManagerRules.this.ruleStack.elementAt(i)).equals(currentName)) 
          {
            ManagerRules.this.ruleStack.setElementAt(newName, i);
          }
        }
      }
    }
  };
  Action ruleDeleteAction = new AbstractAction("Delete rule", Resources.delete16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      if (ManagerRules.this.theComboBox.getItemCount() == 1) 
      {
        JOptionPane.showMessageDialog(ManagerRules.this.gui, "Cant delete only rule - rename instead", "WARNING - Delete rule", 2);
        return;
      }
      String ruleToDelete = (String)ManagerRules.this.theComboBox.getSelectedItem();
      if (ruleToDelete == null)
        return;
      String[] refdRules = ManagerRules.this.findRuleInRules(ruleToDelete);
      if (refdRules.length != 0) 
      {
        String selectedRule = (String)JOptionPane.showInputDialog(ManagerRules.this.gui, String.format("Cant delete rule '%s'\nused in rules listed below\nClick OK to display a rule", new Object[] { ruleToDelete }), "WARNING - Delete rule", 2, null, refdRules, refdRules[0]);
        


        if (selectedRule != null)
          ManagerRules.this.theComboBox.setSelectedItem(selectedRule);
        return;
      }
      if (0 != JOptionPane.showConfirmDialog(ManagerRules.this.gui, String.format("Delete rule '%s'?", new Object[] { ruleToDelete }), "Delete rule", 0, 3)) 
      {
        return;
      }
      ManagerRules.this.gui.theForest.ruleBank.remove(ruleToDelete);
      ManagerRules.this.removeRuleFromComboBox(ruleToDelete);
      ManagerRules.this.ruleStack.remove(ruleToDelete);
      ManagerRules.this.ruleBackAction.setEnabled(!ManagerRules.this.ruleStack.isEmpty());
    }
  };
  
  Action ruleOptimizeAction = new AbstractAction("Optimize rule", Resources.preferences16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      JOptionPane.showMessageDialog(ManagerRules.this.gui, "Rule optimization coming soon!", "Optimize rule", -1);
    }
  };
  

  Action comboBoxAction = new AbstractAction()
  {
    public void actionPerformed(ActionEvent e) 
    {
      String selectedRuleName = (String)ManagerRules.this.theComboBox.getSelectedItem();
      String currentRuleName = ManagerRules.this.gui.theTreePanel.rootNode.ruleName;
      if (selectedRuleName.equals(currentRuleName))
        return;
      if (!selectedRuleName.equals(ManagerRules.this.lastRulePopped)) 
      {
        ManagerRules.this.ruleStack.add(ManagerRules.this.gui.theTreePanel.rootNode.ruleName);
        ManagerRules.this.ruleBackAction.setEnabled(!ManagerRules.this.ruleStack.isEmpty());
      }
      ManagerRules.this.gui.theTreePanel.setRuleName(selectedRuleName);
      ManagerRules.this.gui.theActionCodePanel.resetView();
    }
  };
  
  Vll4jGui gui;
  JComboBox<String> theComboBox;
  private Stack<String> ruleStack = new Stack();
  private String lastRulePopped = "";
}
