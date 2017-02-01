package net.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import net.api.Forest;
import net.api.NodeRoot;

public class RendererRuleComboBox
  extends BasicComboBoxRenderer
{
  private Vll4jGui gui;
  private NodeRoot rootNode;
  
  public RendererRuleComboBox(Vll4jGui gui)
  {
    this.gui = gui;
    setOpaque(true);
  }
  
  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
  {
    this.ruleName = value.toString();
    this.rootNode = ((NodeRoot)this.gui.theForest.ruleBank.get(this.ruleName));
    this.visitorRuleInfo.visitRoot(this.rootNode);
    setText(this.ruleName);
    setIcon(this.myIcon);
    return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
  }
  
  public void paint(Graphics g) 
  {
    super.paint(g);
    this.visitorRuleInfo.visitRoot(this.rootNode);
    if (this.visitorRuleInfo.hasErrors) 
    {
      g.setColor(Color.red);
      g.drawRect(2, 2, 12, 12);
      g.drawRect(1, 1, 14, 14);
    }
    if (this.visitorRuleInfo.hasActions) 
    {
      g.setColor(this.visitorRuleInfo.isTester ? Color.magenta : Color.green.darker());
      g.drawRect(4, 4, 8, 8);
      g.fillRect(4, 4, 8, 8);
      g.setColor(Color.white);
      g.drawLine(6, 5, 9, 8);
      g.drawLine(9, 8, 6, 11);
      g.drawLine(7, 5, 10, 8);
      g.drawLine(10, 8, 7, 11);
    }
  }
  private Icon myIcon = Resources.rule;
  String ruleName = "";
  private VisitorRuleInfo visitorRuleInfo = new VisitorRuleInfo();
}
