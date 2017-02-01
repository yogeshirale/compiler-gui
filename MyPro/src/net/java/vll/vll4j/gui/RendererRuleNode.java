package net.java.vll.vll4j.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
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
import net.java.vll.vll4j.api.VisitorValidation;

public class RendererRuleNode
  extends DefaultTreeCellRenderer
{
  NodeBase theNode;
  
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
  {
    this.theNode = ((NodeBase)value);
    this.isDropped = this.theNode.isDropped;
    JLabel c = (JLabel)super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
    c.setText(this.theNode.getName());
    this.validationString = ((String)this.theNode.accept(this.visitorNodeValidation));
    c.setToolTipText(this.validationString);
    return c;
  }
  
  public Icon getLeafIcon() { return getNodeIcon(); }
  
  public Icon getOpenIcon() { return getNodeIcon(); }
  
  public Icon getClosedIcon() { return getNodeIcon(); }
  
  private Icon getNodeIcon() {
    if ((this.theNode instanceof NodeChoice))
      return Resources.choice;
    if ((this.theNode instanceof NodeLiteral)) 
    {
      String litName = ((NodeLiteral)this.theNode).literalName;
      return litName.endsWith("_") ? Resources.literalLocal : Resources.literal; 
  	}
    if ((this.theNode instanceof NodeReference))
      return Resources.reference;
    if ((this.theNode instanceof NodeRegex)) 
    {
      String regName = ((NodeRegex)this.theNode).regexName;
      return regName.endsWith("_") ? Resources.regexLocal : Resources.regex; 
    }
    if ((this.theNode instanceof NodeRepSep))
      return Resources.repSep;
    if ((this.theNode instanceof NodeRoot))
      return Resources.root;
    if ((this.theNode instanceof NodeSemPred))
      return Resources.semPred;
    if ((this.theNode instanceof NodeSequence))
      return Resources.sequence;
    if ((this.theNode instanceof NodeWildCard)) 
    {
      return Resources.wildCard;
    }
    return null;
  }
  
  public void paintComponent(Graphics g) 
  {
    super.paintComponent(g);
    this.validationString = ((String)this.theNode.accept(this.visitorNodeValidation));
    setToolTipText(this.validationString);
    if (this.validationString != null) 
    {
      g.drawImage(Resources.errorMark.getImage(), 0, 0, null);
    }
    if (this.isDropped) 
    {
      g.setColor(Color.black);
      g.drawLine(0, getHeight(), getHeight(), 0);
      g.setColor(Color.white);
      g.drawLine(0, getHeight() + 1, getHeight(), 1);
      g.drawLine(0, getHeight() - 1, getHeight(), -1);
    }
  }

  private VisitorValidation visitorNodeValidation = new VisitorValidation();
  private String validationString;
  private boolean isDropped;
}
