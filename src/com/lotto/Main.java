package com.lotto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;

public class Main {
    public static void main(String[] args) throws IOException {

        FileManager fileManager = new FileManager();
        LotteryTicket lotteryNumbers = new LotteryTicket();
        ArrayList<Integer> numbers;

        // see how many match
        int matches = 0;
        // number of generated tickets
        int numOfTickets = 0;

        System.out.println("**************** LOTO 5/39 *****************");
        System.out.println("Upisivanjem \"quick pick\" u konzolu sistem ce generisati 5 slučajnih brojeva.");
        System.out.println("Upisivanjem \"pocni izvlacenje\" sistem generise 5 slucajnih brojeva, te vrsi provjeru koliko ima dobitaka.");
        System.out.println("Upisivanjem \"kraj\" zavrsavate sa igrom");
        System.out.println("Da bi ste zapoceli igru potrebno je prvo generisati minimalno 3 listica, te nakon toga pocnite sa izvlacenjem.");
        System.out.println("*******************************************");
        System.out.println("Vaš odabir: ");

        Scanner in = new Scanner (System.in);
        boolean nextRound = true;
        do{
            String input = in.nextLine();
            if(input.equals("quick pick")){
                lotteryNumbers.drawNumbers();
                numbers = lotteryNumbers.numbers();
                System.out.println(numbers.toString());
                if(fileManager.saveToFile(numbers)){
                    numOfTickets++;
                }
            }

            if(input.equals("pocni izvlacenje")){
                if(numOfTickets >= 3){
                    //generae new ticket
                    lotteryNumbers.drawNumbers();
                    System.out.println(lotteryNumbers.numbers());
                    //pass ticket to class Scores
                    Scores scores = new Scores(fileManager, lotteryNumbers.numbers());
                    scores.checkScores();
                    scores.printScores();
                }else{
                    System.out.println("Morate imate minimalno 3 generisana listica prije nego pocnete sa izvlacenjem. Trenutno imate " + numOfTickets + " generisanih listica!");
                }
            }

            if(input.equals("kraj")){
                nextRound = false;
            }

        }while(nextRound);

        //delete all files and exit from game
        fileManager.deleteFiles();
        System.exit(1);
    }
}
