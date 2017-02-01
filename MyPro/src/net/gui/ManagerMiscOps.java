package net.gui;

import java.awt.event.ActionEvent;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import net.combinator.PackratParsers;
import net.combinator.Utils;

public class ManagerMiscOps
{
  ManagerMiscOps(Vll4jGui gui)
  {
    this.gui = gui;
  }
  
  Action globalsWhitespaceAction = new AbstractAction("Whitespace") 
  {
    public void actionPerformed(ActionEvent e) 
    {
      String ws = (String)JOptionPane.showInputDialog(ManagerMiscOps.this.gui, "Enter whitespace pattern", "WhiteSpace", 3, null, null, ManagerMiscOps.this.gui.packratParsers.whiteSpaceRegex);
      
      if ((ws == null) || (ws.equals(ManagerMiscOps.this.gui.packratParsers.whiteSpaceRegex)))
        return;
      try 
      {
        Pattern.compile(Utils.unEscape(ws));
      } 
      catch (Exception ex) 
      {
        JOptionPane.showMessageDialog(ManagerMiscOps.this.gui, ex.toString(), "ERROR - WhiteSpace", 0);
        return;
      }
      ManagerMiscOps.this.gui.packratParsers.whiteSpaceRegex = ws;
      ManagerMiscOps.this.gui.packratParsers.resetWhitespace();
    }
  };
  
  Action globalsCommentAction = new AbstractAction("Comment") 
  {
    public void actionPerformed(ActionEvent e) 
    {
      String cmts = (String)JOptionPane.showInputDialog(ManagerMiscOps.this.gui, "Enter comment pattern", "Comments", 3, null, null, ManagerMiscOps.this.gui.packratParsers.commentSpecRegex);
      
      if ((cmts == null) || (cmts.equals(ManagerMiscOps.this.gui.packratParsers.commentSpecRegex)))
        return;
      try 
      {
        Pattern.compile(Utils.unEscape(cmts));
      } 
      catch (Exception ex) 
      {
        JOptionPane.showMessageDialog(ManagerMiscOps.this.gui, ex.toString(), "ERROR - WhiteSpace", 0);
        return;
      }
      ManagerMiscOps.this.gui.packratParsers.commentSpecRegex = cmts;
      ManagerMiscOps.this.gui.packratParsers.resetWhitespace();
    }
  };
  private Vll4jGui gui;
}