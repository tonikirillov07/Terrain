package org.darkness;

public class Test {
    public static void main(String[] args) {


        for (double i = 0; i < 2; i+=0.1) {
            float y  = (float) (-Math.pow(i, 2) + Math.pow(i * 1.6f, 2) + 1);

            System.out.println(y);
        }
    }
}
