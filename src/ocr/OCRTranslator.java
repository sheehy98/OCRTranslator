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
			ArrayList<OCRColumnType> columns = tokenize(top, middle, bottom, index);
			OCRToken nextToken = checkToken(columns);

			digits = digits.concat(nextToken.digit);
			index = index + nextToken.width;
		}

		return digits;
	}

	private ArrayList<OCRColumnType> tokenize(String top, String middle, String bottom, int index)
	{
		OCRColumnConverter converter = new OCRColumnConverter();
		ArrayList<OCRColumnType> columns = new ArrayList<OCRColumnType>();

		columns.add(converter.convert(top, middle, bottom, index));
		while (columns.get(columns.size() - 1) != OCRColumnType.spaces) {
			index++;
			columns.add(converter.convert(top, middle, bottom, index));
		}

		return columns;
	}

	private OCRToken checkToken(ArrayList<OCRColumnType> columns)
	{
		switch (columns.get(0)) {
			case spaces: 
				return new OCRToken("", 1);

			case midYbotY:
				if (columns.get(1) == OCRColumnType.spaces) {
					return new OCRToken("1", 2);
				}
				else if (
					columns.get(1) == OCRColumnType.topXbotX &&
					columns.get(2) == OCRColumnType.midYbotY &&
					columns.get(3) == OCRColumnType.spaces
				) {
					return new OCRToken("0", 4);
				}
				else if (
					columns.get(1) == OCRColumnType.topXmidXbotX &&
					columns.get(2) == OCRColumnType.botY &&
					columns.get(3) == OCRColumnType.spaces
				) {
					return new OCRToken("6", 4);
				}
				else if (
					columns.get(1) == OCRColumnType.topXmidXbotX &&
					columns.get(2) == OCRColumnType.midYbotY &&
					columns.get(3) == OCRColumnType.spaces
				) {
					return new OCRToken("8", 4);
				}
				break;

			case topXmidXbotX:
				if (
					columns.get(1) == OCRColumnType.midYbotY &&
					columns.get(2) == OCRColumnType.spaces
				) {
					return new OCRToken("3", 3);
				}
				break;

			case botY: 
				if (
					columns.get(1) == OCRColumnType.topXmidXbotX &&
					columns.get(2) == OCRColumnType.midY &&
					columns.get(3) == OCRColumnType.spaces
				) {
					return new OCRToken("2", 4);
				}
				break;

			case midY: 
				if (
					columns.get(1) == OCRColumnType.midX &&
					columns.get(2) == OCRColumnType.midYbotY &&
					columns.get(3) == OCRColumnType.spaces
				) {
					return new OCRToken("4", 4);
				}
				else if (
					columns.get(1) == OCRColumnType.topXmidXbotX &&
					columns.get(2) == OCRColumnType.botY &&
					columns.get(3) == OCRColumnType.spaces
				) {
					return new OCRToken("5", 4);
				}
				else if (
					columns.get(1) == OCRColumnType.topXmidX &&
					columns.get(2) == OCRColumnType.midYbotY &&
					columns.get(3) == OCRColumnType.spaces
				) {
					return new OCRToken("9", 4);
				}
				break;

			case topX: 
				if (
					columns.get(1) == OCRColumnType.midYbotY &&
					columns.get(2) == OCRColumnType.spaces
				) {
					return new OCRToken("7", 3);
				}
				break;

			default:
				break;
		}

		throw new OCRException("invalid OCR digits");
	}
}
