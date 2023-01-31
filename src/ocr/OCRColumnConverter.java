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
    public OCRColumnType convert(String top, String middle, String bottom, int index) {
        // Treat the end of the OCR input as a column of spaces
        // Each token now ends in spaces rather than either spaces or termination
        if (index == top.length()) {
            return OCRColumnType.spaces;
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
                return OCRColumnType.spaces;
            case "_  ":
                return OCRColumnType.topX;
            case "__ ":
                return OCRColumnType.topXmidX;
            case "___":
                return OCRColumnType.topXmidXbotX;
            case "_ _":
                return OCRColumnType.topXbotX;
            case " _ ":
                return OCRColumnType.midX;
            case " | ":
                return OCRColumnType.midY;
            case " ||":
                return OCRColumnType.midYbotY;
            case "  |":
                return OCRColumnType.botY;
            default:
                return OCRColumnType.invalid;
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
