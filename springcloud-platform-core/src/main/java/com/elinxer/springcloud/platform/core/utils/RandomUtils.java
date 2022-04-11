package com.elinxer.springcloud.platform.core.utils;

import java.util.Random;

/**
 * 随机数工具，用于产生随机数，随机密码等
 * @author elinx
 */
public class RandomUtils {

    private static final Random RANDOM = new Random();

    public static Random getRandom() {
        return RANDOM;
    }

    private static final char[] CHARS = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u',
            'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z'
    };

    /**
     * 随机生成由0-9a-zA-Z组合而成的字符串
     *
     * @param len 字符串长度
     * @return 生成结果
     */
    public static String randomChar(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(CHARS[RANDOM.nextInt(CHARS.length)]);
        }
        return sb.toString();
    }

    public static String randomChar() {
        return randomChar(8);
    }

    public static void main(String[] args) {
        for (int i = 0; i <= 100; i++) {
            System.out.println("0x0" + Integer.toHexString(i));
        }
    }

}
