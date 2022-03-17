package com.lotto;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileManager {
    private final static String FILE_NAME = "Ticket_";
    private final static String FILE_EXT = ".dat";
    private int ticketNumber = 1;

    //delete files on startup if for some reason they exist
    public FileManager() {
        deleteFiles();
    }

    //get absoulte path
    private String getApsolutePath(){
        return Path.of("").toAbsolutePath().toString();
    }

    //saving ticket numbers to a file
    public boolean saveToFile(ArrayList<Integer> numbers) throws IOException {
        String fileName = getApsolutePath() + "\\" + FILE_NAME + ticketNumber + FILE_EXT;

        try(FileOutputStream fos = new FileOutputStream(fileName)){
            try(ObjectOutputStream oos = new ObjectOutputStream(fos)){
                oos.writeObject(numbers);
            }
        }catch (IOException e){
            System.out.println(e);
            return false;
        }

        //increment ticket number file name
        ticketNumber++;
        return true;
    }

    //reading ticket numbers from a file
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

    //create a list od all ticket files with extension .dat
    public List<String> getListOfTicketFiles() throws IOException {
        List<String> result = null;
        try (Stream<Path> walk = Files.walk(Paths.get(getApsolutePath()))) {
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

    // used to delete file from file system
    public void deleteFiles(){
        GenericExtFilter filter = new GenericExtFilter(FILE_EXT);
        File dir = new File(getApsolutePath());

        //list out all the file name with .dat extension
        String[] list = dir.list(filter);

        if (list.length == 0) return;

        File fileDelete;

        for (String file : list){
            String temp = new StringBuffer(getApsolutePath())
                    .append(File.separator)
                    .append(file).toString();
            fileDelete = new File(temp);
            boolean isdeleted = fileDelete.delete();
            System.out.println("file : " + temp + " is deleted : " + isdeleted);
        }
    }

    //inner class, generic extension filter
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
