package com.lotto;

import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.Random;

/*
    Lottery Ticket model class. This class is used to generate ArrayList of 5 random numbers
    that represent single lottery ticket.
*/
public class LotteryTicket {
    private ArrayList<Integer> numbers;
    private Random random = new Random();
    private final int MAX_LOTTERY_NUMBER = 39;
    private final int MIN_LOTTERY_NUMBER = 1;
    private final int lotteryLength = 5;

    //Empty class constructor
    public LotteryTicket() {
    }

    //This function returns ArrayList of numbers that represent single lottery ticket [32, 12, 15, 1, 13]
    //@response lottery ticket
    public ArrayList<Integer> numbers() {
        return this.numbers;
    }

    //This function generates single lottery ticket  [32, 12, 15, 1, 13]
    public void drawNumbers() {
        // We'll format a list for the numbers
        this.numbers = new ArrayList<>();
        // Write the number drawing here using the method containsNumber()
        int i = 0;
        int lottery = 0;
        while (i < lotteryLength) {
            //lottery = random.nextInt(MAX_LOTTERY_NUMBER - MIN_LOTTERY_NUMBER + 1) + MIN_LOTTERY_NUMBER; //before Java 8
            lottery = random.ints(MIN_LOTTERY_NUMBER, (MAX_LOTTERY_NUMBER + 1)).limit(1).findFirst().getAsInt(); //Java 8
            //add lottery number to arraylist if is not already added
            if (!this.numbers.contains(lottery)) {
                this.numbers.add(lottery);
                i++;
            }
        }
    }

    //Override toString function used to print lottery tickets using System.out.println
    @Override
    public String toString() {
        return "LotteryTicket{" +
               "numbers=" + numbers +
               '}';
    }
}
