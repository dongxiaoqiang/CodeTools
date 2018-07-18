//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.citydo.tools.service;

public class IdsGenerate {
    public IdsGenerate() {
    }

    public String generate(String code, String date, int sex) {
        StringBuilder generater = new StringBuilder();
        generater.append(code);
        generater.append(date);
        generater.append(this.randomCode(sex));
        generater.append(this.calcTrailingNumber(generater.toString().toCharArray()));
        return generater.toString();
    }

    private String randomCode(int sex) {
        int code;
        for(code = (int)(Math.random() * 1000.0D); (sex != 1 || code % 2 == 0) && (sex != 2 || code % 2 != 0); code = (int)(Math.random() * 1000.0D)) {
            ;
        }

        if (code < 10) {
            return "00" + code;
        } else {
            return code < 100 ? "0" + code : "" + code;
        }
    }

    private char calcTrailingNumber(char[] chars) {
        if (chars.length < 17) {
            return ' ';
        } else {
            int[] c = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
            char[] r = new char[]{'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
            int[] n = new int[17];
            int result = 0;

            int i;
            for(i = 0; i < n.length; ++i) {
                n[i] = Integer.parseInt(String.valueOf(chars[i]));
            }

            for(i = 0; i < n.length; ++i) {
                result += c[i] * n[i];
            }

            return r[result % 11];
        }
    }
}
