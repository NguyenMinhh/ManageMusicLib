package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import modal.Song;

public class FileOperation {

    private static final String FILENAME = "listSong.txt";
    private static Path p = Paths.get(FILENAME);

    // read content file
    public static List<Song> readFile() {
        List<Song> result = new ArrayList<Song>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILENAME));
            String line = reader.readLine();
            line = reader.readLine();
            while (line != null) {
                String[] arr = line.split("\t");

                Song s = new Song(arr[0], arr[1], arr[2]);
                System.out.println(arr[2]);
                result.add(s);
                line = reader.readLine();
            }
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return result;
    }

    public static void addFile(String nameOfSong) {

        int i = readFile().size();
        System.out.println("size la : " + i);
        int id = Integer.parseInt(readFile().get(i - 1).getId());
        System.out.println("Last id là : " + id);
        String newId = System.lineSeparator() + String.valueOf(id + 1);
        
        String song = nameOfSong.replace(".mp3", "");
        
        // append = true
        try (BufferedWriter writer = Files.newBufferedWriter(p, StandardOpenOption.APPEND)) {

            writer.write(newId + "\t");
            writer.write(song + "\t");
            writer.write(nameOfSong);
        } catch (IOException ioe) {
            System.err.format("IOException: %s%n", ioe);
        }

    }

    public static void main(String[] args) {

        System.out.println(readFile().get(0).getId());
    }

}
