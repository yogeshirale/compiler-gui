package net.java.vll.vll4j.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.java.vll.vll4j.api.ActionFunction;
import net.java.vll.vll4j.api.Forest;
import net.java.vll.vll4j.api.NodeBase;
import net.java.vll.vll4j.api.NodeRoot;

public class PanelActionCode extends JPanel
{
  PanelActionCode(Vll4jGui gui)
  {
    this.gui = gui;
    setLayout(new BorderLayout());
    add(new JLabel("Action Code", 0), "North");
    this.codeArea.addKeyListener(this.keyAdapter);
    add(new JScrollPane(this.codeArea), "Center");
    this.saveButton.setEnabled(false);
    JPanel btnPanel = new JPanel();
    btnPanel.setLayout(new BorderLayout());
    btnPanel.add(this.saveButton, "Center");
    this.helpButton = new JButton(gui.theHelpFunctionsManager.displayHelpActionCode)
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
    this.normalTextColor = this.codeArea.getForeground();
  }
  
  void resetView() 
  {
    if ((this.gui.theTreePanel.selectedNode instanceof NodeRoot)) 
    {
      this.codeArea.setEnabled(false);
      this.codeArea.setText("");
      this.codeArea.setBackground(Color.gray.brighter());
      for (MouseListener ml : this.codeArea.getMouseListeners()) 
      {
        if ((ml instanceof PopupListenerText)) 
        {
          this.mouseListener = ml;
          this.codeArea.removeMouseListener(ml);
        }
      }
    } 
    else 
    {
      this.codeArea.setEnabled(true);
      this.codeArea.setBackground(this.normalBackgroundColor);
      String at = this.gui.theTreePanel.selectedNode.actionText;
      ActionFunction af = this.gui.theTreePanel.selectedNode.actionFunction;
      this.codeArea.setText(at);
      this.codeArea.setForeground(at.trim().isEmpty() == (af == null) ? this.normalTextColor : Color.red);
      this.codeArea.addMouseListener(this.mouseListener);
    }
    this.saveButton.setEnabled(false);
  }
  
  private Action saveAction = new AbstractAction("Save")
  {
    public void actionPerformed(ActionEvent o) 
    {
      PanelActionCode.this.gui.theTreePanel.selectedNode.actionText = PanelActionCode.this.codeArea.getText();
      PanelActionCode.this.saveButton.setEnabled(false);
      String msg = PanelActionCode.this.gui.theForest.compileActionCode(PanelActionCode.this.gui.theTreePanel.selectedNode);
      if (msg == null) 
      {
        PanelActionCode.this.codeArea.setForeground(PanelActionCode.this.normalTextColor);
      } 
      else 
      {
        JOptionPane.showMessageDialog(PanelActionCode.this.gui, msg, "ERROR - Save action", 0);
        PanelActionCode.this.codeArea.setForeground(Color.red);
      }
      PanelActionCode.this.gui.theTreePanel.resetNodeDisplay(PanelActionCode.this.gui.theTreePanel.selectedNode);
    }
  };
  
  KeyAdapter keyAdapter = new KeyAdapter()
  {
    public void keyTyped(KeyEvent e) 
    {
      PanelActionCode.this.saveButton.setEnabled(true);
    }
  };
  
  TextAreaCustom codeArea = new TextAreaCustom();
  private MouseListener mouseListener = null;
  private Color normalTextColor; private Color normalBackgroundColor = this.codeArea.getBackground();
  private JButton saveButton = new JButton(this.saveAction);
  private JButton helpButton = null;
  Vll4jGui gui;
}
