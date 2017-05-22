package shop.imake.utils;

/**
 * Created by Alice
 * 2017/5/17
 */
public class DoubleTextUtils {
    /**
     * @param amount
     */
    public static String setDoubleUtils( double amount){
        if (amount != 0) {
            return String.format("%.2f", amount);
        }
        return "0.00";
    }
}
