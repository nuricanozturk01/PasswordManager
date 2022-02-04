package com.example.passwordmanager.Email;

import java.security.SecureRandom;

public class GenerateKey
{
    private static final int DEFAULT_SIZE = 8;

    private static final int UPPER_START       = 0x41;
    private static final int UPPER_STOP        = 0x5A;
    private static final int LOWER_START       = 0x61;
    private static final int LOWER_STOP        = 0x7A;
    private static final int NUMBER_START      = 0x30;
    private static final int NUMBER_STOP       = 0x39;

    private static final int[][] wordNumberList = new int[][]
            { {LOWER_START, LOWER_STOP}, {UPPER_START, UPPER_STOP}, {NUMBER_START, NUMBER_STOP} };

    private static SecureRandom random;
    public static String getKey()
    {
        String key = "";
        random = new SecureRandom();
        for (int i = 0; i < DEFAULT_SIZE; i++)
        {
            int k = random.nextInt(3- 0) + 0;
            key += Character.toString(random.nextInt(wordNumberList[k][1] - wordNumberList[k][0]) + wordNumberList[k][0]);
        }
        return key;
    }


}
