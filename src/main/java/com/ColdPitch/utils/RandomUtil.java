package com.ColdPitch.utils;

import java.util.Random;

public class RandomUtil {
    /**
     * 랜덤 Long을 생성하는 메소드
     *
     * @param end inclusive
     * @return 0부터 end 이하까지의 범위
     */
    public static long getRandom(int end) {
        Random random = new Random();

        return (long) random.nextInt(end);
    }

    public static double getRandomPercentage() {
        return 1 - Math.random();
    }
}
