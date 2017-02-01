package net.java.vll.vll4j.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.java.vll.vll4j.combinator.PackratParsers;
import net.java.vll.vll4j.combinator.Reader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;












public class Forest
{
  private void populateNode(Node xn, NodeBase pn)
  {
    NamedNodeMap attr = xn.getAttributes();
    if (attr.getNamedItem("Drop") != null) {
       pn.isDropped = true;
     }
     if (attr.getNamedItem("ActionText") != null) {
       pn.actionText = attr.getNamedItem("ActionText").getTextContent();
       String status = compileActionCode(pn);
       if (status != null) {
         throw new IllegalArgumentException(String.format("Action-Code error at '%s': %s%n", new Object[] { pn.nodeName(), status }));
       }
     }
     
     if (attr.getNamedItem("Description") != null) {
       pn.description = attr.getNamedItem("Description").getTextContent();
     }
     if (attr.getNamedItem("ErrMsg") != null) {
       pn.errorMessage = attr.getNamedItem("ErrMsg").getTextContent();
     }
     if (attr.getNamedItem("Mult") != null) {
       String m = attr.getNamedItem("Mult").getTextContent();
       if (m.equals("*")) {
         pn.multiplicity = Multiplicity.ZeroOrMore;
       } else if (m.equals("+")) {
         pn.multiplicity = Multiplicity.OneOrMore;
       } else if (m.equals("?")) {
         pn.multiplicity = Multiplicity.ZeroOrOne;
       } else if (m.equals("0")) {
         pn.multiplicity = Multiplicity.Not;
       } else if (m.equals("=")) {
         pn.multiplicity = Multiplicity.Guard;
       }
     }
     if ((!(pn instanceof NodeChoice)) && 
       (!(pn instanceof NodeLiteral)) && 
       (!(pn instanceof NodeReference)) && 
       (!(pn instanceof NodeRegex)) && 
       (!(pn instanceof NodeRepSep))) {
       if ((pn instanceof NodeRoot)) {
         if (attr.getNamedItem("Packrat") != null) {
           ((NodeRoot)pn).isPackrat = true;
         }
       } else if (((pn instanceof NodeSequence)) && 
         (attr.getNamedItem("Commit") != null)) {
         ((NodeSequence)pn).commitIndex = Integer.parseInt(attr.getNamedItem("Commit").getTextContent());
       }
     }
     NodeList clist = xn.getChildNodes();
     for (int i = 0; i < clist.getLength(); i++) {
       Node cn = clist.item(i);
       String elmtName = cn.getNodeName();
       if (elmtName.equals("Choice")) {
         NodeBase c = new NodeChoice();
         pn.add(c);
         populateNode(cn, c);
       } else if (elmtName.equals("Token")) {
         String tokenName = cn.getAttributes().getNamedItem("Ref").getTextContent();
         String tokenValue = (String)this.tokenBank.get(tokenName);
         NodeBase c = tokenValue.charAt(0) == 'L' ? new NodeLiteral(tokenName) : new NodeRegex(tokenName);
         
         pn.add(c);
         populateNode(cn, c);
       } else if (elmtName.equals("Reference")) {
         String ruleName = cn.getAttributes().getNamedItem("Ref").getTextContent();
         NodeBase c = new NodeReference(ruleName);
         pn.add(c);
         populateNode(cn, c);
       } else if (elmtName.equals("RepSep")) {
         NodeBase c = new NodeRepSep();
         pn.add(c);
         populateNode(cn, c);
       } else if (!elmtName.equals("Root"))
       {
         if (elmtName.equals("Sequence")) {
           NodeBase c = new NodeSequence();
           pn.add(c);
           populateNode(cn, c);
         }
       }
     }
   }
   
