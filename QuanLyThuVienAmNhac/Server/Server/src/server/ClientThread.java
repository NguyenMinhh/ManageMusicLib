/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import modal.Song;
import static server.FileOperation.addFile;
import static server.FileOperation.readFile;

/**
 *
 * @author lannguyen
 */
public class ClientThread extends Thread {

    private Socket socket = null;
    private OutputStream os = null;
    private ArrayList<Song> listSong = null;
    private ArrayList<String> lstReq = null;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    public void procceingRequest() {
        try {
            while (true) {
                sendListSongToClient();
                lstReq = new ArrayList<>();
                lstReq = getArrayFromClient();
                String idSong = lstReq.get(0);
                String idReq = lstReq.get(1);

                System.out.println("----");
                System.out.println(idSong);
                Song songSelection = findSongById(idSong);
                //System.out.println(songSelection.getName());
                System.out.println("--> " + idReq);
                int id = Integer.parseInt(idReq);
               
                switch (id) {
                    case 1: // play nhạc
                        if (songSelection != null) {
                            System.out.println("play nhạc");
                            sendFileToClient(new File(songSelection.getNameFile()));
                        }
                        break;
                    case 2: // download nhac
                        if (songSelection != null) {
                            System.out.println("download nhac");
                            sendFileToClient(new File(songSelection.getNameFile()));
                        }
                        break;
                    case 3: // upload nhac
                        receiveFile(new File(idSong));
                        addFile(idSong);
                        break;
                    case 4:
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getStringFromClient() throws IOException, ClassNotFoundException {
        String a = null;
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ArrayList<String> nameOfSong = (ArrayList<String>) in.readObject();
        a = nameOfSong.get(0);
        for (int i = 0; i < nameOfSong.size(); i++) {
            System.out.println(nameOfSong.get(i));
        }
        return a;
    }

    public ArrayList<String> getArrayFromClient() throws IOException, ClassNotFoundException {
        
        ArrayList a = new ArrayList<>();
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ArrayList<String> nameOfSong = (ArrayList<String>) in.readObject();
        a.add(nameOfSong.get(0));
        a.add(nameOfSong.get(1));
        
        for (int i = 0; i < nameOfSong.size(); i++) {
            System.out.println(nameOfSong.get(i));
        }
        return a;
    }

    public void sendListSongToClient() throws IOException {
        this.listSong = createListSong();
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        if (this.listSong != null && this.listSong.size() > 0) {
            out.writeObject(this.listSong);
        }
    }

    public ArrayList<Song> createListSong() {
        ArrayList<Song> data = new ArrayList<>();
        //Song dieuAnhBiet = new Song("1", "Dieu Anh Biet", "DieuAnhBiet.mp3");
        //Song Track1 = new Song("2", "Track1", "Track1.mp3");
        int size = readFile().size();
        for(int i = 0; i < size; i++){
            data.add(readFile().get(i));
        }
        
        //data.add(dieuAnhBiet);
        //data.add(Track1);
        return data;
    }

    public Song findSongById(String idSong) {
        for (Song song : this.listSong) {
            if (song.equalsSong(idSong)) {
                return song;
            }
        }
        return null;
    }

    public void sendFileToClient(File file) throws Exception {

        FileInputStream in = new FileInputStream(file);
        BufferedInputStream bufferIn = new BufferedInputStream(in);

        //OutputStream os = socket.getOutputStream(); // client
        os = socket.getOutputStream();

        byte[] contents;
        int fileLength = (int) file.length();

        contents = new byte[fileLength];
        int i = 0;
        while ((i = in.read(contents, 0, fileLength)) != -1) {
            os.write(contents, 0, i);
            os.flush();
        }
        //socket.shutdownOutput();

        System.out.println("Server gui File thành công!");
    }

    public void receiveFile(File file) throws IOException {
        byte[] b = new byte[1024];
        int bytesRead = 0;
        int bytcount = 1024;
        FileOutputStream inFile = new FileOutputStream(file);
        InputStream is = (InputStream) socket.getInputStream();
        BufferedInputStream in2 = new BufferedInputStream(is, 1024);
        while ((bytesRead = in2.read(b, 0, 1024)) != -1) {
            bytcount = bytcount + 1024;
            inFile.write(b, 0, bytesRead);
        }
        System.out.println("Bytes Writen : " + bytcount);

        in2.close();
        inFile.close();
    }

    @Override
    public void run() {
        this.procceingRequest();
    }

    public static void main(String[] args) {
        ClientThread clientThread = new ClientThread(null);
    }
}
