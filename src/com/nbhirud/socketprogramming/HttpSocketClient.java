/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nbhirud.socketprogramming;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author nbhirud
 */
public class HttpSocketClient {

    private String hostname;
    private int port;
    Socket socketClient;

    public HttpSocketClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void connect() throws UnknownHostException, IOException {

        socketClient = new Socket(hostname, port);
        System.out.println("TCP Connection Established to " + hostname + ":" + port);
        System.out.println();
    }

    public void getreadResponse(String filename) throws IOException {

        PrintWriter pw = new PrintWriter(socketClient.getOutputStream());

        /*
		In response to a GET command, the client must:
		2. submit a valid HTTP/1.1 GET request to the server
         */
        pw.print("GET /" + filename + " HTTP/1.1\r\n");
        pw.print("Accept: text/plain, text/html, text/*\r\n");
        pw.print("\r\n");
        pw.flush();

        //Create a file on client to write data received from server
        String path = System.getProperty("user.dir") + "/clientfiles/" + filename;
        File file = new File(path);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

        /*
		In response to a GET command, the client must:
		3. read the server’s response and display it
		Additional feature: Writing the received data to a file on client
         */
        BufferedReader br = new BufferedReader(new InputStreamReader(
                socketClient.getInputStream()));
        String t;
        String gt = br.readLine();
        /*
		receipt of "200 OK" message followed by the requested object from server to client
         */
        if (gt.contains("200")) {
            System.out.println(gt);
            while ((t = br.readLine()) != null) {
                //Write data to file
                bufferedWriter.write(t);
                bufferedWriter.write("\r\n");
                bufferedWriter.flush();
                //Print data to console
                System.out.println(t);
            }
            bufferedWriter.close();
            br.close();

            /*
		receipt of "404 Not Found" message from server to client
             */
        } else if (gt.contains("404")) {
            System.out.println(gt);
        }

    }

    public void putsendResponse(String filename) throws IOException {


        /*
		In response to a PUT command, the client must:
		2. submit a valid HTTP/1.1 PUT request to the server
         */
        PrintWriter pw = new PrintWriter(socketClient.getOutputStream());
        pw.print("PUT /" + filename + " HTTP/1.1\r\n");
        pw.print("Accept: text/plain, text/html, text/*\r\n\r\n");

        /*
		In response to a PUT command, the client must:
		3. send the file to the server
         */
        try {
            //file path on client to read data from
            filename = System.getProperty("user.dir") + "/clientfiles/" + filename;
            System.out.println(System.getProperty("user.dir"));
            FileReader fr = new FileReader(filename);
            BufferedReader bfr = new BufferedReader(fr);
            String line;
            //read one line at a time from buffer
            while ((line = bfr.readLine()) != null) {
                //and write it to the socketClient
                pw.write(line);
            }
            pw.flush();

            /*
			In response to a PUT command, the client must:
			4. wait for the server’s reply
             */
            BufferedReader br = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));

            /*
			In response to a PUT command, the client must:
			5. read the server’s response and display it
             */
            System.out.println(br.readLine());

            br.close();
            bfr.close();

        } catch (IOException e) {
            System.err.println(e.getMessage());
            pw.close();
        }
    }

    public static void main(String[] args) {

        /*
		Your client should take the following command line arguments (in order): server name, port on which to contact the server, HTTP command (GET or PUT), and the path of the requested object on the server. In other words, assuming that your executable is named "myclient", you should be able to run your program from the command line as follows:
		
		myclient hostname port command filename
		
         */
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String command = args[2];
        String filename = args[3];

        /*
		In response to a GET or PUT command, the client must:
		1. connect to the server via a TCP connection
         */
        // Creating a SocketClient object
        HttpSocketClient client = new HttpSocketClient(host, port);

        try {
            // trying to establish connection to the server
            client.connect();
            // if successful, send action to server
            // client.getorput(command);

        } catch (UnknownHostException e) {
            System.err.println("Host unknown. Cannot establish connection");
        } catch (IOException e) {
            System.err.println("Cannot establish connection." + e.getMessage());
        }

        //In response to a GET command from user
        if (command.equalsIgnoreCase("get")) {

            try {
                client.getreadResponse(filename);
            } catch (IOException e) {
                System.err.println("Cannot establish connection." + e.getMessage());
            }
        } //In response to a PUT command from user
        else if (command.equalsIgnoreCase("put")) {
            try {
                client.putsendResponse(filename);

            } catch (IOException e) {
                System.err.println("Cannot establish connection." + e.getMessage());
            }
        } else {
            System.out.println("Please enter Correct Command");
        }

    }
}
