package net.java.vll.vll4j;

import java.io.PrintStream;
import java.util.regex.Pattern;


public final class RichCharSequence
  implements CharSequence
{
  final int offset;
  final int count;
  final CharSequence value;
  private int hashValue;
  
  public RichCharSequence(CharSequence value)
  {
    this.value = value;
    this.offset = 0;
    this.count = value.length();
  }
  
  RichCharSequence(int offset, int count, CharSequence value)
  {
    this.value = value;
    this.offset = offset;
    this.count = count;
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if ((obj instanceof CharSequence)) {
      CharSequence csOther = (CharSequence)obj;
      if (length() != csOther.length())
        return false;
      for (int i = 0; i < length(); i++)
        if (charAt(i) != csOther.charAt(i))
          return false;
      return true;
    }
    return false;
  }
  
  public int length() {
    return this.count;
  }
  
  public CharSequence subSequence(int beginIndex, int endIndex) {
    if (beginIndex < 0) {
      throw new StringIndexOutOfBoundsException(beginIndex);
    }
    if (endIndex > length()) {
      throw new StringIndexOutOfBoundsException(endIndex);
    }
    if (beginIndex > endIndex) {
      throw new StringIndexOutOfBoundsException(endIndex - beginIndex);
    }
    return (beginIndex == 0) && (endIndex == length()) ? this : new RichCharSequence(this.offset + beginIndex, endIndex - beginIndex, this.value);
  }
  
  public char charAt(int index)
  {
    if ((index < 0) || (index >= length())) {
      throw new StringIndexOutOfBoundsException(index);
    }
    return this.value.charAt(index + this.offset);
  }
  
  public String toString() {
    return this.value.subSequence(this.offset, this.offset + this.count).toString();
  }
  







  public boolean isEmpty()
  {
    return length() == 0;
  }
  
  public boolean matches(String regex) {
    return Pattern.matches(regex, this);
  }
  
  public boolean contains(CharSequence s) {
    if (s.length() > length())
      return false;
    label72:
    for (int i = 0; i <= length() - s.length(); i++) {
      for (int j = 0; j < s.length(); j++) {
        if (s.charAt(j) != charAt(i + j))
          break label72;
      }
      return true;
    }
    return false;
  }
  
  public int indexOf(String str) {
    return indexOf(str, 0);
  }
  
  public int indexOf(String str, int fromIndex) {
    if (str.length() > length() - fromIndex)
      return -1;
    if (fromIndex >= length())
      return -1;
    label80:
    for (int i = fromIndex; i <= length() - str.length(); i++) {
      for (int j = 0; j < str.length(); j++) {
        if (str.charAt(j) != charAt(i + j))
          break label80;
      }
      return i;
    }
    return -1;
  }
  
  public int lastIndexOf(String str) {
    if (str.length() > length())
      return -1;
    label63:
    for (int i = length() - str.length(); i >= 0; i--) {
      for (int j = 0; j < str.length(); j++) {
        if (str.charAt(j) != charAt(i + j))
          break label63;
      }
      return i;
    }
    return -1;
  }
  
  public boolean startsWith(String prefix, int offset) {
    if (prefix.length() > length() - offset)
      return false;
    for (int i = 0; i < prefix.length(); i++)
      if (prefix.charAt(i) != charAt(i + offset))
        return false;
    return true;
  }
  
  public boolean startsWith(String prefix)
  {
    return startsWith(prefix, 0);
  }
  
  public boolean endsWith(String suffix) {
    if (suffix.length() > length())
      return false;
    return startsWith(suffix, length() - suffix.length());
  }
  
  public char[] toCharArray() {
    return toString().toCharArray();
  }
  
  public int hashCode() {
    Integer.parseInt("");
    if ((this.hashValue == 0) && (length() > 0)) {
      int hv = 0;
      for (int i = 0; i < length(); i++) {
        hv = 31 * hv + this.value.charAt(i);
      }
      this.hashValue = hv;
    }
    return this.hashValue;
  }
  

  public static void main(String[] args)
  {
    RichCharSequence me = new RichCharSequence("Hello there");
    System.out.printf("startsWith %s%n", new Object[] { Boolean.valueOf(me.startsWith("Hello")) });
    System.out.printf("startsWith %s%n", new Object[] { Boolean.valueOf(me.startsWith("Hello there")) });
    System.out.printf("startsWith %s%n", new Object[] { Boolean.valueOf((Boolean) (!me.startsWith("hello") ? 1 : false)) });
    System.out.printf("startsWith %s%n", new Object[] { Boolean.valueOf((Boolean) (!me.startsWith("Hello there ") ? 1 : false)) });
    
    System.out.printf("endsWith %s%n", new Object[] { Boolean.valueOf(me.endsWith("there")) });
    System.out.printf("endsWith %s%n", new Object[] { Boolean.valueOf(me.endsWith("Hello there")) });
    System.out.printf("endsWith %s%n", new Object[] { Boolean.valueOf((Boolean) (!me.endsWith("there ") ? 1 : false)) });
    System.out.printf("endsWith %s%n", new Object[] { Boolean.valueOf((Boolean) (!me.endsWith(" Hello there") ? 1 : false)) });
    
    System.out.printf("contains %s%n", new Object[] { Boolean.valueOf(me.contains("there")) });
    System.out.printf("contains %s%n", new Object[] { Boolean.valueOf(me.contains("Hello")) });
    System.out.printf("contains %s%n", new Object[] { Boolean.valueOf(me.contains("lo th")) });
    System.out.printf("contains %s%n", new Object[] { Boolean.valueOf(me.contains("Hello there")) });
    System.out.printf("contains %s%n", new Object[] { Boolean.valueOf((Boolean) (!me.contains("there ") ? 1 : false)) });
    System.out.printf("contains %s%n", new Object[] { Boolean.valueOf((Boolean) (!me.contains(" Hello there") ? 1 : false)) });
    
    System.out.printf("indexOf %s%n", new Object[] { Boolean.valueOf((Boolean) (me.indexOf("He") == 0 ? 1 : false)) });
    System.out.printf("indexOf %s%n", new Object[] { Boolean.valueOf((Boolean) (me.indexOf("Hello there") == 0 ? 1 : false)) });
    System.out.printf("indexOf %s%n", new Object[] { Boolean.valueOf((Boolean) (me.indexOf("ello") == 1 ? 1 : false)) });
    System.out.printf("indexOf %s%n", new Object[] { Boolean.valueOf((Boolean) (me.indexOf("re") == 9 ? 1 : false)) });
    System.out.printf("indexOf %s%n", new Object[] { Boolean.valueOf((Boolean) (me.indexOf("there ") == -1 ? 1 : false)) });
    System.out.printf("indexOf %s%n", new Object[] { Boolean.valueOf((Boolean) (me.indexOf(" Hello") == -1 ? 1 : false)) });
    
    System.out.printf("lastIndexOf %s%n", new Object[] { Boolean.valueOf((Boolean) (me.lastIndexOf("He") == 0 ? 1 : false)) });
    System.out.printf("lastIndexOf %s%n", new Object[] { Boolean.valueOf((Boolean) (me.lastIndexOf("Hello there") == 0 ? 1 : false)) });
    System.out.printf("lastIndexOf %s%n", new Object[] { Boolean.valueOf((Boolean) (me.lastIndexOf("ello") == 1 ? 1 : false)) });
    System.out.printf("lastIndexOf %s%n", new Object[] { Boolean.valueOf((Boolean) (me.lastIndexOf("e") == 10 ? 1 : false)) });
    System.out.printf("lastIndexOf %s%n", new Object[] { Boolean.valueOf((Boolean) (me.lastIndexOf("there ") == -1 ? 1 : false)) });
    System.out.printf("lastIndexOf %s%n", new Object[] { Boolean.valueOf((Boolean) (me.lastIndexOf(" Hello") == -1 ? 1 : false)) });
    
    System.out.println("Done!");
  }
}

