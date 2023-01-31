package ocr;

/**
 * This class has a single public method that returns
 * the OCRColumnType which describes a specified column
 * in a set of OCR input strings 
 */
public class OCRColumnConverter {
    /**
	 * Default constructor.
	 */
    public OCRColumnConverter() { }
    
    /**
	 * Determine the OCRColumnType which describes a specified column
     * in a set of OCR input strings
	 * @param top the top row of the OCR input
	 * @param middle the middle row of the OCR input
	 * @param bottom the third row of the OCR input
     * @param index the column index to convert to a OCRColumnType
	 * @return the OCRColumnType which describes the specified column
	 */
    public OCRColumn convert(String top, String middle, String bottom, int index) {
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

        // Translate column to OCRColumnType
        switch(columnTranspose) {
            case "   ":
                return OCRColumn.spaces;
            case "_  ":
                return OCRColumn.topX;
            case "__ ":
                return OCRColumn.topXmidX;
            case "___":
                return OCRColumn.topXmidXbotX;
            case "_ _":
                return OCRColumn.topXbotX;
            case " _ ":
                return OCRColumn.midX;
            case " | ":
                return OCRColumn.midY;
            case " ||":
                return OCRColumn.midYbotY;
            case "  |":
                return OCRColumn.botY;
            default:
                return OCRColumn.invalid;
        }
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
}
