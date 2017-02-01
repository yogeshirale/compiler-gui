package net.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Document;

public class PanelTesting
  extends JPanel
{
  Vll4jGui theGui;
  
  PanelTesting(Vll4jGui theGui)
  {
    this.theGui = theGui;
    setLayout(new BorderLayout());
    JPanel eastPanel = new JPanel();
    eastPanel.setLayout(new BorderLayout());
    eastPanel.add(new JLabel("Parser Log", 0), "North");
    this.logArea.setOpaque(true);
    this.logArea.setFont(new Font("Monospaced", this.logArea.getFont().getStyle(), this.logArea.getFont().getSize()));
    
    this.logArea.setEditable(false);
    JPanel logBtnPanel = new JPanel();
    logBtnPanel.setLayout(new BorderLayout());
    logBtnPanel.add(this.logStatus, "Center");
    JButton helpButton2 = new JButton(theGui.theHelpFunctionsManager.displayHelpTestLog)
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
    logBtnPanel.add(helpButton2, "East");
    eastPanel.add(new JScrollPane(this.logArea), "Center");
    eastPanel.add(logBtnPanel, "South");
    JPanel westPanel = new JPanel();
    westPanel.setLayout(new BorderLayout());
    westPanel.add(new JLabel("Parser Test Input", 0), "North");
    this.inputArea.setLineWrap(true);
    this.inputArea.addCaretListener(new CaretListener() 
    {
      public void caretUpdate(CaretEvent e) 
      {
        int dot = e.getDot();
        int line = 1;int col = 1;
        for (char ch : PanelTesting.this.inputArea.getText().substring(0, dot).toCharArray()) {
          if (ch == '\n') 
          {
            line++;col = 1;
          } 
          else 
          {
            col++;
          }
        }
        PanelTesting.this.inputStatus.setText(String.format(" Line %d, Column %d", new Object[] { Integer.valueOf(line), Integer.valueOf(col) }));
      }
    });
    JPanel inputBtnPanel = new JPanel();
    inputBtnPanel.setLayout(new BorderLayout());
    inputBtnPanel.add(this.inputStatus, "Center");
    JButton helpButton1 = new JButton(theGui.theHelpFunctionsManager.displayHelpTestInput)
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
    inputBtnPanel.add(helpButton1, "East");
    westPanel.add(new JScrollPane(this.inputArea), "Center");
    westPanel.add(inputBtnPanel, "South");
    JSplitPane sp = new JSplitPane(1, westPanel, eastPanel);
    
    sp.setDividerLocation(theGui.frameWidth / 5);
    add(sp, "Center");
  }
  
  void setMultiFileLog(boolean mf) 
  {
    if (mf) 
    {
      this.logArea.setLineWrap(true);
    } 
    else 
    {
      this.logArea.setLineWrap(false);
    }
  }
  
  void logClear() 
  {
    this.logArea.errLines.clear();
    this.logArea.setText("");
  }
  
  void logCopy() 
  {
    int selectionStart = this.logArea.getSelectionStart();
    int selectionEnd = this.logArea.getSelectionEnd();
    this.logArea.setSelectionStart(0);
    this.logArea.setSelectionEnd(this.logArea.getDocument().getLength());
    this.logArea.copy();
    this.logArea.setSelectionStart(selectionStart);
    this.logArea.setSelectionEnd(selectionEnd);
  }
  
  PrintStream getOutStream() 
  {
    OutputStream os = new OutputStream() 
    {
      StringBuilder sb = new StringBuilder();
      
      public void write(int b) 
      { 
      	System.out.flush();
        this.sb.append((char)b);
        if (b == 10) 
        {
          final String line = this.sb.toString();
          this.sb.setLength(0);
          SwingUtilities.invokeLater(new Runnable()
          {
            public void run() 
            {
              PanelTesting.this.logArea.append(line);
              int len = PanelTesting.this.logArea.getText().length();
              PanelTesting.this.logArea.select(len, len);
            }
          });
        }
      }
    };
    return new PrintStream(os, true);
  }
  
  PrintStream getErrStream() 
  {
    OutputStream os = new OutputStream() 
    {
      StringBuilder sb = new StringBuilder();
      
      public void write(int b) 
      { 
      	System.out.flush();
        this.sb.append((char)b);
        if (b == 10) 
        {
          final String line = this.sb.toString().replace("\t", "        ");
          this.sb.setLength(0);
          SwingUtilities.invokeLater(new Runnable()
          {
            public void run() 
            {
              PanelTesting.this.logArea.errLines.add(new Integer[] { Integer.valueOf(PanelTesting.this.logArea.getText().length()), Integer.valueOf(line.length()) });
              PanelTesting.this.logArea.append(line);
              int len = PanelTesting.this.logArea.getText().length();
              PanelTesting.this.logArea.select(len, len);
            }
          });
        }
      }
    };
    return new PrintStream(os, true);
  }
  
  JTextArea inputArea = new TextAreaCustom();
  LogTextArea logArea = new LogTextArea();
  private JLabel inputStatus = new JLabel();
  JLabel logStatus = new JLabel();
}
