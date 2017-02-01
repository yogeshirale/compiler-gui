package net.gui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import net.api.Forest;
import net.api.NodeBase;
import net.combinator.PackratParsers;
import net.combinator.Utils;

public class ManagerFileOps
{
  ManagerFileOps(Vll4jGui gui)
  {
    this.gui = gui;
  }
  
  Action fileNewAction = new AbstractAction("New", Resources.new16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      ManagerFileOps.this.grammarFile = null;
      ManagerFileOps.this.gui.reset(true);
      ManagerFileOps.this.gui.theRuleManager.reset();
    }
  };
  
  void openInputStream(InputStream is, boolean tokensOnly) 
  {
    try 
    {
      this.gui.theForest.openInputStream(is, tokensOnly);
      if (!tokensOnly) 
      {
        this.gui.packratParsers.whiteSpaceRegex = this.gui.theForest.whiteSpace;
        this.gui.packratParsers.commentSpecRegex = this.gui.theForest.comment;
        this.gui.packratParsers.resetWhitespace();
        this.gui.theRuleManager.theComboBox.setAction(null);
        this.gui.theRuleManager.theComboBox.removeAllItems();
        for (String ruleName : this.gui.theForest.ruleBank.keySet()) 
        {
          this.gui.theRuleManager.theComboBox.addItem(ruleName);
        }
        this.gui.theRuleManager.theComboBox.setMaximumSize(this.gui.theRuleManager.theComboBox.getPreferredSize());
        this.gui.theRuleManager.theComboBox.setAction(this.gui.theRuleManager.comboBoxAction);
        this.gui.theTreePanel.setRuleName((String)this.gui.theRuleManager.theComboBox.getItemAt(0));
      }
    } 
    catch (Exception ex) 
    {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this.gui, String.format("Error processing file: %s", new Object[] { ex }), "ERROR - File open", 0);
    }
  }
  

  Action fileOpenAction = new AbstractAction("Open", Resources.open16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      JFileChooser fc = ManagerFileOps.this.getFileChooser("Open");
      if (fc.showOpenDialog(ManagerFileOps.this.gui) != 0) 
      {
        return;
      }
      ManagerFileOps.this.grammarFile = fc.getSelectedFile();
      try 
      {
        InputStream is = new FileInputStream(ManagerFileOps.this.grammarFile);
        ManagerFileOps.this.gui.reset(false);
        ManagerFileOps.this.openInputStream(is, false);
        ManagerFileOps.this.gui.setGrammarName(ManagerFileOps.this.grammarFile.getName());
        ManagerFileOps.this.gui.theRuleManager.reset();
        is.close();
      } 
      catch (Exception ex) 
      {
        JOptionPane.showMessageDialog(ManagerFileOps.this.gui, String.format("%s", new Object[] { ex }), "ERROR - File open", 0);
        
        ex.printStackTrace();
      }
    }
  };
  
  Action importTokenAction = new AbstractAction("Import tokens", Resources.import16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      JFileChooser fc = ManagerFileOps.this.getFileChooser("Import tokens");
      if (fc.showOpenDialog(ManagerFileOps.this.gui) != 0) 
      {
        return;
      }
      ManagerFileOps.this.grammarFile = fc.getSelectedFile();
      try 
      {
        InputStream is = new FileInputStream(ManagerFileOps.this.grammarFile);
        ManagerFileOps.this.openInputStream(is, true);
        is.close();
      } 
      catch (Exception ex) 
      {
        JOptionPane.showMessageDialog(ManagerFileOps.this.gui, String.format("%s", new Object[] { ex }), "ERROR - Import tokens", 0);
        
        ex.printStackTrace();
      }
    }
  };
  
  private void writeFile(boolean tokensOnly) 
  {
    PrintWriter pw = null;
    try 
    {
      pw = new PrintWriter(this.grammarFile);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this.gui, ex, "ERROR - Save file", 0);
      return;
    }
   
    for (Map.Entry<String, String> me : this.gui.theForest.tokenBank.entrySet()) 
    {
      String value = (String)me.getValue();
      if (value.startsWith("L")) 
      {
        pw.printf("    <Literal Name=\"%s\" Pattern=\"%s\" />%n", new Object[] { Utils.encode4xml((String)me.getKey()), Utils.encode4xml(value.substring(1)) });
      }
      else 
      {
        pw.printf("    <Regex Name=\"%s\" Pattern=\"%s\" />%n", new Object[] { Utils.encode4xml((String)me.getKey()), Utils.encode4xml(value).substring(1) });
      }
    }
    
    pw.println("  </Tokens>");
    if (!tokensOnly) 
    {
      pw.printf("  <Whitespace>%s</Whitespace>%n", new Object[] { Utils.encode4xml(this.gui.packratParsers.whiteSpaceRegex) });
      
      pw.printf("  <Comments>%s</Comments>%n", new Object[] { Utils.encode4xml(this.gui.packratParsers.commentSpecRegex) });
      
      pw.println("  <Parsers>");
      VisitorXmlGeneration xmlWriter = new VisitorXmlGeneration(pw);
      for (Map.Entry<String, NodeBase> me : this.gui.theForest.ruleBank.entrySet()) 
      {
        ((NodeBase)me.getValue()).accept(xmlWriter);
      }
      pw.println("  </Parsers>");
    }
    pw.println("</VLL-Grammar>");
    pw.close();
    this.gui.setGrammarName(this.grammarFile.getName());
  }
  
  Action fileSaveAction = new AbstractAction("Save", Resources.save16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      if (ManagerFileOps.this.grammarFile == null) 
      {
        JFileChooser fc = ManagerFileOps.this.getFileChooser("Save");
        if (fc.showOpenDialog(ManagerFileOps.this.gui) != 0) 
        {
          return;
        }
        ManagerFileOps.this.grammarFile = fc.getSelectedFile();
        if ((ManagerFileOps.this.grammarFile.exists()) && 
          (0 != JOptionPane.showConfirmDialog(ManagerFileOps.this.gui, "Overwrite existing file?", "Save As", 0, 3))) 
        {
          return;
        }
      }
      
      ManagerFileOps.this.writeFile(false);
    }
  };
  
  Action fileSaveAsAction = new AbstractAction("Save As", Resources.saveAs16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      JFileChooser fc = ManagerFileOps.this.getFileChooser("Save As");
      if (fc.showOpenDialog(ManagerFileOps.this.gui) != 0) 
      {
        return;
      }
      ManagerFileOps.this.grammarFile = fc.getSelectedFile();
      if ((ManagerFileOps.this.grammarFile.exists()) && 
        (0 != JOptionPane.showConfirmDialog(ManagerFileOps.this.gui, "Overwrite existing file?", "Save As", 0, 3))) 
      {
        return;
      }
      
      ManagerFileOps.this.writeFile(false);
    }
  };
  
  Action exportTokenAction = new AbstractAction("Export tokens", Resources.export16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      JFileChooser fc = ManagerFileOps.this.getFileChooser("Export tokens");
      if (fc.showOpenDialog(ManagerFileOps.this.gui) != 0) 
      {
        return;
      }
      ManagerFileOps.this.grammarFile = fc.getSelectedFile();
      if ((ManagerFileOps.this.grammarFile.exists()) && 
        (0 != JOptionPane.showConfirmDialog(ManagerFileOps.this.gui, "Overwrite existing file?", "Save As", 0, 3))) 
      {
        return;
      }
      
      ManagerFileOps.this.writeFile(true);
    }
  };
  
  Action fileExitAction = new AbstractAction("Exit")
  {

    public void actionPerformed(ActionEvent e) { System.exit(0); }
  };
  Vll4jGui gui;
  private JFileChooser fileChooser;
  
  private JFileChooser getFileChooser(String title) 
  { 
  	if (this.fileChooser == null) 
  	{
      this.fileChooser = new JFileChooser();
      this.fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
      this.fileChooser.setFileSelectionMode(0);
      this.fileChooser.setFileFilter(new FileFilter() 
      {
        public String getDescription() 
        {
          return "VisualLangLab grammar";
        }
        
        public boolean accept(File f) { return (f.isDirectory()) || (f.getName().endsWith(".vll")) || (f.getName().endsWith(".VLL")); }
      });
    }
    
    this.fileChooser.setDialogTitle(title);
    this.fileChooser.setApproveButtonText((title.charAt(0) == 'S') || (title.charAt(0) == 'E') ? "Save" : "Open");
    
    return this.fileChooser;
  }
  private File grammarFile = null;
}