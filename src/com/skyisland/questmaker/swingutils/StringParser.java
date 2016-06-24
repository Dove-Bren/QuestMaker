package com.skyisland.questmaker.swingutils;

public interface StringParser<E> {

	/**
	 * Parses the given text, returning an object from it.
	 * @param text
	 * @return The object the text signifies, or null if it is invalid
	 */
	E parse(String text);
}
