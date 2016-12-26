/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import org.omg.CORBA.portable.InputStream;

/**
 *
 * @author lannguyen
 */
public class AudioServer {

    private static ServerSocket serverSocket;
    private static Socket socket;
    Scanner inFromClient;

    public AudioServer(Socket socket) {
        this.socket = socket;
    }

    public AudioServer() throws IOException {
        try {
            this.serverSocket = new ServerSocket(5000);
            System.out.println("Server is listening...");
            for (;;) {
                socket = this.serverSocket.accept();
                Thread t = new ClientThread(socket);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serverSocket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        AudioServer server = new AudioServer();
        /*int portNumber = 5000;
        if (args.length < 1) {
            System.out
                    .println("Usage: java MultiThreadChatServer <portNumber>\n"
                            + "Now using port number=" + portNumber);
        } else {
            portNumber = Integer.valueOf(args[0]).intValue();
        }

        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println(e);
        }

        while (true) {
            try {
                socket = serverSocket.accept();
                int i = 0;
                for (;;) {
                    
                        Thread t = new ClientThread(socket);
                        t.start();
                        break;
                    
                }

            } catch (IOException e) {
                System.out.println(e);
            }
        }*/
    }

    /*	public static void main(String[] args) throws IOException, Exception {

		ServerSocket socket = new ServerSocket(5000);
		Socket client = socket.accept();
		AudioServer server = new AudioServer(client);

		server.sendFileToClient(new File("Track1.mp3"));
		client.close();
		
		
	}*/
 /*	public void sendListSongToClient() throws IOException{
		ArrayList songs = new ArrayList<>();
		songs.add("asdasd");
		songs.add("lkjlkj");
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.writeObject(songs); 
	}*/
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

    public void sendFileToClient(File file) throws Exception {

        FileInputStream in = new FileInputStream(file);
        BufferedInputStream bufferIn = new BufferedInputStream(in);

        OutputStream os = socket.getOutputStream(); // client

        byte[] contents;
        int fileLength = (int) file.length();

        contents = new byte[fileLength];
        int i = 0;
        while ((i = in.read(contents, 0, fileLength)) != -1) {
            os.write(contents, 0, i);
            os.flush();
        }
        socket.shutdownOutput();
        System.out.println("Server gui File th�nh c�ng!");
    }

    public String getStringFromClient() throws IOException {
        String fromClient = "";

        System.out.print("bat dau nhan chuoi");
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        fromClient = inFromClient.readLine();
        System.out.println("Received: " + fromClient);

        return fromClient;
    }

    //~~~~~
    public void sendFileToClient2() throws Exception {

        String fromClient = "";

        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        fromClient = inFromClient.readLine();
        System.out.println("Received: " + fromClient);

        File songName = new File("fromClient");

        FileInputStream in = new FileInputStream(songName);
        BufferedInputStream bufferIn = new BufferedInputStream(in);

        OutputStream os = socket.getOutputStream(); // client

        byte[] contents;
        int fileLength = (int) songName.length();

        contents = new byte[fileLength];
        int i = 0;
        while ((i = in.read(contents, 0, fileLength)) != -1) {
            os.write(contents, 0, i);
            os.flush();
        }
        socket.shutdownOutput();
        System.out.println("Server gui File th�nh c�ng!");
    }
}
