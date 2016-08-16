package com.moybl.axiom.frame;

public class Temp {

	private String name;

	public Temp(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}

		return name.equals(((Temp) obj).name);
	}

	private static int counter;

	public static Temp create() {
		counter++;
		return new Temp("T" + counter);
	}

}
