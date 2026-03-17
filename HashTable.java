import java.util.*;
import java.io.*;
import java.util.function.Consumer;

public class HashTable {
    
    Entry[] arr;

    public HashTable(){
        arr = new Entry[100]; 
    }

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

    private class HashIterator implements Iterator<String> {
        int index;

        public HashIterator(){
            index = 0;
        }

        public boolean hasNext(){
            while(index < arr.length && arr[index] == null){
                index++;
            }
            return index < arr.length;
        }

        public String next(){
            if(!hasNext()){
                return null;
            }
            return arr[index++].key;
        }

        public void forEachRemaining(Consumer<? super String> action){
            throw new UnsupportedOperationException();
        }

        public void remove(){
            throw new UnsupportedOperationException();
        }

    }
}

