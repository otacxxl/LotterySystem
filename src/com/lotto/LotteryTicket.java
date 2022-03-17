package com.lotto;

import java.util.ArrayList;
import java.util.Random;

public class LotteryTicket {
    private ArrayList<Integer> numbers;
    private Random random = new Random();
    private final int MAX_LOTTERY_NUMBER = 39;
    private final int MIN_LOTTERY_NUMBER = 1;
    private final int lotteryLength = 5;

    public LotteryTicket() {
        // Draw numbers as LotteryNumbers is created
        this.drawNumbers();
    }

    public ArrayList<Integer> numbers() {
        return this.numbers;
    }

    public void drawNumbers() {
        // We'll format a list for the numbers
        this.numbers = new ArrayList<>();
        // Write the number drawing here using the method containsNumber()
        int i = 0;
        int lottery = 0;
        while (i < lotteryLength) {
            lottery = random.nextInt(MAX_LOTTERY_NUMBER - MIN_LOTTERY_NUMBER + 1) + MIN_LOTTERY_NUMBER;
            if (!this.numbers.contains(lottery)) {
                this.numbers.add(lottery);
                i++;
            }
        }
    }

    public boolean containsNumber(int number) {
        // Test here if the number is already in the drawn numbers
        return this.numbers.contains(number);

    }

    @Override
    public String toString() {
        return "LotteryTicket{" +
               "numbers=" + numbers +
               '}';
    }
}
