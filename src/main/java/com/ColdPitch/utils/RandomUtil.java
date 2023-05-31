package com.ColdPitch.utils;

import java.util.Random;

public class RandomUtil {
    /**
     * 랜덤 Long을 생성하는 메소드
     *
     * @param end inclusive
     * @return 1부터 end 이하까지의 범위
     */
    public static long getRandom(int end) {
        long res = (long) new Random().nextInt(end);

        return res == 0 ? 1L : res;
    }

    public static double getRandomPercentage() {
        return 1 - Math.random();
    }
}
