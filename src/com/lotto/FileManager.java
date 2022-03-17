package com.lotto;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
    This class is used to manage communication with filesystem. Using this class we are able to store
    lottery numbers to binary file. We can also read and delete binary files.
*/
public class FileManager {
    //predefined file name for lottery tickets
    private final static String FILE_NAME = "Ticket_";
    //predefined extension of lottery ticket files
    private final static String FILE_EXT = ".dat";
    //used to track number of lottery ticket files
    private int ticketNumber = 1;

    //Constructor for a class.
    //Delete files on startup if for some reason they exist
    public FileManager() {
        deleteFiles();
    }

    //Gets absolute path
    private String getAbsolutPath(){
        return Path.of("").toAbsolutePath().toString();
    }

    //Saving ticket numbers to a file
    //@param numbers represent one lottery ticket [5, 33, 14, 21, 36]
    //@response true if file save operation is successfully, false if not.
    public boolean saveToFile(ArrayList<Integer> numbers) throws IOException {
        String fileName = getAbsolutPath() + "\\" + FILE_NAME + ticketNumber + FILE_EXT;

        //try with resources concept
        try(FileOutputStream fos = new FileOutputStream(fileName)){
            try(ObjectOutputStream oos = new ObjectOutputStream(fos)){
                oos.writeObject(numbers);
            }
        }catch (IOException e){
            System.out.println(e);
            return false;
        }

        //On successful file store increment ticket number for the next ticket filename
        ticketNumber++;
        return true;
    }

    //Reading ticket numbers from a single file
    //@param filename
    //@response ArrayList of integers or a single lottery ticket [5, 33, 14, 21, 36]
    public ArrayList<Integer> readTicketNumbersFromFile(String fileName) throws IOException, ClassNotFoundException {
        ArrayList<Integer> read = new ArrayList<>();
        try(FileInputStream fis = new FileInputStream(fileName)){
            try(ObjectInputStream ois = new ObjectInputStream(fis)){
                read = (ArrayList<Integer>)ois.readObject();
            }
        }catch (IOException e){
            System.out.println(e);
        }
        return read;
    }

    //Create a list of all ticket files with extension ".dat"
    //@response List of Strings that represents file names of lottery ticket files
    public List<String> getListOfTicketFiles() throws IOException {
        List<String> result = null;
        try (Stream<Path> walk = Files.walk(Paths.get(getAbsolutPath()))) {
                     result = walk
                    .filter(p -> !Files.isDirectory(p))   // not a directory
                    .map(p -> p.toString().toLowerCase()) // convert path to string
                    .filter(f -> f.endsWith("dat"))       // check end with
                    .collect(Collectors.toList());        // collect all matched to a List
        }catch (IOException e){
            System.out.println(e);
        }

        return result;
    }

    //Used to delete file from file system
    public void deleteFiles(){
        // Creating filter with given extension
        GenericExtFilter filter = new GenericExtFilter(FILE_EXT);
        // Now, creating an object of FIle  class
        File dir = new File(getAbsolutPath());

        //list out all the file name with .dat extension
        String[] list = dir.list(filter);

        // if list is empty return from function, no files found
        if (list.length == 0) return;

        File fileDelete;

        // Now going throughout file list and delete specific files
        for (String file : list){
            String temp = new StringBuffer(getAbsolutPath())
                    .append(File.separator)
                    .append(file).toString();
            fileDelete = new File(temp);
            boolean isdeleted = fileDelete.delete();
            System.out.println("file : " + temp + " is deleted : " + isdeleted);
        }
    }

    //Inner class, generic extension filter
    public class GenericExtFilter implements FilenameFilter {

        private String ext;

        public GenericExtFilter(String ext) {
            this.ext = ext;
        }

        public boolean accept(File dir, String name) {
            return (name.endsWith(ext));
        }
    }
}
