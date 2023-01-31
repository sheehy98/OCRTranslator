/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016-2017 Gary F. Pollice
 *******************************************************************************/
package ocr;

import java.util.ArrayList;

/**
 * This class has a single method that will translate OCR digits to a string of
 * text digits that correspond to the OCR digits.
 * 
 * @version Mar 13, 2019
 */
public class OCRTranslator
{
	/**
	 * Default constructor. You may not add parameters to this. This is
	 * the only constructor for this class and is what the master tests will use.
	 */
	public OCRTranslator()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * Determine if an array of strings contains a null value
	 * @param strings the array of strings to be checked
	 * @return true if a null value is found, false otherwise
	 */
	private boolean hasNull(String strings[])
	{
		for(int i = 0; i < strings.length; i++) {
			if (strings[i] == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determine if an array of strings contains strings with different lengths
	 * @param strings the array of strings to be checked
	 * @return true if strings with different lengths are found, false otherwise
	 */
	private boolean hasInvalidLengths(String strings[])
	{
		for(int i = 0; i < strings.length-1; i++) {
			if (strings[i].length() != strings[i+1].length()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determine if a string contains any characters other than '_', '|', or ' '
	 * @param string the string to be checked
	 * @return true if string contains any invalid characters, false otherwise
	 */
	private boolean hasInvalidCharacters(String string)
	{
		for(int i = 0; i < string.length(); i++) {
			if (
				string.charAt(i) != '_' &&
				string.charAt(i) != '|' &&
				string.charAt(i) != ' '
			) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Translate a string of OCR digits to a corresponding string of text
	 * digits. OCR digits are represented as three rows of characters (|, _, and space).
	 * @param top the top row of the OCR input
	 * @param middle the middle row of the OCR input
	 * @param bottom the third row of the OCR input
	 * @return a String containing the digits corresponding to the OCR input
	 * @throws an OCRException on error as noted in the specification
	 */
	public String translate(String top, String middle, String bottom)
	{
		String[] args = {top, middle, bottom};

		if (hasNull(args)) {
			throw new OCRException("Null string found in input");
		}

		if (hasInvalidLengths(args)) {
			throw new OCRException("Inputs strings must have equal length");
		}

		if (
			hasInvalidCharacters(top) ||
			hasInvalidCharacters(middle) ||
			hasInvalidCharacters(bottom)
		) {
			throw new OCRException("Invalid characters in at least one input string");
		}

		return parse(top, middle, bottom);
	}

	/**
	 * Performs translation of OCR digits to a String of digits.
	 * OCR digits are represented as three (validated) rows of characters (|, _, and space).
	 * @param top the top row of the OCR input
	 * @param middle the middle row of the OCR input
	 * @param bottom the third row of the OCR input
	 * @return a String containing the digits corresponding to the OCR input
	 * @throws an OCRException if the strings do not describe a list of OCR characters
	 */
	private String parse(String top, String middle, String bottom)
	{
		String digits = "";
		
		int index = 0;
		while (index < top.length()) {
			// Get the OCRColumns that describe the next token
			ArrayList<OCRColumn> columns = tokenize(top, middle, bottom, index);
			// Convert the OCRColumns into a digit (and a width to properly traverse the strings)
			digits = digits.concat(checkToken(columns));
			// 
			index = index + columns.size();
		}

		return digits;
	}

	/**
	 * Builds a list of OCRColumns to describe each column in the next token.
	 * A token is a set of columns that fall between two columns of spaces (includes trailing space)
	 * @param converter the OCRColumnConverter that will perform the transformation
	 * @param top the top row of the OCR input
	 * @param middle the middle row of the OCR input
	 * @param bottom the third row of the OCR input
	 * @param index the column where the token of interest begins
	 * @return an ArrayList of OCRColumns describing the next token
	 */
	private ArrayList<OCRColumn> tokenize(String top, String middle, String bottom, int index)
	{
		OCRColumnConverter converter = new OCRColumnConverter();
		ArrayList<OCRColumn> columns = new ArrayList<OCRColumn>();

		// Keep translating columns until a column of spaces is reached
		// The end of the input is treated as a column of spaces
		columns.add(converter.convert(top, middle, bottom, index));
		while (columns.get(columns.size() - 1) != OCRColumn.spaces) {
			index++;
			columns.add(converter.convert(top, middle, bottom, index));
		}

		return columns;
	}

	/**
	 * Determines the arabic digit described by a list of OCRColumns
	 * @param columns an ArrayList of OCRColumns describing a token
	 * @return a String containing the arabic digit
	 * @throws an OCRException if the OCRColumns do not correspond to a valid digit
	 */
	private String checkToken(ArrayList<OCRColumn> columns)
	{
		if (
			columns.get(0) == OCRColumn.spaces
		) { return ""; }

		if (
			columns.get(0) == OCRColumn.midYbotY &&
			columns.get(1) == OCRColumn.topXbotX &&
			columns.get(2) == OCRColumn.midYbotY &&
			columns.get(3) == OCRColumn.spaces
		) { return "0"; }

		if (
			columns.get(0) == OCRColumn.midYbotY &&
			columns.get(1) == OCRColumn.spaces
		) { return "1"; }

		if (
			columns.get(0) == OCRColumn.botY &&
			columns.get(1) == OCRColumn.topXmidXbotX &&
			columns.get(2) == OCRColumn.midY &&
			columns.get(3) == OCRColumn.spaces
		) { return "2"; }
		
		if (
			columns.get(0) == OCRColumn.topXmidXbotX &&
			columns.get(1) == OCRColumn.midYbotY &&
			columns.get(2) == OCRColumn.spaces
		) { return "3"; }

		if (
			columns.get(0) == OCRColumn.midY &&
			columns.get(1) == OCRColumn.midX &&
			columns.get(2) == OCRColumn.midYbotY &&
			columns.get(3) == OCRColumn.spaces
		) { return "4"; }

		if (
			columns.get(0) == OCRColumn.midY &&
			columns.get(1) == OCRColumn.topXmidXbotX &&
			columns.get(2) == OCRColumn.botY &&
			columns.get(3) == OCRColumn.spaces
		) { return "5"; }

		if (
			columns.get(0) == OCRColumn.midYbotY &&
			columns.get(1) == OCRColumn.topXmidXbotX &&
			columns.get(2) == OCRColumn.botY &&
			columns.get(3) == OCRColumn.spaces
		) { return "6"; }

		if (
			columns.get(0) == OCRColumn.topX &&
			columns.get(1) == OCRColumn.midYbotY &&
			columns.get(2) == OCRColumn.spaces
		) { return "7"; }

		if (
			columns.get(0) == OCRColumn.midYbotY &&
			columns.get(1) == OCRColumn.topXmidXbotX &&
			columns.get(2) == OCRColumn.midYbotY &&
			columns.get(3) == OCRColumn.spaces
		) { return "8"; }

		if (
			columns.get(0) == OCRColumn.midY &&
			columns.get(1) == OCRColumn.topXmidX &&
			columns.get(2) == OCRColumn.midYbotY &&
			columns.get(3) == OCRColumn.spaces
		) { return "9"; }

		throw new OCRException("invalid OCR digits");
	}
}
