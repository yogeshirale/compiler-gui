package net.gui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class PopupMenuText
  extends JPopupMenu
{
  PopupMenuText(TextAreaCustom textComponent)
  {
    this.textComponent = textComponent;
    createPopup();
  }
  
  private void createPopup() 
  {
    this.cutItem = new JMenuItem(this.cutAction);
    add(this.cutItem);
    this.copyItem = new JMenuItem(this.copyAction);
    add(this.copyItem);
    this.pasteItem = new JMenuItem(this.pasteAction);
    add(this.pasteItem);
    this.clearItem = new JMenuItem(this.clearAction);
    add(this.clearItem);
    this.selectAllItem = new JMenuItem(this.selectAllAction);
    add(this.selectAllItem);
  }
 
  private Action cutAction = new AbstractAction("Cut", Resources.cut16) 
  {
    public void actionPerformed(ActionEvent evt) 
    {
      PopupMenuText.this.textComponent.cut();
      PopupMenuText.this.textComponent.dispatchEvent(new KeyEvent(PopupMenuText.this.textComponent, 400, 0L, 0, 0, '\033'));
    }
  };
  

  private Action copyAction = new AbstractAction("Copy", Resources.copy16) 
  {
    public void actionPerformed(ActionEvent evt) 
    {
      PopupMenuText.this.textComponent.copy();
    }
  };
  
  private Action pasteAction = new AbstractAction("Paste", Resources.paste16) 
  {
    public void actionPerformed(ActionEvent evt) 
    {
      PopupMenuText.this.textComponent.paste();
      PopupMenuText.this.textComponent.dispatchEvent(new KeyEvent(PopupMenuText.this.textComponent, 400, 0L, 0, 0, '\033'));
    }
  };
 
  private Action clearAction = new AbstractAction("Clear", Resources.clear16) 
  {
    public void actionPerformed(ActionEvent evt) 
    {
      PopupMenuText.this.textComponent.setText("");
      PopupMenuText.this.textComponent.dispatchEvent(new KeyEvent(PopupMenuText.this.textComponent, 400, 0L, 0, 0, '\033'));
    }
  };
  
  private Action selectAllAction = new AbstractAction("Select All") 
  {
    public void actionPerformed(ActionEvent evt) 
    {
      PopupMenuText.this.textComponent.setSelectionStart(0);
      PopupMenuText.this.textComponent.setSelectionEnd(PopupMenuText.this.textComponent.getText().length());
      PopupMenuText.this.textComponent.selectAll();
    }
  };
  TextAreaCustom textComponent;
  JMenuItem cutItem;
  JMenuItem copyItem;
  JMenuItem pasteItem;
  JMenuItem clearItem;
  JMenuItem selectAllItem;
  
  void adjustMenu() {}
}