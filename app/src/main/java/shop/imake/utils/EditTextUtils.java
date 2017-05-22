package shop.imake.utils;

/**
 *
 * @author Alice
 *Creare 2016/9/21 11:57
 *
 *
 */
public class EditTextUtils {
    /**
     * 判断是否是表情字符
     * @param codePoint
     * @return
     */
    public static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) ||
                (codePoint == 0xA) || (codePoint == 0xD) ||
                ((codePoint >= 0x20) && codePoint <= 0xD7FF))||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

}
