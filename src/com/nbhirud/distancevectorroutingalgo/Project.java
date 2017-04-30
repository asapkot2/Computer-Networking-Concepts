package com.nbhirud.distancevectorroutingalgo;

import java.awt.Desktop;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Project {

    public final static void main(String[] argv) {

        NetworkSimulator simulator;

        int trace = -1;
        int hasLinkChange = -1;
        long seed = -1;
        String buffer = "";
        boolean hasChange;

        BufferedReader stdIn = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.println("Network Simulator v1.0");

        while (trace < 0) {
            System.out.print("Enter trace level (>= 0): [0] ");
            try {
                buffer = stdIn.readLine();
            } catch (IOException ioe) {
                System.out.println("IOError reading your input!");
                System.exit(1);
            }

            if (buffer.equals("")) {
                trace = 0;
            } else {
                try {
                    trace = Integer.parseInt(buffer);
                } catch (NumberFormatException nfe) {
                    trace = -1;
                }
            }
        }

        while ((hasLinkChange < 0) || (hasLinkChange > 1)) {
            System.out.print("Will the link change (1 = Yes, 0 = No): [0] ");
            try {
                buffer = stdIn.readLine();
            } catch (IOException ioe) {
                System.out.println("IOError reading your input!");
                System.exit(1);
            }

            if (buffer.equals("")) {
                hasLinkChange = 0;
            } else {
                try {
                    hasLinkChange = Integer.parseInt(buffer);
                } catch (NumberFormatException nfe) {
                    hasLinkChange = -1;
                }
            }
        }

        while (seed < 1) {
            System.out.print("Enter random seed: [random] ");
            try {
                buffer = stdIn.readLine();
            } catch (IOException ioe) {
                System.out.println("IOError reading your input!");
                System.exit(1);
            }

            if (buffer.equals("")) {
                seed = System.currentTimeMillis();
            } else {
                try {
                    seed = (Long.valueOf(buffer)).longValue();
                } catch (NumberFormatException nfe) {
                    seed = -1;
                }
            }
        }
        String filepath = "C:\\CCN\\Output.html";
        new File("C:\\CCN").mkdir();
        File f = new File(filepath);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(f);
            PrintStream ps = new PrintStream(fos);
            System.setOut(ps);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String style = "<style type=\"text/css\">\n"
                + "\n"
                + "\n"
                + ".entity1_blue\n"
                + "{ background-color:#FFFFE0;border-collapse:collapse;font-family:Georgia, Garamond, Serif;color:blue; }\n"
                + "\n"
                + ".entity1_blue th \n"
                + "{ font:bold 18px/1.1em Arial, Helvetica, Sans-Serif;text-shadow: 1px 1px 4px black;letter-spacing:0.3em;background-color:#BDB76B;color:white; }\n"
                + "\n"
                + ".entity1_blue td, .entity1_blue th { padding:5px;border:1px solid #BDB76B; }\n"
                + "\n"
                + ".entity1_blue td { line-height:2.5em; }\n"
                + "\n"
                + "\n"
                + "\n"
                + "\n"
                + ".entity1_red\n"
                + "{ background-color:#FFFFE0;border-collapse:collapse;font-family:Georgia, Garamond, Serif;color:red; }\n"
                + "\n"
                + ".entity1_red th \n"
                + "{ font:bold 18px/1.1em Arial, Helvetica, Sans-Serif;text-shadow: 1px 1px 4px black;letter-spacing:0.3em;background-color:#BDB76B;color:white; }\n"
                + "\n"
                + ".entity1_red td, .entity1_red th { padding:5px;border:1px solid #BDB76B; }\n"
                + "\n"
                + ".entity1_red td { line-height:2.5em; }\n"
                + "\n"
                + "\n"
                + "\n"
                + ".entity2_green\n"
                + "{ background-color:#FFFFE0;border-collapse:collapse;font-family:Georgia, Garamond, Serif;color:green; }\n"
                + "\n"
                + ".entity2_green th \n"
                + "{ font:bold 18px/1.1em Arial, Helvetica, Sans-Serif;text-shadow: 1px 1px 4px black;letter-spacing:0.3em;background-color:#BDB76B;color:white; }\n"
                + "\n"
                + ".entity2_green td, .entity2_green th { padding:5px;border:1px solid #BDB76B; }\n"
                + "\n"
                + ".entity2_green td { line-height:2.5em; }\n"
                + "\n"
                + "\n"
                + ".entity3_darkviolet\n"
                + "{ background-color:#FFFFE0;border-collapse:collapse;font-family:Georgia, Garamond, Serif;color:DarkViolet; }\n"
                + "\n"
                + ".entity3_darkviolet th \n"
                + "{ font:bold 18px/1.1em Arial, Helvetica, Sans-Serif;text-shadow: 1px 1px 4px black;letter-spacing:0.3em;background-color:#BDB76B;color:white; }\n"
                + "\n"
                + ".entity3_darkviolet td, .entity3_darkviolet th { padding:5px;border:1px solid #BDB76B; }\n"
                + "\n"
                + ".entity3_darkviolet td { line-height:2.5em; }\n"
                + "\n"
                + "\n"
                + "\n"
                + "\n"
                + "\n"
                + "</style>";

        System.out.println("<html><head><title>Distance Vector Algorithm</title>" + style + "</head><body>");

        if (hasLinkChange == 0) {
            hasChange = false;
        } else {
            hasChange = true;
        }

        simulator = new NetworkSimulator(hasChange, trace, seed);

        simulator.runSimulator();
        System.out.println("</body></html>");
        try {
            Desktop.getDesktop().browse(f.toURI());
        } catch (IOException ex) {
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
