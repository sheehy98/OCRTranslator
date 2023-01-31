package ocr;

public class OCRColumnConverter {
    public OCRColumnConverter() { }

    public OCRColumnType convert(String top, String middle, String bottom, int index) {
        if (index == top.length()) {
            return OCRColumnType.spaces;
        }
        
        String columnTranspose = transpose(
			top.charAt(index), 
			middle.charAt(index), 
			bottom.charAt(index)
        );

        OCRColumnType type;
        switch(columnTranspose) {
            case "   ":
                type = OCRColumnType.spaces;
                break;
            case "_  ":
                type = OCRColumnType.topX;
                break;
            case "__ ":
                type = OCRColumnType.topXmidX;
                break;
            case "___":
                type = OCRColumnType.topXmidXbotX;
                break;
            case "_ _":
                type = OCRColumnType.topXbotX;
                break;
            case " _ ":
                type = OCRColumnType.midX;
                break;
            case " | ":
                type = OCRColumnType.midY;
                break;
            case " ||":
                type = OCRColumnType.midYbotY;
                break;
            case "  |":
                type = OCRColumnType.botY;
                break;
            default:
                type = OCRColumnType.invalid;
        }

        return type;
    }

    private String transpose(char top, char middle, char bottom) {
        StringBuilder transposeResult = new StringBuilder(3);
        
        transposeResult.append(top);
        transposeResult.append(middle);
        transposeResult.append(bottom);

        return transposeResult.toString();
    }
}
