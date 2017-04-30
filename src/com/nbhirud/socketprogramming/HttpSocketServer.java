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
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author nbhirud
 */
public class HttpSocketServer {

    /*
	Your server should take a command line argument that specifies the port number that the server will use to listen for incoming connection requests. In other words, assuming that your executable is named "myserver", you should be able to run your server from the command line as follows:
	
	myserver port
	
     */

    public static void main(String args[]) throws Exception {
        System.out.println("The server is running.");
        int port = Integer.parseInt(args[0]);
        int clientNumber = 0;
        /*
		Your server must:
		1. Create a socket with the specified port number
         */
        ServerSocket listener = new ServerSocket(port);

        try {
            while (true) {
                /*
				Your server must:
				2. Listen for incoming connections from clients
				7. Continue to "loop" to listen for incoming connection
                 */
                new service(listener.accept(), clientNumber++).start();
            }
        } finally {
            listener.close();
        }
    }

    private static class service extends Thread {

        private Socket socket;
        private int clientNumber;

        public service(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            System.out.println("New connection with client# " + clientNumber + " at " + socket);
        }

        public void run() {
            try {
                /*
				Your server must:
				3. When a client connection is accepted, read the HTTP request
                 */
                InputStream sis = socket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(sis));
                // Now you get GET or PUT index.html HTTP/1.1
                String request = br.readLine();
                String[] requestParam = request.split(" ");
                String path = requestParam[1].substring(1);
                System.out.println("Type of Request: " + request);

                /*
				Your server must:
				4. Construct a valid HTTP response:
                 */
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                /*
				Your server must:
				a. When the server receives a GET request, it should either construct a "200 OK" message followed by the requested object or a "404 Not Found" message.
                 */
                if (request.contains("GET")) {

                    path = System.getProperty("user.dir") + "/serverfiles/" + path;
                    File file = new File(path);
                    if (!file.exists()) {
                        // the file does not exists
                        out.writeBytes("HTTP/1.1 404 Not Found\r\n");
                        out.flush();
                    }
                    FileReader fr = new FileReader(file);
                    BufferedReader bfr = new BufferedReader(fr);
                    String line;
                    out.writeBytes("HTTP/1.1 200 OK\r\n");
                    out.flush();
                    while ((line = bfr.readLine()) != null) {
                        out.writeBytes(line + "\n");
                        out.flush();
                    }
                    System.out.println("File Sent!");
                    bfr.close();
                    br.close();
                } else if (request.contains("PUT")) {

                    try {
                        path = System.getProperty("user.dir") + "/serverfiles/" + path;

                        request = br.readLine();
                        System.out.println(request);
                        /*
						Your server must:
						b. When the server receives a PUT request, it should save the file locally.
                         */
                        File file = new File(path);
                        String line;

                        BufferedWriter bufferedWriter = new BufferedWriter(
                                new FileWriter(file));

                        while ((line = br.readLine()) != null) {
                            bufferedWriter.write(line);
                            bufferedWriter.write("\r\n");
                            bufferedWriter.flush();
                            System.out.println(line);
                        }
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        br.close();
                        System.out.println("\nFile Copied at " + path);

                        /*
						Your server must:
						c. If the received file is successfully saved, the server should construct a "200 OK File Created" response.
                         */
                        out.writeBytes("HTTP/1.1 200 OK File Created\r\n");
                        out.flush();

                    } catch (IOException e) {
                        System.err.println("No file received from Client: " + e.getMessage());
                        br.close();
                    }
                } else {
                    System.out.println("Wrong Action...use either GET or PUT");
                }

            } catch (IOException e) {
                System.out.println("Error handling client# " + clientNumber + ": " + e.getMessage());
            } finally {
                /*
				Your server must:
				6. Close the client connection */
                try {
                    System.out.println("Closing Socket...");
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Couldn't close a socket:" + e.getMessage());
                }
                System.out.println("Connection with client# " + clientNumber + " closed");
                System.out.println();
            }
        }

    }
}
