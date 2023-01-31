package ocr;

/**
 * This enum captures each type of column that appears in OCR digits.
 * X refers to '_' (x-axis), Y refers to '|' (y-axis)
 */
public enum OCRColumnType {
    spaces,
    topX,
    topXmidX,
    topXmidXbotX,
    topXbotX,
    midX,
    midY,
    midYbotY,
    botY,
    invalid
}
