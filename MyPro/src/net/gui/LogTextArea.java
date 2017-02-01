package net.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JTextArea;

public class LogTextArea
  extends JTextArea
{
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    if (this.fontMetrics == null) 
    {
      this.fontMetrics = g.getFontMetrics();
      this.fontBase = (this.fontMetrics.getLeading() + this.fontMetrics.getAscent());
      this.fontHeight = this.fontMetrics.getHeight();
      this.backGroundColor = getBackground();
    }
    int y1 = getVisibleRect().y;
    int y2 = y1 + getVisibleRect().height;
    int width = getVisibleRect().width;
    int rowHeight = getRowHeight();
    for (Integer[] el : this.errLines)
      try 
      {
        int textOffset = el[0].intValue();
        int textLength = el[1].intValue();
        int y = modelToView(textOffset).y;
        if ((y + rowHeight >= y1) && (y <= y2)) 
        {
          String s = getText(textOffset, textLength);
          
          if ((getLineWrap()) && (s != null) && (!s.isEmpty())) 
          {
            int len = 0;
            for (int j = 0;; j++) 
			{ 
				if (j >= s.length()) 
					break;///////////////////////////////////////////////// label315
              	len += this.fontMetrics.charWidth(s.charAt(j));
              	if (len > width) 
              	{
	                String ss = s.substring(0, j);
	                g.setColor(this.backGroundColor);
	                g.fillRect(0, y, width, this.fontHeight);
	                g.setColor(Color.red);
	                g.drawString(ss, 0, y + this.fontBase);
	                y += rowHeight;
	                s = s.substring(j);
	                break;
	            }
            }
          }
          
          if (!s.isEmpty()) 
          {
            g.setColor(this.backGroundColor);
            g.fillRect(0, y, width, this.fontHeight);
            g.setColor(Color.red);
            g.drawString(s, 0, y + this.fontBase);
          }
        }
      }
		catch (Exception e) 
		{}
        label315 :
		{}
  }
  
  volatile ArrayList<Integer[]> errLines = new ArrayList();
  private Color backGroundColor;
  private int fontHeight;
  private int fontBase; private FontMetrics fontMetrics = null;
}