   public void openInputStream(InputStream is, boolean tokensOnly) throws ParserConfigurationException, SAXException, IOException {
     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
     DocumentBuilder db = dbf.newDocumentBuilder();
     Document d = db.parse(is);
     Element docElmt = d.getDocumentElement();
     NodeList regs = docElmt.getElementsByTagName("Regex");
     for (int i = 0; i < regs.getLength(); i++) {
       Node r = regs.item(i);
       String regName = r.getAttributes().getNamedItem("Name").getTextContent();
       String regPat = r.getAttributes().getNamedItem("Pattern").getTextContent();
       this.tokenBank.put(regName, "R" + regPat);
     }
     NodeList lits = docElmt.getElementsByTagName("Literal");
     for (int i = 0; i < lits.getLength(); i++) {
       Node r = lits.item(i);
       String litName = r.getAttributes().getNamedItem("Name").getTextContent();
       String litPat = r.getAttributes().getNamedItem("Pattern").getTextContent();
       this.tokenBank.put(litName, "L" + litPat);
     }
     if (!tokensOnly) {
       this.whiteSpace = docElmt.getElementsByTagName("Whitespace").item(0).getTextContent();
       this.comment = docElmt.getElementsByTagName("Comments").item(0).getTextContent();
       NodeList parsers = docElmt.getElementsByTagName("Parser");
       for (int i = 0; i < parsers.getLength(); i++) {
         Node xNode = parsers.item(i);
         String ruleName = xNode.getAttributes().getNamedItem("Name").getTextContent();
         NodeRoot root = new NodeRoot(ruleName);
         populateNode(xNode, root);
         this.ruleBank.put(ruleName, root);
       }
     }
   }
   
   private ActionFunction compile(String script) throws ScriptException {
     if (this.compilable == null) {
       this.scriptEngine = new ScriptEngineManager().getEngineByName("JavaScript");
       this.compilable = ((Compilable)this.scriptEngine);
     }
     script = script.substring(script.indexOf('('));
     final CompiledScript cs = this.compilable.compile(String.format("(function %s)(vllARG)", new Object[] { script }));
 return new ActionFunction()
     {
       public Object run(Object arg, Reader input, int endOffset) throws ScriptException {
         cs.getEngine().put("vllARG", arg);
         cs.getEngine().put("vllLine", Integer.valueOf(input.line()));
         cs.getEngine().put("vllCol", Integer.valueOf(input.column()));
         cs.getEngine().put("vllOffset", Integer.valueOf(input.offset()));
         cs.getEngine().put("vllInput", input.source().subSequence(input.offset(), endOffset));
         cs.getEngine().put("vllLastNoSuccess", PackratParsers.lastNoSuccess);
         for (String k : Forest.this.bindings.keySet()) {
           cs.getEngine().put(k, Forest.this.bindings.get(k));
         }
         return cs.eval();
       }
     };   }
   
   public String compileActionCode(NodeBase node) {
     String script = node.actionText;
     if (script.trim().isEmpty()) {
       node.actionFunction = null;
       return null;
     }
     if (!this.functionMatcher.reset(script).matches()) {
       node.actionFunction = null;
       return "Need JavaScript function with 1 argument";
     }
     try {
       node.actionFunction = compile(script);
       return null;
     } catch (Exception e) {
       node.actionFunction = null;
       String message = e.getMessage();
       return message.contains(": ") ? message.substring(message.indexOf(": ") + 2) : message;
     }
   }
   
 
 
   ScriptEngine scriptEngine = null;
   Compilable compilable = null;
   private Matcher functionMatcher = Pattern.compile("\\s*f(?:u(?:n(?:c(?:t(?:i(?:on?)?)?)?)?)?)?\\s*\\(\\s*[a-zA-Z][a-zA-Z0-9]*\\s*\\)(?s:.*)").matcher("");
   
   public Map<String, String> tokenBank = new TreeMap();
   public Map<String, NodeBase> ruleBank = new TreeMap();
  public String whiteSpace;
  public String comment; public Map<String, Object> bindings = new HashMap();
}
