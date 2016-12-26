/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import modal.Song;

/**
 *
 * @author lannguyen
 */
public class AudioClient {

    private final Socket socket;
    private Player player = null;
    private Thread ss = null;
    private Thread sDownload = null;
    private ArrayList<String> listSong = null;

    public AudioClient(Socket socket) {
        this.socket = socket;
    }

    public static void main(String[] args) throws Exception {
        // Initialize socket
        Socket socket = new Socket(InetAddress.getByName("localhost"), 5000);
        AudioClient client = new AudioClient(socket);

        System.out.println("Wellcome");
    }

    public void uploadFileToServer(File file) throws IOException, ClassNotFoundException {
        int sizeFile = (int) file.length();
        System.out.println(sizeFile);
        byte[] buf = new byte[sizeFile];
        OutputStream os = socket.getOutputStream();
        BufferedOutputStream outToServer = new BufferedOutputStream(os, sizeFile);
        FileInputStream in = new FileInputStream(file);
        int i = 0;

        while ((i = in.read(buf, 0, sizeFile)) != -1) {
            outToServer.write(buf, 0, i);
            outToServer.flush();
        }
        socket.shutdownOutput();
        System.out.println("Bytes Sent :" + sizeFile);

        outToServer.close();
        in.close();
    }

    public ArrayList<Song> getArrayFromServer() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ArrayList<Song> songs = (ArrayList<Song>) in.readObject();
        for (int i = 0; i < songs.size(); i++) {
            System.out.println(songs.get(i).getNameFile());
        }
        return songs;
    }

    public void downloadFile2(File file) throws Exception {
        System.out.println("xin chào");
        byte[] contents = new byte[1024];
        int bytcount = 1024;

        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        System.out.println("xin chào2");
        InputStream is = socket.getInputStream();
        // BufferedInputStream in = new BufferedInputStream(is);

        // No of bytes read in one read() call
        int bytesRead = 0;
        System.out.println("xin chào3");

        //while ((bytesRead = is.read(contents, 0, contents.length)) != -1) {
        bytcount = bytcount + 1024;
        bos.write(contents, 0, bytesRead);

        //}
        System.out.println(is.read(contents, 0, contents.length));

        bos.flush();
        System.out.println("Download file thành công");
    }

    public void downloadFile(File file) throws Exception {
        byte[] contents = new byte[1024];

        // Initialize the FileOutputStream to the output file's full path.
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        InputStream is = socket.getInputStream();
        BufferedInputStream in = new BufferedInputStream(is);

        // No of bytes read in one read() call
        int bytesRead = 0;

        int inChar;
        int count = 0;
        int i = 0;
        while ( (bytesRead = is.read(contents)) != -1 ) {
            i++;
            bos.write(contents, 0, bytesRead);
            System.out.println(i);
            //System.out.print(bos.toString());
        }

        //is.read();
        bos.flush();
        System.out.println("Download file thành công");
    }

    //public void playMusicOnline() throws Exception {
    public void receiveAndPlayFileFromServer() throws Exception {
        byte[] contents = new byte[16];
        InputStream is = socket.getInputStream();
        BufferedInputStream in = new BufferedInputStream(is);
        int bytesRead = 0;
        int inChar;
        int count = 0;
        try {
            player = new Player(in);
            player.play();
        } catch (JavaLayerException ex) {

        }
//        while ((bytesRead = is.read(contents)) != -1) {
//            ++count;
//        }
        //btnPlay.setEnabled(true);
        System.out.println("Finished");
    }


    public void stopMusic(JButton btnPlay, JButton btnStop) throws IOException {
        btnPlay.setEnabled(true);
        btnStop.setEnabled(false);
        if (player != null) {
            System.out.println("llll");
            ss.interrupt();
        }
        //player.close();
        ss.interrupt();
        if(ss.isInterrupted()){
            System.out.println("ngat r");
        }

        //socket.shutdownOutput();
    }

    public void sendStringToServer(String a,String b) throws IOException {
        this.listSong = new ArrayList<>();
        listSong.add(a);
        listSong.add(b);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        if (this.listSong != null && this.listSong.size() > 0) {
            out.writeObject(this.listSong);
        }
        System.out.println("da gui xong");
    }

    public void playMusicOnline2(JButton btnPlay, JButton btnStop) throws Exception, IOException {
        System.out.println("chay 1");
        btnPlay.setEnabled(false);
        System.out.println("chay 2");
        receiveAndPlayFileFromServer();
        btnStop.setEnabled(true);

    }

    public synchronized void playSound(JButton btnPlay, JButton btnStop) {
        btnPlay.setEnabled(false);
        ss = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    receiveAndPlayFileFromServer();
                } catch (Exception ex) {
                    Logger.getLogger(AudioClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        ss.start();
        

        btnStop.setEnabled(true);
    }
    
    public synchronized void download(File name,JButton btnDownload) {
        btnDownload.setEnabled(true);
        sDownload= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    downloadFile(name);
                } catch (Exception ex) {
                    Logger.getLogger(AudioClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        sDownload.start();
        

    }
}
