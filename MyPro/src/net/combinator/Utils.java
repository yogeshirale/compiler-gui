package net.combinator;

import java.util.HashMap;
import java.util.List;

public class Utils
{
  private static HashMap<String, Character> decodeMap = new HashMap();
  
  static { String[] keys = { "&#xA;", "&#x9;", "&#xD;", "&amp;", "&lt;", "&gt;", "&quot;", "&apos;" };
    char[] vals = { '\n', '\t', '\r', '&', '<', '>', '"', '\'' };
    for (int i = 0; i < keys.length; i++)
      decodeMap.put(keys[i], Character.valueOf(vals[i]));
  }
  
  public static String encode4xml(String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      char ch = s.charAt(i);
      switch (ch) {
      case '\n':  sb.append("&#xA;"); break;
      case '\t':  sb.append("&#x9;"); break;
      case '\r':  sb.append("&#xD;"); break;
      case '&':  sb.append("&amp;"); break;
      case '<':  sb.append("&lt;"); break;
      case '>':  sb.append("&gt;"); break;
      case '"':  sb.append("&quot;"); break;
      case '\'':  sb.append("&apos;"); break;
      default:  sb.append(ch);
      }
    }
    return sb.toString();
  }
  
  public static String decode4xml(String s)
  {
    StringBuilder sb = new StringBuilder();
    StringBuilder esc = new StringBuilder();
    boolean escaping = false;
    for (int i = 0; i < s.length(); i++) {
      char ch = s.charAt(i);
      if (escaping) {
        esc.append(ch);
        if (ch == ';') {
          escaping = false;
          sb.append(decodeMap.get(esc.toString()));
          esc.setLength(0);
        }
      }
      else if (ch == '&') {
        escaping = true;
        esc.append(ch);
      } else {
        sb.append(ch);
      }
    }
    

    return sb.toString();
  }
  
  public static String escapeMetachars(String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      char ch = s.charAt(i);
      switch (ch) {
      case '$': case '(': case ')': case '*': case '+': case ',': case '-': case '.': 
      case '?': case '[': case '\\': case ']': case '^': case '{': case '|': case '}': 
        sb.append('\\').append(ch);
        break;
      default:  sb.append(ch);
      }
    }
    return sb.toString();
  }
  
  public static String unEscape(String s) {
    if (!s.contains("\\")) {
      return s;
    }
    StringBuilder sb = new StringBuilder();StringBuilder octals = new StringBuilder();
    boolean escaping = false;
    for (int i = 0; i < s.length(); i++) {
      char ch = s.charAt(i);
      if (escaping) {
        if ((ch >= '0') && (ch <= '7')) {
          octals.append(ch);
        } else {
          if (octals.length() > 0) {
            sb.append((char)(Integer.parseInt(octals.toString(), 8) & 0xFF));
            octals.setLength(0);
          }
          escaping = false;
          switch (ch) {
          case 'b':  sb.append('\b'); break;
          case 'f':  sb.append('\f'); break;
          case 'n':  sb.append('\n'); break;
          case 'r':  sb.append('\r'); break;
          case 't':  sb.append('\t'); break;
          case '\\':  sb.append('\\'); break;
          case '\'':  sb.append('\''); break;
          case '"':  sb.append('"'); break;
          default:  throw new IllegalArgumentException(String.format("Bad escape at offset %d -> %s", new Object[] { Integer.valueOf(i), s }));
          }
          
        }
      }
      else if (ch == '\\') {
        escaping = true;
      } else {
        sb.append(ch);
      }
    }
    return sb.toString();
  }
  
  public static String reEscape(String s)
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      char ch = s.charAt(i);
      switch (ch) {
      case '\b':  sb.append("\\b"); break;
      case '\f':  sb.append("\\f"); break;
      case '\n':  sb.append("\\n"); break;
      case '\r':  sb.append("\\r"); break;
      case '\t':  sb.append("\\t"); break;
      case '\\':  sb.append("\\\\"); break;
      default:  sb.append(ch);
      }
    }
    return sb.toString();
  }
  
  public static void dumpValue(Object v, StringBuilder sb, boolean structured, int level) {
    if ((v instanceof List)) {
      List<Object> lst = (List)v;
      for (int i = 0; i < level; i++) sb.append("|  ");
      if (lst.isEmpty()) {
        sb.append("List()");
      } else {
        sb.append(structured ? "List(\n" : "List(");
        boolean first = true;
        for (Object e : lst) {
          if (first) {
            first = false;
          } else {
            sb.append(structured ? ",\n" : ", ");
          }
          dumpValue(e, sb, structured, structured ? level + 1 : level);
        }
        if (structured) sb.append("\n");
        for (int i = 0; i < level; i++) sb.append("|  ");
        sb.append(")");
      }
    } else if ((v instanceof Object[])) {
      Object[] arr = (Object[])v;
      for (int i = 0; i < level; i++) sb.append("|  ");
      if (arr.length == 0) {
        sb.append("Array()");
      } else {
        sb.append(structured ? "Array(\n" : "Array(");
        boolean first = true;
        for (Object e : arr) {
          if (first) {
            first = false;
          } else {
            sb.append(structured ? ",\n" : ", ");
          }
          dumpValue(e, sb, structured, structured ? level + 1 : level);
        }
        if (structured) sb.append("\n");
        for (int i = 0; i < level; i++) sb.append("|  ");
        sb.append(")");
      }
    } else {
      for (int i = 0; i < level; i++) sb.append("|  ");
      sb.append(v == null ? "null" : v.toString());
    }
  }
  
  public static String dumpValue(Object v, boolean structured) {
    StringBuilder sb = new StringBuilder();
    dumpValue(v, sb, structured, 0);
    return sb.toString();
  }
}

