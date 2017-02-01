package net.java.vll.vll4j.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import net.java.vll.vll4j.api.NodeBase;

public class PanelAstDisplay
  extends JPanel
{
  PanelAstDisplay(Vll4jGui gui)
  {
    this.gui = gui;
    this.visitorAstGeneration = new VisitorAstDescription(gui);
    setLayout(new BorderLayout());
    add(new JLabel("Parse Tree (AST) Structure", 0), "North");
    this.astArea.setEditable(false);
    this.astArea.setFont(new Font("Monospaced", this.astArea.getFont().getStyle(), this.astArea.getFont().getSize()));
    add(new JScrollPane(this.astArea), "Center");
    JPanel btnPanel = new JPanel();
    btnPanel.setLayout(new GridLayout(1, 6));
    ButtonGroup bg = new ButtonGroup();
    btnPanel.add(this.btnOne);
    bg.add(this.btnOne);
    btnPanel.add(this.btnThree);
    bg.add(this.btnThree);
    btnPanel.add(this.btnRule);
    bg.add(this.btnRule);
    this.btnRule.setToolTipText("Rule");
    this.btnRule.setSelected(true);
    btnPanel.add(this.btnFull);
    bg.add(this.btnFull);
    this.btnFull.setToolTipText("Infinite");
    JPanel southPanel = new JPanel();
    southPanel.setLayout(new BorderLayout());
    this.helpButton = new JButton(gui.theHelpFunctionsManager.displayHelpAST)
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
    southPanel.add(this.helpButton, "East");
    southPanel.add(btnPanel, "Center");
    add(southPanel, "South");
    setButtonsActive(true);
  }
  
  private void drawAst() 
  {
    if (this.myThread != null)
      return;
    this.visitorAstGeneration.level = 0;
    this.myThread = new Thread() 
    {
      public void run() 
      {
        NodeBase node = PanelAstDisplay.this.gui.theTreePanel.selectedNode;
        if (PanelAstDisplay.this.btnOne.isSelected()) 
        {
          PanelAstDisplay.this.visitorAstGeneration.maxDepth = 1;
        } 
        else if (PanelAstDisplay.this.btnThree.isSelected()) 
        {
          PanelAstDisplay.this.visitorAstGeneration.maxDepth = 3;
        } 
        else if (PanelAstDisplay.this.btnRule.isSelected()) 
        {
          PanelAstDisplay.this.visitorAstGeneration.maxDepth = 2147483646;
        } 
        else 
        {
          PanelAstDisplay.this.visitorAstGeneration.maxDepth = Integer.MAX_VALUE;
        }
        try 
        {
          String ast = (String)node.accept(PanelAstDisplay.this.visitorAstGeneration);
          PanelAstDisplay.this.astArea.setText(ast);
        } 
        catch (Throwable ex) 
        {
          JOptionPane.showMessageDialog(PanelAstDisplay.this.gui, ex, "ERROR - AST generation", 0);
          ex.printStackTrace();
        }
        PanelAstDisplay.this.myThread = null;
      }
    };
    this.myThread.start();
  }
  
  void resetView() 
  {
    setButtonsActive(false);
    this.btnRule.setSelected(true);
    setButtonsActive(true);
    drawAst();
  }
  
  private void setButtonsActive(boolean active) 
  {
    if (active) 
    {
      this.btnOne.addActionListener(this.buttonListener);
      this.btnThree.addActionListener(this.buttonListener);
      this.btnRule.addActionListener(this.buttonListener);
      this.btnFull.addActionListener(this.buttonListener);
    } 
    else 
    {
      this.btnOne.removeActionListener(this.buttonListener);
      this.btnThree.removeActionListener(this.buttonListener);
      this.btnRule.removeActionListener(this.buttonListener);
      this.btnFull.removeActionListener(this.buttonListener);
    }
  }
  
  private ActionListener buttonListener = new ActionListener() 
  {
    public void actionPerformed(ActionEvent evt) 
    {
      Object src = evt.getSource();
      if ((src == PanelAstDisplay.this.btnFull) && 
        (0 != JOptionPane.showConfirmDialog(PanelAstDisplay.this.gui, "Need full-depth AST?\n(may cause memory errors)", "CONFIRM - AST depth", 2)))
      {

        PanelAstDisplay.this.setButtonsActive(false);
        PanelAstDisplay.this.btnRule.setSelected(true);
        PanelAstDisplay.this.setButtonsActive(true);
        return;
      }
      
      PanelAstDisplay.this.drawAst();
    }
  };
  
  private JTextArea astArea = new JTextArea();
  private JRadioButton btnOne = new JRadioButton("1");
  private JRadioButton btnThree = new JRadioButton("3");
  private JRadioButton btnRule = new JRadioButton("§");
  private JRadioButton btnFull = new JRadioButton("∞");
  private JButton helpButton = null;
  private VisitorAstDescription visitorAstGeneration;
  private Vll4jGui gui;
  private Thread myThread = null;
}
