package com.nbhirud.tcpvsudp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSock = new ServerSocket(8777);

        Socket sock = serverSock.accept();
        System.out.println("The server is running.");

        String padding = "";
        int count = 10;

        for (int i = 0; i < count; i++) {
            padding = padding + "A";
        }
        System.out.println("Padding = " + padding);
        System.out.println("Padding length = " + padding.length());
        System.out.println();

        int seq, rep;
        String msg = null;
        InputStream sis = null;
        long starttime;
        float[][] ETE = new float[5][1000];
        float[] ETEmax = new float[5];
        float[] ETEavg = new float[5];

        PrintWriter out = new PrintWriter(sock.getOutputStream(), true);

        BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));

        int x = 1;
        for (rep = 0; rep < x; rep++) {

            for (seq = 0; seq < 1000; seq++) {
                msg = seq + 1 + padding;
                starttime = System.currentTimeMillis();
                out.println(msg);
                out.flush();
                msg = br.readLine();
                ETE[rep][seq] = (System.currentTimeMillis() - starttime) / 2f;
            }
            int flag = 0;
            System.out.println("Number of samples for repetition " + rep + "= " + ETE[rep].length);
            ETEmax[rep] = ETE[rep][0];
            ETEavg[rep] = ETE[rep][0];
            for (int i = 1; i < ETE[rep].length; i++) {
                if (ETE[rep][i] > ETEmax[rep]) {
                    ETEmax[rep] = ETE[rep][i];
                    flag = i + 1;
                }
                ETEavg[rep] = ETEavg[rep] + ETE[rep][i];
            }
            ETEavg[rep] = ETEavg[rep] / 1000f;
            System.out.println("Sequence Number for max. ETE = " + flag);
            System.out.println("Maximum ETE = " + ETEmax[rep] + " milliseconds");
            System.out.println("Average ETE = " + ETEavg[rep] + " milliseconds");
            System.out.println();
        }
        br.close();
        out.close();
        sock.close();

    }

}
