package net.java.vll.vll4j.gui;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

public class ManagerHelp
{
  ManagerHelp(Vll4jGui gui)
  {
    this.gui = gui; Object[] 
      tmp269_266 = new Object[1];gui.getClass();tmp269_266[0] = "11.03";this.title = String.format("LL(k) Parser Generator %s", tmp269_266);
    if (Desktop.isDesktopSupported()) 
    {
      this.desktop = Desktop.getDesktop();
    }
  }
  
  Action aboutAction = new AbstractAction("About LL(k) Parser Generator", Resources.information16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      JTabbedPane tp = new JTabbedPane();
      tp.add("LL(K) PARSER GENERATOR", new JLabel(Resources.splashImage));
      tp.add("Copyright", new JLabel(this.copyright));
      tp.add("Licenses", new JLabel(this.licenses));
      JOptionPane.showMessageDialog(ManagerHelp.this.gui, tp, ManagerHelp.this.title, -1); 
  	}
    String copyright = "";
    String licenses = "";
    };

  Action versionCheck = new AbstractAction("Version check", Resources.tipOfTheDay16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      JOptionPane.showMessageDialog(ManagerHelp.this.gui, "Alpha", "Version check", -1);
    }
  };
  
  Action sampleTdarExpr = new AbstractAction("TDAR-Expr")
  {
    public void actionPerformed(ActionEvent e) 
    {
      JOptionPane.showMessageDialog(ManagerHelp.this.gui, this.msg, "TDAR-Expr", 1);
      InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("net/java/vll/vll4j/gui/resources/TDAR-Expr.vll");
      
      ManagerHelp.this.gui.reset(false);
      ManagerHelp.this.gui.theFileManager.openInputStream(is, false);
      ManagerHelp.this.gui.setGrammarName("TDAR-Expr");
      ManagerHelp.this.gui.theRuleManager.reset(); 
  	}
    String msg = "<html>This example is based on the parser described in <br/>section 3.1, <i>Recognizing Language Syntax</i>, <br/>of the book <i>The Definitive ANTLR Reference</i> <br>(http://pragprog.com/book/tpantlr/the-definitive-antlr-reference)<br/><br/>NOTE: Each 'statement' in the test input <br/>(including the last) must end with a NEWLINE<br/><br/>Read more about this parser at the following url:<br/>&nbsp;&nbsp;&nbsp;&nbsp;http://vll.java.net/examples/a-quick-tour.html";
  };
  
  Action sampleTdarExprActions = new AbstractAction("TDAR-Expr-Action")
  {
    public void actionPerformed(ActionEvent e) 
    {
      JOptionPane.showMessageDialog(ManagerHelp.this.gui, this.msg, "TDAR-Expr-Action", 1);
      InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("net/java/vll/vll4j/gui/resources/TDAR-Expr-Action.vll");
      
      ManagerHelp.this.gui.reset(false);
      ManagerHelp.this.gui.theFileManager.openInputStream(is, false);
      ManagerHelp.this.gui.setGrammarName("TDAR-Expr-Action");
      ManagerHelp.this.gui.theRuleManager.reset(); 
  	}  
    String msg = "<html>This example is based on the parser described in <br/>section 3.2, <i>Using Syntax to Drive Action Execution</i>, <br/>of the book <i>The Definitive ANTLR Reference</i> <br>(http://pragprog.com/book/tpantlr/the-definitive-antlr-reference)<br/><br/>Actions (JavaScript functions) are associated with some <br/> rule-tree nodes identified by the <i>action</i> attribute<br/><br/>NOTE: Each 'statement' in the test input <br/>(including the last) must end with a NEWLINE<br/><br/>Read more about this parser at the following url:<br/>&nbsp;&nbsp;&nbsp;&nbsp;http://vll.java.net/examples/a-quick-tour.html";
  };
 
  Action sampleTdarExprAst = new AbstractAction("TDAR-Expr-AST")
  {
    public void actionPerformed(ActionEvent e) 
    {
      JOptionPane.showMessageDialog(ManagerHelp.this.gui, this.msg, "TDAR-Expr-AST", 1);
      InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("net/java/vll/vll4j/gui/resources/TDAR-Expr-AST.vll");
      
      ManagerHelp.this.gui.reset(false);
      ManagerHelp.this.gui.theFileManager.openInputStream(is, false);
      ManagerHelp.this.gui.setGrammarName("TDAR-Expr-AST");
      ManagerHelp.this.gui.theRuleManager.reset(); 
  	} 
    String msg = "<html>This grammar supports discussion of the techinque in section 3.3, <br/><i>Evaluating Expressions Using an AST Intermediate Form</i>, <br/>of the book <i>The Definitive ANTLR Reference</i> <br>(http://pragprog.com/book/tpantlr/the-definitive-antlr-reference)<br/><br/>Further discussion of the use of this grammar can be <br/>found at the following url:<br/>&nbsp;&nbsp;&nbsp;&nbsp;http://vll.java.net/examples/a-quick-tour.html";
  };
  
  Action sampleTdarSimpleTreeInterpreter = new AbstractAction("TDAR-Simple-Tree-Based-Interpreter")
  {
    public void actionPerformed(ActionEvent e) 
    {
      JOptionPane.showMessageDialog(ManagerHelp.this.gui, this.msg, "TDAR-Simple-Tree-Based-Interpreter", 1);
      InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("net/java/vll/vll4j/gui/resources/TDAR-Simple-Tree-Based-Interpreter.vll");
      
      ManagerHelp.this.gui.reset(false);
      ManagerHelp.this.gui.theFileManager.openInputStream(is, false);
      ManagerHelp.this.gui.setGrammarName("TDAR-Simple-Tree-Based-Interpreter");
      ManagerHelp.this.gui.theRuleManager.reset(); 
  	}   
    String msg = "<html>This example is based on the parser described at the very end of <br/>section 3.3, <i>Evaluating Expressions Using an AST Intermediate Form</i>, <br/>of the book <i>The Definitive ANTLR Reference</i> <br/>(http://pragprog.com/book/tpantlr/the-definitive-antlr-reference)<br/><br/>Complete details can be found here: <br/> http://www.antlr.org/wiki/display/ANTLR3/Simple+tree-based+interpeter<br/><br/>You can find sample test input at the web-page above. <br/><br/>NOTE: Each 'statement' in the test input <br/>(including the last) must end with a NEWLINE<br/><br/>Read more about this parser at the following url:<br/>&nbsp;&nbsp;&nbsp;&nbsp;http://vll.java.net/examples/a-quick-tour.html";
  };
 
  Action samplePs2eArithExpr = new AbstractAction("PS2E-ArithExpr")
  {
    public void actionPerformed(ActionEvent e) 
    {
      JOptionPane.showMessageDialog(ManagerHelp.this.gui, this.msg, "PS2E-ArithExpr", 1);
      InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("net/java/vll/vll4j/gui/resources/PS2E-ArithExpr.vll");
      
      ManagerHelp.this.gui.reset(false);
      ManagerHelp.this.gui.theFileManager.openInputStream(is, false);
      ManagerHelp.this.gui.setGrammarName("PS2E-ArithExpr");
      ManagerHelp.this.gui.theRuleManager.reset(); 
  	}  
    String msg = "<html>This parser is based on the code at page 760 of \"Programming in Scala\"<br/>(http://www.artima.com/shop/programming_in_scala_2ed)<br/><br/>Another version of this parser with action-code that evaluages the<br>AST to a number is also available (P2SE-ArithExpr-Action).<br/><br/>IMPORTANT: Select the top-level parser (Expr) when running it.<br/>Sample input (remove quotes): \"(2 + 3) * (7 - 3)\"</html>";
  };
  
  Action samplePs2eSimpleJson = new AbstractAction("PS2E-SimpleJSON")
  {
    public void actionPerformed(ActionEvent e) 
    {
      JOptionPane.showMessageDialog(ManagerHelp.this.gui, this.msg, "PS2E-SimpleJSON", 1);
      InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("net/java/vll/vll4j/gui/resources/PS2E-SimpleJSON.vll");
      
      ManagerHelp.this.gui.reset(false);
      ManagerHelp.this.gui.theFileManager.openInputStream(is, false);
      ManagerHelp.this.gui.setGrammarName("PS2E-SimpleJSON");
      ManagerHelp.this.gui.theRuleManager.reset(); 
  	}   
    String msg = "<html>This parser is based on the code at page 764 of \"Programming in Scala\"<br/>(http://www.artima.com/shop/programming_in_scala_2ed)<br/><br/>IMPORTANT: Select the top-level parser (Value) when running it.<br/></html>";
  };
 
  Action samplePs2eArithExprAction = new AbstractAction("PS2E-ArithExpr-Action")
  {
    public void actionPerformed(ActionEvent e) 
    {
      JOptionPane.showMessageDialog(ManagerHelp.this.gui, this.msg, "PS2E-ArithExpr-Action", 1);
      InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("net/java/vll/vll4j/gui/resources/PS2E-ArithExpr-Action.vll");
      
      ManagerHelp.this.gui.reset(false);
      ManagerHelp.this.gui.theFileManager.openInputStream(is, false);
      ManagerHelp.this.gui.setGrammarName("PS2E-ArithExpr-Action");
      ManagerHelp.this.gui.theRuleManager.reset(); 
  	}  
    String msg = "<html>This parser is also based on the code at page 760 of \"Programming in Scala\"<br/>(http://www.artima.com/shop/programming_in_scala_2ed),<br/>but it also has actions that evaluate the AST to a number.<br><br/>IMPORTANT: Select the top-level parser (Expr) when running it.<br/>Sample input (remove quotes): \"(2 + 3) * (7 - 3)\"</html>";
  };
 
  Action samplePswpPayrollParserCombinators = new AbstractAction("PSWP-Payroll-Parser-Combinators")
  {
    public void actionPerformed(ActionEvent e) 
    {
      JOptionPane.showMessageDialog(ManagerHelp.this.gui, this.msg, "PSWP-Payroll-Parser-Combinators", 1);
      InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("net/java/vll/vll4j/gui/resources/PSWP-Payroll-Parser-Combinators.vll");
      
      ManagerHelp.this.gui.reset(false);
      ManagerHelp.this.gui.theFileManager.openInputStream(is, false);
      ManagerHelp.this.gui.setGrammarName("PSWP-Payroll-Parser-Combinators");
      ManagerHelp.this.gui.theRuleManager.reset(); 
  	}   
    String msg = "<html>This example is based on the parser described around page-240 <br/>of the book <i>Programming Scala</i> (http://programmingscala.com/)<br/><br/>An online version of the parser code can be found here: <br/> http://ofps.oreilly.com/titles/9780596155957/DomainSpecificLanguages.html<br/>&nbsp;&nbsp;&nbsp;&nbsp;#_generating_paychecks_with_the_external_dsl<br/><br/>This parser also includes a special rule that serves as a <i>test-harness</i> <br/>by using an action-function to wrap the main rule in a <br/>test-data setup part and a output-checker part. <br/>The rule called <i>PaycheckTester</i> is the test-harness. <br/><br/>Read more about this parser at the following url:<br/>&nbsp;&nbsp;&nbsp;&nbsp;http://vll.java.net/RapidPrototypingForScala.html";
  };
  
  Action displayHelpMain = new AbstractAction("Web-Site", Resources.icon)
  {
    public void actionPerformed(ActionEvent e) 
    {
      try 
      {
        URI webLink = new URL("http:www.google.com/").toURI();
        ManagerHelp.this.desktop.browse(webLink);
      } 
      catch (Exception ex) 
      {
        if (ManagerHelp.this.desktop == null) 
        {
          JOptionPane.showMessageDialog(ManagerHelp.this.gui, "Error/", "Unsupported: java.awt.Desktop", -1);
        }
        else 
        {
          ex.printStackTrace();
        }
      }
    }
  };

  Action displayHelpRuleTree = new AbstractAction("", Resources.help16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      JLabel hlpLabel = new JLabel(ManagerHelp.this.getHtml("PanelRuleTree.html"));
      if (0 == JOptionPane.showConfirmDialog(ManagerHelp.this.gui, hlpLabel, "Rule Tree - " + ManagerHelp.this.title, 0, -1)) 
      {
        try
        {
          URI webLink = new URL("http://vll.java.net/EditingTheGrammarTree.html").toURI();
          ManagerHelp.this.desktop.browse(webLink);
        } 
        catch (Exception ex) 
        {
          if (ManagerHelp.this.desktop == null) 
          {
            JOptionPane.showMessageDialog(ManagerHelp.this.gui, "Error", "Unsupported: java.awt.Desktop", -1);
          }
          else 
          {
            ex.printStackTrace();
          }
        }
      }
    }
  };

  Action displayHelpAST = new AbstractAction("", Resources.help16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      JLabel hlpLabel = new JLabel(ManagerHelp.this.getHtml("PanelAST.html"));
      if (0 == JOptionPane.showConfirmDialog(ManagerHelp.this.gui, hlpLabel, "Parse Tree (AST) Structure - " + ManagerHelp.this.title, 0, -1)) 
      {
        try
        {
          URI webLink = new URL("http:www.google.com").toURI();
          ManagerHelp.this.desktop.browse(webLink);
        } 
        catch (Exception ex) 
        {
          if (ManagerHelp.this.desktop == null) 
          {
            JOptionPane.showMessageDialog(ManagerHelp.this.gui, "Error", "Unsupported: java.awt.Desktop", -1);
          }
          else 
          {
            ex.printStackTrace();
          }
        }
      }
    }
  };

  Action displayHelpActionCode = new AbstractAction("", Resources.help16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      JLabel hlpLabel = new JLabel(ManagerHelp.this.getHtml("PanelActionCode.html"));
      if (0 == JOptionPane.showConfirmDialog(ManagerHelp.this.gui, hlpLabel, "Action Code - " + ManagerHelp.this.title, 0, -1)) 
      {
        try
        {
          URI webLink = new URL("http://vll.java.net/ASTAndActionCode.html#ActionCodeDesign").toURI();
          ManagerHelp.this.desktop.browse(webLink);
        } 
        catch (Exception ex) 
        {
          if (ManagerHelp.this.desktop == null) 
          {
            JOptionPane.showMessageDialog(ManagerHelp.this.gui, "Error", "Unsupported: java.awt.Desktop", -1);
          }
          else 
          {
            ex.printStackTrace();
          }
        }
      }
    }
  };
  Action displayHelpTestInput = new AbstractAction("", Resources.help16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      JLabel hlpLabel = new JLabel(ManagerHelp.this.getHtml("PanelTestInput.html"));
      if (0 == JOptionPane.showConfirmDialog(ManagerHelp.this.gui, hlpLabel, "Parser Test Input - " + ManagerHelp.this.title, 0, -1)) 
      {
        try
        {
          URI webLink = new URL("http://vll.java.net/TestingParsers.html").toURI();
          ManagerHelp.this.desktop.browse(webLink);
        } 
        catch (Exception ex) 
        {
          if (ManagerHelp.this.desktop == null) 
          {
            JOptionPane.showMessageDialog(ManagerHelp.this.gui, "Error", "Unsupported: java.awt.Desktop", -1);
          }
          else 
          {
            ex.printStackTrace();
          }
        }
      }
    }
  };
  Action displayHelpTestLog = new AbstractAction("", Resources.help16)
  {
    public void actionPerformed(ActionEvent e) 
    {
      JLabel hlpLabel = new JLabel(ManagerHelp.this.getHtml("PanelTestLog.html"));
      if (0 == JOptionPane.showConfirmDialog(ManagerHelp.this.gui, hlpLabel, "Parser Log - " + ManagerHelp.this.title, 0, -1))
        try
        {
          URI webLink = new URL("http://vll.java.net/TestingParsers.html").toURI();
          ManagerHelp.this.desktop.browse(webLink);
        } 
        catch (Exception ex) 
        {
          if (ManagerHelp.this.desktop == null) 
          {
            JOptionPane.showMessageDialog(ManagerHelp.this.gui, "Error", "Unsupported: java.awt.Desktop", -1);
          }
          else
            ex.printStackTrace();
        }
    }
  };

  private Vll4jGui gui;
  String title;
  
  private String getHtml(String name) 
  { 
  	ClassLoader cl = ClassLoader.getSystemClassLoader();
    URL res = cl.getResource("net/java/vll/vll4j/gui/resources/" + name);
    try 
    {
      InputStream hs = res.openStream();
      byte[] buf = new byte[hs.available()];
      hs.read(buf);
      return new String(buf);
    } catch (Exception e) {}
    return "";
  }
  private Desktop desktop = null;
}
