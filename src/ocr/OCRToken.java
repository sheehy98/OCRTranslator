package ocr;

/**
 * This class represents a translated token.
 * It stores the result of the translation (digit)
 * and the number of characters in each column of the 
 * pre-translated text including a trailing space (width)
 */
public class OCRToken {
    String digit;
    int width;

    public OCRToken(String digit, int width)
	{
		this.digit = digit;
        this.width = width;
	}
}
