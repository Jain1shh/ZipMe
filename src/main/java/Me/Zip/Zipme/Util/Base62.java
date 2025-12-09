package Me.Zip.Zipme.Util;

public class Base62 {

    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String encode(long num) {
        if (num == 0) {
            return "a"; // in case
        }
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            int index = (int) (num % 62);
            sb.append(CHARS.charAt(index));
            num = num / 62;
        }
        return sb.reverse().toString();
    }
    
}
