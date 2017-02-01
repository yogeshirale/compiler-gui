package net.java.vll.vll4j.gui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.script.ScriptException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import net.java.vll.vll4j.api.Forest;
import net.java.vll.vll4j.api.NodeBase;
import net.java.vll.vll4j.api.VisitorParserGeneration;
import net.java.vll.vll4j.combinator.PackratParsers;
import net.java.vll.vll4j.combinator.Parsers;
import net.java.vll.vll4j.combinator.Parsers.ParseResult;
import net.java.vll.vll4j.combinator.Parsers.Parser;
import net.java.vll.vll4j.combinator.Reader;
import net.java.vll.vll4j.combinator.Utils;

public class ManagerTesting
{
  ManagerTesting(Vll4jGui gui)
  {
    this.gui = gui;
  }
  
  private void enableTestControls(boolean enable) 
  {
    this.parseFileAction.setEnabled(enable);
    this.parseInputAction.setEnabled(enable);
    this.treeHandlerBasicAction.setEnabled(enable);
    this.treeHandlerStructuredAction.setEnabled(enable);
    this.traceAllAction.setEnabled(enable);
    this.useCharSequenceAction.setEnabled(enable);
    this.useStringAction.setEnabled(enable);
  }
  
  Action parseInputAction = new AbstractAction("Parse text", Resources.alignLeft16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      ManagerTesting.this.myThread = new Thread()
      {
        public void run() 
        {
          ManagerTesting.this.parseStopAction.setEnabled(true);
          ManagerTesting.this.enableTestControls(false);
          ManagerTesting.this.runner(false);
          ManagerTesting.this.enableTestControls(true);
          ManagerTesting.this.parseStopAction.setEnabled(false);
          System.out.flush();
        }
      };
      ManagerTesting.this.myThread.start();
    }
  };
  
  Action parseFileAction = new AbstractAction("Parse file", Resources.host16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      ManagerTesting.this.myThread = new Thread()
      {
        public void run() 
        {
          ManagerTesting.this.parseStopAction.setEnabled(true);
          ManagerTesting.this.enableTestControls(false);
          if (ManagerTesting.this.fileChooser == null) 
          {
            ManagerTesting.this.fileChooser = new JFileChooser();
            ManagerTesting.this.fileChooser.setDialogTitle("Open");
            ManagerTesting.this.fileChooser.setFileSelectionMode(2);
            ManagerTesting.this.fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
          }
          if (ManagerTesting.this.fileChooser.showOpenDialog(ManagerTesting.this.gui) == 0) 
          {
            try 
            {
              ManagerTesting.this.runner(true);
            } 
            catch (Throwable e) 
            {
              e.printStackTrace();
            }
          }
          ManagerTesting.this.enableTestControls(true);
          ManagerTesting.this.parseStopAction.setEnabled(false);
          System.out.flush();
        }
      };
      ManagerTesting.this.myThread.start();
    }
  };
  
  Action parseStopAction = new AbstractAction("Stop parsing", Resources.stop16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      ManagerTesting.this.visitorParserGenerator.stopFlag[0] = true;
    }
  };
  
  Action treeHandlerBasicAction = new AbstractAction("Basic")
  {
    public void actionPerformed(ActionEvent e) 
    {
      ManagerTesting.this.printStructured = false;
    }
  };
  
  Action treeHandlerStructuredAction = new AbstractAction("Structured")
  {
    public void actionPerformed(ActionEvent e) 
    {
      ManagerTesting.this.printStructured = true;
    }
  };
  
  Action treeHandlerCustomAction = new AbstractAction("Custom")
  {
    public void actionPerformed(ActionEvent e) 
    {
      ManagerTesting.this.printStructured = false;
    }
  };
  
  Action useCharSequenceAction = new AbstractAction("CharSequence")
  {
    public void actionPerformed(ActionEvent e) 
    {
      ManagerTesting.this.useRichCharSequence = true;
    }
  };
  
  Action useStringAction = new AbstractAction("String")
  {
    public void actionPerformed(ActionEvent e) 
    {
      ManagerTesting.this.useRichCharSequence = false;
    }
  };
  
  Action traceAllAction = new AbstractAction("Trace all")
  {
    public void actionPerformed(ActionEvent e) 
    {
      ManagerTesting.this.traceAll = (!ManagerTesting.this.traceAll);
    }
  };
  
  Action logClearAction = new AbstractAction("Clear log", Resources.clear16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      ManagerTesting.this.gui.theTestingPanel.logClear();
      ManagerTesting.this.gui.theTestingPanel.logStatus.setText("");
    }
  };
  
  Action logCopyAction = new AbstractAction("Copy log", Resources.copy16)
  {

    public void actionPerformed(ActionEvent e) { ManagerTesting.this.gui.theTestingPanel.logCopy(); }
  };
  private Vll4jGui gui;
  private VisitorParserGeneration visitorParserGenerator;
  
  private void appendStatus(final String status, final boolean reset) 
  { 
  	SwingUtilities.invokeLater(new Thread()
    {
      public void run() 
      {
        if (reset) 
        {
          ManagerTesting.this.gui.theTestingPanel.logStatus.setText(status);
        } 
        else 
        {
          ManagerTesting.this.gui.theTestingPanel.logStatus.setText(ManagerTesting.this.gui.theTestingPanel.logStatus.getText() + status);
        }
      }
    }); 
  }
  
  private File[] dredgeFiles(File f) 
  {
    if (f.isDirectory()) 
    {
      List<File> lf = new ArrayList();
      dredgeFiles(f, lf);
      return (File[])lf.toArray(new File[lf.size()]);
    }
    return new File[] { f };
  }
  
  private void dredgeFiles(File root, List<File> lf) 
  {
    File[] files = root.listFiles();
    for (File f : files) 
    {
      if (f.isDirectory()) 
      {
        dredgeFiles(f, lf);
      } 
      else 
      {
        lf.add(f);
      }
    }
  }
  
  private void runner(boolean fromFile) 
  {
    NodeBase apex = this.gui.theTreePanel.rootNode;
    long t0 = System.currentTimeMillis();
    this.visitorParserGenerator = new VisitorParserGeneration(this.gui.theForest, this.gui.packratParsers, this.traceAll);
    this.gui.theForest.bindings.put("vllParserTestInput", this.gui.theTestingPanel.inputArea);
    this.gui.theForest.bindings.put("vllParserLog", this.gui.theTestingPanel.logArea);
    Parsers.Parser<? extends Object> parser = (Parsers.Parser)apex.accept(this.visitorParserGenerator);
    if (!this.visitorParserGenerator.parserGeneratedOk) 
    {
      JOptionPane.showMessageDialog(this.gui, "Can't generate parser\nReview rule definitions", fromFile ? "ERROR - Parse file" : "ERROR - Parse input", 0);
      
      appendStatus(" Can't generate parser - Review rule definitions", true);
      return;
    }
    long t1 = System.currentTimeMillis();
    appendStatus(String.format(" Combinators: %d ms", new Object[] { Long.valueOf(t1 - t0) }), true);
    
    File inFile = null;
    if (fromFile) 
    {
      inFile = this.fileChooser.getSelectedFile();
    }
    if ((fromFile) && (inFile.isDirectory())) 
    {
      this.gui.theTestingPanel.setMultiFileLog(false);
      inFile = this.fileChooser.getSelectedFile();
      t0 = System.currentTimeMillis();
      int countOk = 0;int countNotOk = 0;
      for (File f : dredgeFiles(inFile)) 
      {
        t1 = System.currentTimeMillis();
        try 
        {
          ReaderFile readerFile = new ReaderFile(f, this.useRichCharSequence);
          this.gui.theForest.bindings.put("vllSource", readerFile.source());
          Parsers.ParseResult pr = this.gui.packratParsers.parseAll(parser, readerFile);
          long t2 = System.currentTimeMillis();
          if (pr.successful()) 
          {
            countOk++;
            System.out.printf("%s (%d bytes %d ms): OK%n", new Object[] { f.getAbsolutePath(), Long.valueOf(f.length()), Long.valueOf(t2 - t1) });
          }
          else 
          {
            countNotOk++;
            System.err.printf("%s (%d bytes %d ms): ERROR (line=%d, col=%d)%n", new Object[] { f.getAbsolutePath(), Long.valueOf(f.length()), Long.valueOf(t2 - t1), Integer.valueOf(pr.next().line()), Integer.valueOf(pr.next().column()) });
          }
        }
        catch (Throwable t)
        {
          countNotOk++;
          if ((t.getCause() instanceof ScriptException)) 
          {
            t.printStackTrace();
            JOptionPane.showMessageDialog(this.gui, "Error in user-provided action-code\nDetails in stack-trace", "Action-code error", 0);
            
            break; }
          if ((t.getCause() instanceof StackOverflowError)) {
            System.err.printf("%s: ERROR: %s%n", new Object[] { f.getAbsolutePath(), t.getMessage() });
          } 
          else 
          {
            if ((t.getCause() instanceof IOException)) 
            {
              System.err.printf("User-Requested STOP%n", new Object[0]);
              break;
            }
            System.err.printf("Internal error: %s(%s)%n", new Object[] { t.getClass().getName(), t.getMessage() });
            t.printStackTrace();
          }
        }
        appendStatus(String.format(" %d Ok, %d NOk in %d ms", new Object[] { Integer.valueOf(countOk), Integer.valueOf(countNotOk), Long.valueOf(t1 - t0) }), true);
      }
    } 
    else 
    {
      this.gui.theTestingPanel.setMultiFileLog(true);
      t0 = System.currentTimeMillis();
      try 
      {
        Reader reader = fromFile ? new ReaderFile(inFile, this.useRichCharSequence) : new ReaderTextArea(this.gui.theTestingPanel.inputArea, this.useRichCharSequence);
        
        this.gui.theForest.bindings.put("vllSource", reader.source());
        Parsers.ParseResult pr = this.gui.packratParsers.parseAll(parser, reader);
        t1 = System.currentTimeMillis();
        appendStatus(String.format(", Parser: %d ms", new Object[] { Long.valueOf(t1 - t0) }), false);
        if (pr.successful()) 
        {
          t0 = System.currentTimeMillis();
          String ast = Utils.dumpValue(pr.get(), this.printStructured);
          t1 = System.currentTimeMillis();
          appendStatus(String.format(", AST->String: %d ms", new Object[] { Long.valueOf(t1 - t0) }), false);
          t0 = System.currentTimeMillis();
          System.out.println(ast);
          System.out.println();
          t1 = System.currentTimeMillis();
          appendStatus(String.format(", Printing: %d ms", new Object[] { Long.valueOf(t1 - t0) }), false);
        } 
        else 
        {
          System.err.printf("%s%n", new Object[] { this.gui.packratParsers.dumpResult(pr) });
        }
      } 
      catch (Throwable t) 
      {
        if ((t.getCause() instanceof ScriptException)) 
        {
          JOptionPane.showMessageDialog(this.gui, "Error in user-provided action-code\nSee details in stack-trace", "Action-code error", 0);
          
          t.printStackTrace();
        } 
        else if ((t.getCause() instanceof IOException)) 
        {
          System.err.printf("User-Requested STOP%n", new Object[0]);
        } 
        else 
        {
          System.err.printf("Internal error: %s(%s)%n", new Object[] { t.getClass().getName(), t.getMessage() });
          t.printStackTrace();
        }
      }
    }
  }
  
  private Thread myThread = null;
  private JFileChooser fileChooser = null;
  private boolean traceAll = false;
  private boolean useRichCharSequence = false;
  private boolean printStructured = false;
}
