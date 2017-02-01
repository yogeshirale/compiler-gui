package net.java.vll.vll4j.gui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import net.java.vll.vll4j.api.Forest;
import net.java.vll.vll4j.api.NodeBase;
import net.java.vll.vll4j.combinator.Utils;

public class ManagerTokens
{
  ManagerTokens(Vll4jGui theGui)
  {
    this.gui = theGui;
  }
  
  private String[] getTokenInfo(boolean isRegex) 
  {
    String title = String.format("New %s", new Object[] { isRegex ? "regex" : "literal" });
    Pattern inPattern = Pattern.compile(isRegex ? "([^\\s]+)|([a-zA-Z][a-zA-Z0-9$_]*(?::\\-?\\d+)?)\\s+(.+)" : "([^\\s]+)|([a-zA-Z][a-zA-Z0-9$_]*)\\s+(.+)");
    
    String input = JOptionPane.showInputDialog(this.gui, "Enter: name & spaces (optional), pattern", title, 3);
    
    if ((input == null) || (input.trim().length() == 0))
      return null;
    Matcher m = inPattern.matcher(input.trim());
    if (!m.matches()) 
    {
      JOptionPane.showMessageDialog(this.gui, "Bad format\nExpected name, space(s), pattern", "WARNING - " + title, 2);
      
      return null;
    }
    String tokenName = m.group(1) == null ? m.group(2) : String.format("\"%s\"", new Object[] { m.group(0) });
    if (this.gui.theForest.tokenBank.containsKey(tokenName)) 
    {
      JOptionPane.showMessageDialog(this.gui, "Name conflict\nA token with this name already exists", "WARNING - " + title, 2);
      
      return null;
    }
    String tokenPattern = m.group(1) == null ? m.group(3) : m.group(0);
    try 
    {
      String reg = Utils.unEscape(tokenPattern);
      if (isRegex)
        Pattern.compile(reg);
    } 
    catch (Exception ex) 
    {
      JOptionPane.showMessageDialog(this.gui, ex.toString(), "WARNING - New " + (isRegex ? "regex" : "literal"), 2);
      
      return null;
    }
    boolean ok = true;
    for (Map.Entry<String, String> e : this.gui.theForest.tokenBank.entrySet())
     {
      if ((((String)e.getValue()).substring(1).equals(tokenPattern)) && (((String)e.getKey()).endsWith("_") == tokenName.endsWith("_")))
      {
        JOptionPane.showMessageDialog(this.gui, String.format("Pattern conflict\nToken '%s' uses the same pattern", new Object[] { e.getKey() }), "WARNING - " + title, 2);
        
        ok = false;
      }
    }
    if ((isRegex) && (!tokenPattern.equals("\\\\z")) && ("".matches(Utils.unEscape(tokenPattern)))) 
    {
      JOptionPane.showMessageDialog(this.gui, "Bad pattern\nPattern matches empty string", "WARNING - " + title, 2);
      return null;
    }
    if ((isRegex) && (tokenPattern.equals("\\\\z")) && (!tokenName.endsWith("_"))) 
    {
      JOptionPane.showMessageDialog(this.gui, "Bad name\nEOF must be local token", "WARNING - " + title, 2);
      return null;
    }
    String pattern = (isRegex ? "R" : "L") + tokenPattern;
    return ok ? new String[] { tokenName, pattern } : null;
  }
  
  private String[] findTokenInRules(String token) 
  {
    VisitorTokenSearch v = new VisitorTokenSearch(token);
    for (NodeBase n : this.gui.theForest.ruleBank.values())
    {
      n.accept(v);
    }
    return (String[])v.ruleSet.toArray(new String[v.ruleSet.size()]);
  }
  
  Action newLiteralAction = new AbstractAction("New literal", Resources.newLiteral)
  {
    public void actionPerformed(ActionEvent e) 
    {
      String[] info = ManagerTokens.this.getTokenInfo(false);
      if (info == null)
        return;
      ManagerTokens.this.gui.theForest.tokenBank.put(info[0], info[1]);
    }
  };
  
  Action newRegexAction = new AbstractAction("New regex", Resources.newRegex)
  {
    public void actionPerformed(ActionEvent e) 
    {
      String[] info = ManagerTokens.this.getTokenInfo(true);
      if (info == null)
        return;
      ManagerTokens.this.gui.theForest.tokenBank.put(info[0], info[1]);
    }
  };
  
