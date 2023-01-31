package ocr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class handles the extraction and conversion of OCR tokens.
 * It has two public methods: one to extract the next token, and 
 * one to convert a token to a digit
 */
public class OCRTokenizer {
    private Map<String, OCRColumn> columnMap;
    private Map<List<OCRColumn>, String> digitMap;

    /**
	 * Constructor that buils the String->OCRColumn map and the OCRColumns->digit map
	 */
    public OCRTokenizer() {
		columnMap = new HashMap<String, OCRColumn>();
		columnMap.put("   ", OCRColumn.spaces);
		columnMap.put("_  ", OCRColumn.topX);
		columnMap.put("__ ", OCRColumn.topXmidX);
		columnMap.put("___", OCRColumn.topXmidXbotX);
		columnMap.put("_ _", OCRColumn.topXbotX);
		columnMap.put(" _ ", OCRColumn.midX);
		columnMap.put(" | ", OCRColumn.midY);
		columnMap.put(" ||", OCRColumn.midYbotY);
		columnMap.put("  |", OCRColumn.botY);

		digitMap = new HashMap<List<OCRColumn>, String>();
		digitMap.put(Arrays.asList(), "");
		digitMap.put(Arrays.asList(OCRColumn.midYbotY, OCRColumn.topXbotX, OCRColumn.midYbotY), "0");
		digitMap.put(Arrays.asList(OCRColumn.midYbotY), "1");
		digitMap.put(Arrays.asList(OCRColumn.botY, OCRColumn.topXmidXbotX, OCRColumn.midY), "2");
		digitMap.put(Arrays.asList(OCRColumn.topXmidXbotX, OCRColumn.midYbotY), "3");
		digitMap.put(Arrays.asList(OCRColumn.midY, OCRColumn.midX, OCRColumn.midYbotY), "4");
		digitMap.put(Arrays.asList(OCRColumn.midY, OCRColumn.topXmidXbotX, OCRColumn.botY), "5");
		digitMap.put(Arrays.asList(OCRColumn.midYbotY, OCRColumn.topXmidXbotX, OCRColumn.botY), "6");
		digitMap.put(Arrays.asList(OCRColumn.topX, OCRColumn.midYbotY), "7");
		digitMap.put(Arrays.asList(OCRColumn.midYbotY, OCRColumn.topXmidXbotX, OCRColumn.midYbotY), "8");
		digitMap.put(Arrays.asList(OCRColumn.midY, OCRColumn.topXmidX, OCRColumn.midYbotY), "9");
    }

	/**
	 * Builds a list of OCRColumns to describe each column in the next token.
	 * A token is a set of columns that fall between two columns of spaces
	 * @param top the top row of the OCR input
	 * @param middle the middle row of the OCR input
	 * @param bottom the third row of the OCR input
	 * @param index the column where the token of interest begins
	 * @return an ArrayList of OCRColumns describing the next token
	 */
	public List<OCRColumn> tokenize(String top, String middle, String bottom, int index)
	{
		List<OCRColumn> columns = new ArrayList<OCRColumn>();

		// Keep translating columns until a column of spaces is reached
		// The end of the input is treated as a column of spaces
		columns.add(convertColumn(top, middle, bottom, index));
		while (columns.get(columns.size() - 1) != OCRColumn.spaces) {
			index++;
			columns.add(convertColumn(top, middle, bottom, index));
		}
		columns.remove(columns.size() - 1);

		return columns;
	}
    
    /**
	 * Determine the OCRColumnType which describes a specified column
     * in a set of OCR input strings
	 * @param top the top row of the OCR input
	 * @param middle the middle row of the OCR input
	 * @param bottom the third row of the OCR input
     * @param index the column index to convert to a OCRColumnType
	 * @return the OCRColumnType which describes the specified column
	 */
    private OCRColumn convertColumn(String top, String middle, String bottom, int index) {
        // Treat the end of the OCR input as a column of spaces
        // Each token now ends in spaces rather than either spaces or termination
        if (index == top.length()) {
            return OCRColumn.spaces;
        }
        
        // Concat each character in the column to allow for simpler comparison
        String columnTranspose = transpose(
			top.charAt(index), 
			middle.charAt(index), 
			bottom.charAt(index)
        );

        if (columnMap.containsKey(columnTranspose)) {
			return columnMap.get(columnTranspose);
		}
		return OCRColumn.invalid;
    }

    /**
	 * Combines three characters into a single string
	 * @param first the first character to combine
	 * @param second the second character to combine
	 * @param third the third character to combine
	 * @return a String with each character concatenated
	 */
    private String transpose(char first, char second, char third) {
        StringBuilder transposeResult = new StringBuilder(3);
        
        transposeResult.append(first);
        transposeResult.append(second);
        transposeResult.append(third);

        return transposeResult.toString();
    }

	/**
	 * Determines the arabic digit described by a list of OCRColumns
	 * @param columns an ArrayList of OCRColumns describing a token
	 * @return a String containing the arabic digit
	 * @throws an OCRException if the OCRColumns do not correspond to a valid digit
	 */
	public String checkToken(List<OCRColumn> columns)
	{
		if (digitMap.containsKey(columns)) {
			return digitMap.get(columns);
		}
		throw new OCRException("invalid OCR digits");
	}
}
