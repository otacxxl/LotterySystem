package com.lotto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/*
    Scores class is used to calculate scores based on generated tickets.
    If ticket has all five matches it is a JACKPOT.
*/
public class Scores {

    //generated ticket passed by constructor
    private ArrayList<Integer> ticket;
    //tickets read from files and stored as arraylist of arraylists [23,12,2,13,14], [36,15,25,13,8] .......
    private ArrayList<ArrayList<Integer>> generatedTickets = new ArrayList<ArrayList<Integer>>();
    //list of files on filesystem that represent a ticket
    private List<String> listOfFilesToCheck;
    //instance of file manager passed by constructor
    private final FileManager fileManagerInstance;
    //use this Map to track winnings
    private Map<Winnings, Integer> calculatedWinnings = new HashMap<Winnings, Integer>();


    //Scores constructor
    //@params instance of file manager and ticked generated when we type "pocni izvlacenje"
    public Scores(FileManager fileManagerInstance, ArrayList<Integer> ticket) throws IOException {
        this.ticket = ticket;
        this.fileManagerInstance = fileManagerInstance;

        //initialize calculated winnings to all zeros
        calculatedWinnings.put(Winnings.PETICA, 0);
        calculatedWinnings.put(Winnings.CETVORKA, 0);
        calculatedWinnings.put(Winnings.TROJKA, 0);

        //we first detect all files on file system that contains lottery tickets
        getAllTicketFilesFromFileSystem();
        //then we read all lottery numbers from file and store them to generatedTickets
        readAllTicketNumbersFromFiles();
    }

    //Function used to gather file names of all the files in current directory that contain lottery tickets
    //List of files are stored in listOfFilesToCheck
    private void getAllTicketFilesFromFileSystem() throws IOException {
        try {
            listOfFilesToCheck = fileManagerInstance.getListOfTicketFiles();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    //Functon used to read all files and store tickets in list od lists generatedTickets variable
    private void readAllTicketNumbersFromFiles() {
        listOfFilesToCheck.forEach(file -> {
            try {
                ArrayList<Integer> numbersRead = fileManagerInstance.readTicketNumbersFromFile(file);
                generatedTickets.add(numbersRead);
            } catch (IOException exception) {
                exception.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    //Here we go throughout all lottery tickets read from files and compare them with lottery ticket
    //generated when we entered "pocni izvlacenje"
    public void checkScores(){
        //AtomicInteger because modification is done in lambda function
        AtomicInteger matches = new AtomicInteger();
        //run through all tickets
        generatedTickets.forEach(ticketFromFileSystem -> {
            matches.set(0);
            //see if the user entry exists in the lottery, if so, we ave a match
            for (int i = 0; i < ticketFromFileSystem.size(); ++i) {
                    if (numberInArray(ticketFromFileSystem.get(i), ticket)) {
                        matches.incrementAndGet();
                    }
            }
            if(matches.get() == 5) {
                increment(calculatedWinnings, Winnings.PETICA);
                //System.out.println("Tiket " + ticketFromFileSystem.toString() + " je JACKPOT! Pogodjeni svi brojevi (petica)!");
            }else if(matches.get() == 4){
                increment(calculatedWinnings, Winnings.CETVORKA);
                //System.out.println("Tiket " + ticketFromFileSystem.toString() + " je dobitak druge vrste. Pogodjena 4 izvucena broja!");
            }else if(matches.get() == 3){
                increment(calculatedWinnings, Winnings.TROJKA);
                //System.out.println("Tiket " + ticketFromFileSystem.toString() + " je trece vrste. Pogodjena 3 izvucena broja!");
            }
        });
    }

    //Function used to print scores
    void printScores(){
        calculatedWinnings.forEach((key, value) -> System.out.println(key + ":" + value));
    }

    //This function is used to increment winning scores based on lottery ticket match
    public static<K> void increment(Map<K, Integer> map, K key)
    {
        //map.putIfAbsent(key, 0);
        map.put(key, map.get(key) + 1);
    }

    //This is helper function used to compare how many numbers are the same in two lottery tickets
    private boolean numberInArray(int queryNum, ArrayList<Integer> pastSelections)
    {
        // look at each element and see if already there
        for (int i = 0; i < pastSelections.size(); ++i) {
            if (pastSelections.get(i) == queryNum) {
                return true;
            }
        }
        return false;
    }
}