  void editToken(String token) 
  {
    String value = (String)this.gui.theForest.tokenBank.get(token);
    boolean isRegex = value.charAt(0) == 'R';
    value = value.substring(1);
    String title = String.format(isRegex ? "Edit regex '%s'" : "Edit literal '%s'", new Object[] { token });
    String newValue = (String)JOptionPane.showInputDialog(this.gui, "Edit pattern", title, 3, null, null, value);
    
    if ((newValue == null) || (newValue.equals(value))) 
    {
      return;
    }
    if (isRegex)
    {
      try 
      {
        Pattern.compile(Utils.unEscape(newValue));
      } 
      catch (Exception ex) 
      {
        JOptionPane.showMessageDialog(this.gui, ex.getMessage(), title, 2);
        return;
      }
    }
    String pattern = (isRegex ? "R" : "L") + newValue;
    for (Map.Entry<String, String> me : this.gui.theForest.tokenBank.entrySet()) 
    {
      if (((String)me.getValue()).equals(pattern)) 
      {
        JOptionPane.showMessageDialog(this.gui, String.format("Token %s has the same pattern", new Object[] { me.getKey() }), title, 2);
        return;
      }
    }
    this.gui.theForest.tokenBank.put(token, pattern);
  }
  
  Action editTokenAction = new AbstractAction("Edit token", Resources.edit16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      String[] names = (String[])ManagerTokens.this.gui.theForest.tokenBank.keySet().toArray(new String[ManagerTokens.this.gui.theForest.tokenBank.size()]);
      List<String> editableNames = new ArrayList();
      for (String key : names) 
      {
        String val = (String)ManagerTokens.this.gui.theForest.tokenBank.get(key);
        
        if ((val.length() != key.length() - 1) || (!val.substring(1).equals(key.substring(1, key.length() - 1))))
        {

          editableNames.add(key); 
        }
      }
      if (editableNames.size() == 0) 
      {
        JOptionPane.showMessageDialog(ManagerTokens.this.gui, "No editable tokens defined yet", "WARNING - Edit token", 2);
        return;
      }
      names = (String[])editableNames.toArray(new String[editableNames.size()]);
      String token = (String)JOptionPane.showInputDialog(ManagerTokens.this.gui, "Select token to edit", "Edit token", 3, null, names, names[0]);
      
      if (token == null)
        return;
      ManagerTokens.this.editToken(token);
    }
  };
  
  Action findTokenAction = new AbstractAction("Find token", Resources.replace16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      Object[] names = ManagerTokens.this.gui.theForest.tokenBank.keySet().toArray();
      if (names.length == 0) 
      {
        JOptionPane.showMessageDialog(ManagerTokens.this.gui, "No tokens defined yet", "WARNING - Find token", 2);
        return;
      }
      String tokenName = (String)JOptionPane.showInputDialog(ManagerTokens.this.gui, "Select token to find", "Find token", 3, null, names, names[0]);
      
      if (tokenName == null)
        return;
      String[] rules = ManagerTokens.this.findTokenInRules(tokenName);
      if (rules.length == 0) 
      {
        JOptionPane.showMessageDialog(ManagerTokens.this.gui, String.format("Token '%s' is not referred in any rule", new Object[] { tokenName }), "WARNING - Find token", 2);
        return;
      }
      String rule = (String)JOptionPane.showInputDialog(ManagerTokens.this.gui, String.format("Token '%s' is referred in rules listed below\nClick OK to display selected rule", new Object[] { tokenName }), "Find token", 3, null, rules, rules[0]);
      

      if (rule != null) 
      {
        ManagerTokens.this.gui.theRuleManager.theComboBox.setSelectedItem(rule);
      }
    }
  };
  Action deleteTokenAction = new AbstractAction("Delete token", Resources.delete16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      Object[] names = ManagerTokens.this.gui.theForest.tokenBank.keySet().toArray();
      if (names.length == 0) 
      {
        JOptionPane.showMessageDialog(ManagerTokens.this.gui, "No tokens defined yet", "WARNING - Delete token", 2);
        return;
      }
      String tokenToDelete = (String)JOptionPane.showInputDialog(ManagerTokens.this.gui, "Select token to delete", "Delete token", 3, null, names, names[0]);
      
      if (tokenToDelete == null)
        return;
      String[] rules = ManagerTokens.this.findTokenInRules(tokenToDelete);
      if (rules.length != 0)
      {
        String rule = (String)JOptionPane.showInputDialog(ManagerTokens.this.gui, String.format("Can't delete token '%s'\nUsed in rules listed below\nClick OK to display a rule", new Object[] { tokenToDelete }), "WARNING - Delete token", 2, null, rules, rules[0]);
        if (rule != null)
          ManagerTokens.this.gui.theRuleManager.theComboBox.setSelectedItem(rule);
        return;
      }
      int opt = JOptionPane.showConfirmDialog(ManagerTokens.this.gui, String.format("Delete '%s' ?", new Object[] { tokenToDelete }), "Delete token", 2);
      if (opt == 0) 
      {
        ManagerTokens.this.gui.theForest.tokenBank.remove(tokenToDelete);
      }
    }
  };
  Vll4jGui gui;
}