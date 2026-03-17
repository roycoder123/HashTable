//Author: Roy Rozin   Date: 3/16/2026  Description: Class implementing a hashtable with get, put, and remove functions

import java.util.*;
import java.io.*;
import java.util.function.Consumer;

public class HashTable {
    Entry[] arr;

    //constructor for array
    public HashTable(){
        arr = new Entry[100]; 
    }

    //pre-condition: key is not null
    //post-condition: this function takes a key, and using an already made hash function, stores it in a certain index in the array
    //if one spot is taken, it'll move over using linear probing
    public void put(String key, Object value) {
        int index = Math.abs(key.hashCode()) % arr.length;

        while(arr[index] != null){
            if(arr[index].key.equals(key)){
                arr[index].value = (String)value;
                return;
            }
            index = (index + 1) % arr.length;
        }
        arr[index] = new Entry(key, (String)value);
    }

    //pre-condition: key is not null
    //post-condition: this method will return the value of a key given
    public String get(String key) {
        int index = Math.abs(key.hashCode()) % arr.length;

        while(arr[index] != null){
            if(arr[index].key.equals(key)){
                return arr[index].value;
            }
            index = (index + 1) % arr.length;
        }
        return null;
    }

    //pre-condition: key is not null
    //post-condition: this method will remove an object stored based on the key, and then rehash all of the following elements
    public String remove(String key){
        int index = Math.abs(key.hashCode()) % arr.length;

        while(arr[index] != null){
            if(arr[index].key.equals(key)){
                String remove = arr[index].value;
                arr[index] = null;

                //rehash
                index = (index + 1) % arr.length;
                while(arr[index] != null){
                    Entry rehash = arr[index];
                    arr[index] = null;
                    put(rehash.key, rehash.value);
                    index = (index + 1) % arr.length;
                }
                return remove;
            }
            index = (index + 1) % arr.length;
        }
        return null;
    }

    public Iterator keys() {
        return new HashIterator();
    }

    //print function that loops through and prints the values in the array
    public void print(){
        Iterator<String> iterator = keys();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }

    //Loads this HashTable from a file named "Lookup.dat". 
    public void load() {
        FileReader fileReader;
        BufferedReader bufferedReader = null;
        // Open the file for reading
        try {
            File f = new File(System.getProperty("user.home"), "Lookup.dat");
            fileReader = new FileReader(f);
            bufferedReader = new BufferedReader(fileReader);
        }
        catch (FileNotFoundException e) {
            System.err.println("Cannot find input file \"Lookup.dat\"");
        }
        
        // Read the file contents and save in the HashTable
        try {
            while (true) {
                String key = bufferedReader.readLine();
                if (key == null) return;
                String value = bufferedReader.readLine();
                if (value == null) {
                    System.out.println("Error in input file");
                    System.exit(1);
                }
                String blankLine = bufferedReader.readLine();
                if (!"".equals(blankLine)) {
                    System.out.println("Error in input file");
                    System.exit(1);
                }
                put(key, value);
            }
        }
        catch (IOException e) {
            e.printStackTrace(System.out);
        }
        
        // Close the file when we're done
        try {
            bufferedReader.close( );
        }
        catch(IOException e) {
            e.printStackTrace(System.out);
        }
    }

	//Saves this HashTable onto a file named "Lookup.dat".
	public void save() {
        FileOutputStream stream;
        PrintWriter printWriter = null;
        Iterator iterator;
        
        // Open the file for writing
        try {
            File f = new File(System.getProperty("user.home"), "Lookup.dat");
            stream = new FileOutputStream(f);
            printWriter = new PrintWriter(stream);
        }
        catch (Exception e) {
            System.err.println("Cannot use output file \"Lookup.dat\"");
        }
       
        // Write the contents of this HashTable to the file
        iterator = keys();
        while (iterator.hasNext()) {
            String key = (String)iterator.next();
            printWriter.println(key);
            String value = (String)get(key);
            value = removeNewlines(value);
            printWriter.println(value);
            printWriter.println();
        }
       
        // Close the file when we're done
        printWriter.close( );
    }
    
    //Replaces all line separator characters (which vary from one platform to the next) with spaces.
    // @param value The input string, possibly containing line separators.
    // @return The input string with line separators replaced by spaces.
    private String removeNewlines(String value) {
        return value.replaceAll("\r|\n", " ");
        
    }

    //iterator function
    private class HashIterator implements Iterator<String> {
        int index;

        //constructor
        public HashIterator(){
            index = 0;
        }

        //this method checks if there is another element in the array and returns true/false accordingly
        public boolean hasNext(){
            while(index < arr.length && arr[index] == null){
                index++;
            }
            return index < arr.length;
        }

        //this method returns the next value in the array
        public String next(){
            if(!hasNext()){
                return null;
            }
            return arr[index++].value;
        }

        //throws exception because not needed
        public void forEachRemaining(Consumer<? super String> action){
            throw new UnsupportedOperationException();
        }

        //throws exception because not needed
        public void remove(){
            throw new UnsupportedOperationException();
        }

    }
}

