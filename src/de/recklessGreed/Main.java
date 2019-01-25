package de.recklessGreed;

import java.io.File;

public class Main {

    static Duplicates duplicates;


    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Not Enough Arguments.");
            printHelp();
            return;
        }
        parseArgs(args);
    }

    private static void printHelp() {
        System.out.println("Duplicate Checker - Version 1.0");
        System.out.println("Required Parameters:");
        System.out.println("-p <path>\t: Path to working directory");
        System.out.println("Optional Parameters:");
        System.out.println("-e <ext>\t: File Extension \t\t\t [Default: x68]");
        System.out.println("-h\t\t\t: Print this Help Message");
        System.out.println("-(v|vv|vvv)\t: Verbose Level");
        //System.out.println("-u\t\t\t: Unzip the zips in the First Level of PATH");
        System.out.println("-x <int>\t: Detection Percentage\t\t [Default: 10]");

    }
    private static void parseArgs(String[] args) {
        String path = null;
        String extension = "x68";
        int verboseLevel = 0;
        int percentage = 10;
        for (int i = 0; i < args.length; i++) {
            String curr = args[i];
            if (curr.equalsIgnoreCase("-p")) {
                path = args[i + 1];
                i++;
            } else if (curr.equalsIgnoreCase("-e")) {
                extension = args[i + 1];
                i++;
                if (verboseLevel > 0) System.out.print("Extension: " + extension);
            } else if (curr.equalsIgnoreCase("-x")) {
                percentage = Integer.parseInt(args[i + 1]);
                i++;
                if (verboseLevel > 0) System.out.print("Percentage: " + percentage);
            } else if (curr.equalsIgnoreCase("-v")) {
                verboseLevel = 1;
            } else if (curr.equalsIgnoreCase("-vv")) {
                verboseLevel = 2;
            } else if (curr.equalsIgnoreCase("-vvv")) {
                verboseLevel = 3;
            } else if (curr.equalsIgnoreCase("-u")) {
                //unzip = true;
            } else if (curr.equalsIgnoreCase("-h")) {
                printHelp();
            }
        }
        if (path == null) {
            printHelp();
            return;
        }
        duplicates = new Duplicates(path, extension, verboseLevel, percentage);
    }
}
