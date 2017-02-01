package net.java.vll.vll4j.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PopupListenerText
  extends MouseAdapter
{
  TextAreaCustom textAreaCustom;
  
  PopupListenerText(TextAreaCustom textAreaCustom)
  {
    this.textAreaCustom = textAreaCustom;
  }
  
  public void mousePressed(MouseEvent me)
  {
    popup(me);
  }
  
  public void mouseReleased(MouseEvent me)
  {
    popup(me);
  }
  
  void popup(MouseEvent e) 
  {
    if (e.isPopupTrigger()) 
    {
      int x = e.getX();int y = e.getY();
      if (y + this.textAreaCustom.popupMenuText.getHeight() > this.textAreaCustom.getHeight()) 
      {
        y = this.textAreaCustom.getHeight() - this.textAreaCustom.popupMenuText.getHeight();
      }
      if (x + this.textAreaCustom.popupMenuText.getWidth() > this.textAreaCustom.getWidth()) 
      {
        x = this.textAreaCustom.getWidth() - this.textAreaCustom.popupMenuText.getWidth();
      }
      this.textAreaCustom.popupMenuText.show(e.getComponent(), x, y);
    }
  }
}
