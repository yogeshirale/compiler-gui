package net.api;

public enum Multiplicity
{
  One {
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
},  ZeroOrOne {
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
},  ZeroOrMore {
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
},  OneOrMore {
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
},  Not {
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
},  Guard {
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}
};
  
  private Multiplicity() {}
  
  public abstract String toString();
}
