package net.gui;

import java.awt.Font;
import javax.swing.JTextArea;

public class TextAreaCustom
  extends JTextArea
{
  TextAreaCustom()
  {
    setFont(new Font("Monospaced", getFont().getStyle(), getFont().getSize()));
    addMouseListener(new PopupListenerText(this));
  }
  
  PopupMenuText popupMenuText = new PopupMenuText(this);
}
