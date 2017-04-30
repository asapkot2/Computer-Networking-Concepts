package com.nbhirud.tcpvsudp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

//Sender
public class TCPClient {

    public static void main(String[] args) {
        try {
            int num_of_chars = Integer.parseInt(args[0]);
            //String path = "localhost";
            String path = args[1];
            System.out.println(path);
            Socket s = new Socket(path, 8777);
            int seq;

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            int x = 5;
            for (int rep = 0; rep < x; rep++) {
                for (seq = 0; seq < num_of_chars; seq++) {
                    String msg = br.readLine();
                    //	System.out.println(msg);
                    out.println(msg);
                    out.flush();
                }
            }
            br.close();
            out.close();
            s.close();

        } catch (UnknownHostException e) {
            System.err.println("Host unknown. Cannot establish connection");
        } catch (IOException e) {
            System.err.println("Cannot establish connection." + e.getMessage());
        }
    }

}
