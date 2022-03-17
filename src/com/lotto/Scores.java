package com.lotto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Scores {

    //generated ticket
    private ArrayList<Integer> ticket;
    //tickets read from file
    private ArrayList<ArrayList<Integer>> generatedTickets = new ArrayList<ArrayList<Integer>>();
    //list of files on filesystem that represent a ticket
    private List<String> listOfFilesToCheck;
    private FileManager fileManagerInstance;

    public Scores(FileManager fileManagerInstance, ArrayList<Integer> ticket) throws IOException {
        this.ticket = ticket;
        this.fileManagerInstance = fileManagerInstance;
        getAllTicketFilesFromFileSystem();
        readAllTicketNumbersFromFiles();
    }

    private void getAllTicketFilesFromFileSystem() throws IOException {
        try {
            listOfFilesToCheck = fileManagerInstance.getListOfTicketFiles();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

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

    public void checkScores(){
        //AtomicInteger because modification is done in lambda function
        AtomicInteger matches = new AtomicInteger();
        //run through all tickets
        generatedTickets.forEach(ticketFromFileSystem -> {
            //see if the user entry exists in the lottery, if so, we have a match
            for (int i = 0; i < ticketFromFileSystem.size(); ++i) {
                matches.set(0);
                    if (numberInArray(ticketFromFileSystem.get(i), ticket)) {
                        matches.incrementAndGet();
                    }
            }
            if(matches.get() == 5) {
                System.out.println("Tiket " + ticketFromFileSystem.toString() + " je JACKPOT! Pogodjeni svi brojevi (petica)!");
            }else if(matches.get() == 4){
                System.out.println("Tiket " + ticketFromFileSystem.toString() + " je dobitak druge vrste. Pogodjena 4 izvucena broja!");
            }else if(matches.get() == 3){
                System.out.println("Tiket " + ticketFromFileSystem.toString() + " je trece vrste. Pogodjena 3 izvucena broja!");
            }else{
                System.out.println("Tiket " + ticketFromFileSystem.toString() + " ima " + matches + " pogodaka.");
            }
        });
    }

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
