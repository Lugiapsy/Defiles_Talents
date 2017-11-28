package testPackage;

import java.util.Locale;

public class Test {
    public static void main(String... args) {
        String s = String.format(Locale.ENGLISH, "test %f", 4.5f);
        
        System.out.println(s);
    
    }
}