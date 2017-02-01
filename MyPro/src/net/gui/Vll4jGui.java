package net.gui;
 
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URL;
import java.util.Map;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import net.api.Forest;
import net.api.NodeRoot;
import net.combinator.PackratParsers;

public class Vll4jGui extends JFrame
{

	private static final long serialVersionUID = 1L;
  private Vll4jGui()
  {
    super("LL(k) Parser Generator");
    this.titleString = getTitle();
    initializeAllData();
    this.theRuleManager.theComboBox.setMaximumSize(this.theRuleManager.theComboBox.getPreferredSize());
    createGui();
  }
  
  private void initializeAllData() 
  {
    this.theHelpFunctionsManager = new ManagerHelp(this);
    this.theTestManager = new ManagerTesting(this);
    this.theTokenManager = new ManagerTokens(this);
    this.theFileManager = new ManagerFileOps(this);
    this.theRuleManager = new ManagerRules(this);
    this.theRuleManager.theComboBox = new JComboBox();
    this.theRuleManager.theComboBox.setToolTipText("Select rule");
    this.theRuleManager.theComboBox.setRenderer(this.theCellRenderer);
    this.theMiscFunctionsManager = new ManagerMiscOps(this);
    reset(true);
  }
  
  void reset(boolean withMain) 
  {
    this.theForest.tokenBank.clear();
    this.theForest.ruleBank.clear();
    if (withMain)
      this.theForest.ruleBank.put("Main", new NodeRoot("Main"));
    this.theRuleManager.theComboBox.setAction(null);
    this.theRuleManager.theComboBox.removeAllItems();
    if (withMain)
      this.theRuleManager.theComboBox.addItem("Main");
    this.theRuleManager.theComboBox.setAction(this.theRuleManager.comboBoxAction);
    if ((withMain) && (this.theTreePanel != null))
      this.theTreePanel.setRuleName("Main");
    setTitle(this.titleString);
  }
  
  private void createGui() 
  {
    setSize(this.frameWidth, this.frameHeight);
    setLocation((this.screenSize.width - this.frameWidth) / 2, (this.screenSize.height - this.frameHeight) / 2);
    setDefaultCloseOperation(3);
    setIconImage(Resources.icon.getImage());
    addMenuBar();
    addToolBar();
    this.theTreePanel = new PanelRuleTree(this);
    this.theTestingPanel = new PanelTesting(this);
    this.theAstPanel = new PanelAstDisplay(this);
    this.theActionCodePanel = new PanelActionCode(this);
    JSplitPane sp1 = new JSplitPane(1, this.theAstPanel, this.theActionCodePanel);
    JSplitPane sp2 = new JSplitPane(1, this.theTreePanel, sp1);
    sp2.setDividerLocation(this.screenSize.width / 4);
    JSplitPane sp = new JSplitPane(0, sp2, this.theTestingPanel);
    add(sp, "Center");
  }
  
