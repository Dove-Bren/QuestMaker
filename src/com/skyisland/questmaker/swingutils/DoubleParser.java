package com.skyisland.questmaker.swingutils;

public class DoubleParser implements StringParser<Double> {

	private static DoubleParser inst = null;
	
	public static DoubleParser instance() {
		if (inst == null)
			inst = new DoubleParser();
		
		return inst;
	}
	
	@Override
	public Double parse(String text) {
		try {
			return Double.parseDouble(text);
		} catch (Exception e) {
			return null;
		}
	}

	
	
}
