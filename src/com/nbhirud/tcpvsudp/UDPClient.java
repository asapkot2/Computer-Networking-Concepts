package com.nbhirud.tcpvsudp;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClient {

    public static void main(String[] args) throws UnknownHostException, IOException {
        String host = "127.0.0.1";
        int port = 8777;
        /* Input fro user for nuber of characters to be sent in the connection */
        int num_of_chars = Integer.parseInt(args[0]);
        String padding = "";
        int count = 10;
        for (int i = 0; i < count; i++) {
            padding = padding + "A";
        }

        System.out.println("Padding = " + padding);
        System.out.println("Padding length = " + padding.length());
        System.out.println();

        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName(host);
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        int seq, rep = 0;
        String msg;
        long starttime;
        float[][] ETE = new float[5][1000];
        float[] ETEmax = new float[5];
        float[] ETEavg = new float[5];
        int x = 5;
        for (rep = 0; rep < x; rep++) {
            for (seq = 0; seq < num_of_chars; seq++) {
                msg = seq + 1 + padding;
                sendData = msg.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                starttime = System.currentTimeMillis();

                clientSocket.send(sendPacket);

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
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
        clientSocket.close();
    }

}