  private void addMenuBar() 
  {
    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);
    menuBar.add(this.fileMenu);
    this.fileMenu.add(this.theFileManager.fileNewAction);
    this.fileMenu.add(new JSeparator());
    this.fileMenu.add(this.theFileManager.fileOpenAction);
    this.fileMenu.add(this.theFileManager.fileSaveAction);
    this.fileMenu.add(this.theFileManager.fileSaveAsAction);
    this.fileMenu.add(new JSeparator());
    this.fileMenu.add(this.theFileManager.fileExitAction);
    menuBar.add(this.viewMenu);
    menuBar.add(this.tokenMenu);
    this.tokenMenu.add(this.theTokenManager.newLiteralAction);
    this.tokenMenu.add(this.theTokenManager.newRegexAction);
    this.tokenMenu.add(this.theTokenManager.editTokenAction);
    this.tokenMenu.add(this.theTokenManager.findTokenAction);
    this.tokenMenu.add(new JSeparator());
    this.tokenMenu.add(this.theTokenManager.deleteTokenAction);
    this.tokenMenu.add(new JSeparator());
    this.tokenMenu.add(this.theFileManager.importTokenAction);
    this.tokenMenu.add(this.theFileManager.exportTokenAction);
    menuBar.add(this.ruleMenu);
    this.ruleMenu.add(this.theRuleManager.ruleNewAction);
    this.ruleMenu.add(this.theRuleManager.ruleFindAction);
    this.ruleMenu.add(new JSeparator());
    this.ruleMenu.add(this.theRuleManager.ruleRenameAction);
    this.ruleMenu.add(this.theRuleManager.ruleOptimizeAction);
    this.ruleMenu.add(new JSeparator());
    this.ruleMenu.add(this.theRuleManager.ruleDeleteAction);
    menuBar.add(this.globalsMenu);
    this.globalsMenu.add(this.theMiscFunctionsManager.globalsWhitespaceAction);
    this.globalsMenu.add(this.theMiscFunctionsManager.globalsCommentAction);
    menuBar.add(this.logMenu);
    this.logMenu.add(this.theTestManager.logCopyAction);
    this.logMenu.add(this.theTestManager.logClearAction);
    menuBar.add(this.testMenu);
    this.testMenu.add(this.theTestManager.parseInputAction);
    this.testMenu.add(this.theTestManager.parseFileAction);
    this.testMenu.add(new JSeparator());
    this.testMenu.add(this.testTextTypeMenu);
    ButtonGroup bg2 = new ButtonGroup();
    this.testTextTypeString = new JRadioButtonMenuItem(this.theTestManager.useStringAction);
    this.testTextTypeString.setSelected(true);
    this.testTextTypeMenu.add(this.testTextTypeString);
    this.testTextTypeCharSequence = new JRadioButtonMenuItem(this.theTestManager.useCharSequenceAction);
    this.testTextTypeMenu.add(this.testTextTypeCharSequence);
    bg2.add(this.testTextTypeString);
    bg2.add(this.testTextTypeCharSequence);
    this.testMenu.add(new JSeparator());
    this.testMenu.add(this.testTreeHandlerMenu);
    ButtonGroup bg = new ButtonGroup();
    this.testTreeHandlerBasic = new JRadioButtonMenuItem(this.theTestManager.treeHandlerBasicAction);
    this.testTreeHandlerBasic.setSelected(true);
    this.testTreeHandlerMenu.add(this.testTreeHandlerBasic);
    this.testTreeHandlerStructured = new JRadioButtonMenuItem(this.theTestManager.treeHandlerStructuredAction);
    this.testTreeHandlerMenu.add(this.testTreeHandlerStructured);
    bg.add(this.testTreeHandlerBasic);
    bg.add(this.testTreeHandlerStructured);
    this.testTraceAllItem = new JCheckBoxMenuItem(this.theTestManager.traceAllAction);
    this.testMenu.add(this.testTraceAllItem);
    menuBar.add(this.helpMenu);
    this.helpMenu.add(this.theHelpFunctionsManager.displayHelpMain);
    this.helpMenu.add(this.helpSamplesMenu);
    this.helpSamplesMenu.add(new JMenuItem(this.theHelpFunctionsManager.sampleTdarExpr));
    this.helpSamplesMenu.add(new JMenuItem(this.theHelpFunctionsManager.sampleTdarExprActions));
    this.helpSamplesMenu.add(new JMenuItem(this.theHelpFunctionsManager.sampleTdarExprAst));
    this.helpSamplesMenu.add(new JMenuItem(this.theHelpFunctionsManager.sampleTdarSimpleTreeInterpreter));
    this.helpSamplesMenu.add(new JSeparator());
    this.helpSamplesMenu.add(new JMenuItem(this.theHelpFunctionsManager.samplePs2eArithExpr));
    this.helpSamplesMenu.add(new JMenuItem(this.theHelpFunctionsManager.samplePs2eSimpleJson));
    this.helpSamplesMenu.add(new JMenuItem(this.theHelpFunctionsManager.samplePs2eArithExprAction));
    this.helpSamplesMenu.add(new JSeparator());
    this.helpSamplesMenu.add(new JMenuItem(this.theHelpFunctionsManager.samplePswpPayrollParserCombinators));
    this.helpMenu.add(this.theHelpFunctionsManager.versionCheck);
    this.helpMenu.add(this.theHelpFunctionsManager.aboutAction);
  }
  
  private Action tip(Action aa) 
  {
    aa.putValue("ShortDescription", aa.getValue("Name"));
    return aa;
  }
  
  void addToolBar() 
  {
    JToolBar toolBar = new JToolBar();
    getContentPane().add(toolBar, "North");
    toolBar.add(tip(this.theFileManager.fileNewAction));
    toolBar.add(tip(this.theFileManager.fileOpenAction));
    toolBar.add(tip(this.theFileManager.fileSaveAction));
    toolBar.add(tip(this.theFileManager.fileSaveAsAction));
    toolBar.addSeparator();
    this.theRuleManager.ruleBackAction.setEnabled(false);
    toolBar.add(tip(this.theRuleManager.ruleBackAction));
    toolBar.add(this.theRuleManager.theComboBox);
    toolBar.add(tip(this.theRuleManager.ruleNewAction));
    toolBar.add(tip(this.theRuleManager.ruleFindAction));
    toolBar.add(tip(this.theRuleManager.ruleOptimizeAction));
    toolBar.add(tip(this.theRuleManager.ruleRenameAction));
    toolBar.addSeparator();
    toolBar.add(tip(this.theTokenManager.newLiteralAction));
    toolBar.add(tip(this.theTokenManager.newRegexAction));
    toolBar.add(tip(this.theTokenManager.findTokenAction));
    toolBar.add(tip(this.theTokenManager.editTokenAction));
    toolBar.add(tip(this.theFileManager.importTokenAction));
    toolBar.add(tip(this.theFileManager.exportTokenAction));
    toolBar.addSeparator();
    toolBar.add(tip(this.theTestManager.parseInputAction));
    toolBar.add(tip(this.theTestManager.parseFileAction));
    toolBar.add(tip(this.theTestManager.parseStopAction));
    toolBar.addSeparator();
    toolBar.add(tip(this.theTestManager.logCopyAction));
    toolBar.add(tip(this.theTestManager.logClearAction));
    this.theTestManager.parseStopAction.setEnabled(false);
  }
  

  void setGrammarName(String grammarName)
  {
    setTitle(String.format("%s - %s", new Object[] { this.titleString, grammarName }));
  }
  
  void scalaNotice() 
  {
    String msg = "<html>If you followed a link in the article \"Grammar Without Tears\" <br/>to download this program, please read the updated version of <br/>the article at the following link: <br/><br/>http://vll.java.net/GrammarWithoutTears2/GrammarWithoutTears2.html<br/><br/>The current version of VisualLangLab (this program) does not <br/> support actions written in Scala (as described in the<br/>original article). <br/><br/>A version of this program without this popup notice can be <br/>downloaded from the following link: <br/><br/>http://java.net/projects/vll/downloads/download/VLL4J.jar<br/><br/>The original Scala version of VisualLangLab (<u>only</u> for those who <br/>must write actions in Scala) is available at the following link:<br/><br/>http://java.net/projects/vll/downloads/download/VLLS-Scala.jar<br/><br/>For more information go to http://vll.java.net/";
    URL is = ClassLoader.getSystemClassLoader().getResource("vlls.txt");
    if (is != null) 
    {
      JOptionPane.showMessageDialog(this, msg, "Important Notice", -1, null);
    }
  }
  
  public static void main(String[] args) 
  {
    if (System.getProperty("os.name").contains("Windows")) 
    {
      try 
      {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
      } catch (Exception e) {}
    }
    Vll4jGui me = new Vll4jGui();
    System.setOut(me.theTestingPanel.getOutStream());
    System.setErr(me.theTestingPanel.getErrStream());
    me.setVisible(true);
    me.scalaNotice();
  }
  
  PanelRuleTree theTreePanel = null;
  PanelTesting theTestingPanel = null;
  PanelActionCode theActionCodePanel = null;
  PanelAstDisplay theAstPanel = null;
  ManagerTesting theTestManager = null;
  ManagerTokens theTokenManager = null;
  ManagerFileOps theFileManager = null;
  ManagerRules theRuleManager = null;
  ManagerMiscOps theMiscFunctionsManager = null;
  ManagerHelp theHelpFunctionsManager = null;
  PackratParsers packratParsers = new PackratParsers();
  Forest theForest = new Forest();
  JMenu fileMenu = new JMenu("File");
  JMenu viewMenu = new JMenu("View");
  JMenu tokenMenu = new JMenu("Tokens");
  JMenu ruleMenu = new JMenu("Rules");
  JMenu globalsMenu = new JMenu("Globals");
  JMenu logMenu = new JMenu("Log");
  JMenu testMenu = new JMenu("Test");
  JMenu testTextTypeMenu = new JMenu("Text type");
  JMenu testTreeHandlerMenu = new JMenu("Tree handler");
  JMenuItem testTextTypeString = null;
  JMenuItem testTextTypeCharSequence = null;
  JMenuItem testTreeHandlerBasic = null;
  JMenuItem testTreeHandlerStructured = null;
  JMenuItem testTraceAllItem = null;
  JMenuItem useRichCharSequenceItem = null;
  JMenu helpMenu = new JMenu("Help");
  JMenu helpSamplesMenu = new JMenu("Sample grammars");
  Toolkit tk = Toolkit.getDefaultToolkit();
  Dimension screenSize = this.tk.getScreenSize();
  int frameWidth = (int)(this.screenSize.width * 1D);
  int frameHeight = (int)(this.screenSize.height * 1D);
  private String titleString;
  public final String version = "11.03";
  private ListCellRenderer theCellRenderer = new RendererRuleComboBox(this);
}