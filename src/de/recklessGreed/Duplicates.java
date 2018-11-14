package de.recklessGreed;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Duplicates {
            static String         path         = null;
    private static File           parent       = null;
    private static List<x68_File> codeList     = new ArrayList<>();
            static int            verboseLevel = 0;
    private static String         extension    = "x68";
            static int            percentage   = 10;
            static boolean        unzip        = false;

    //private static Map<x68_File,x68_File> listOfMatches = new HashMap<>();
    private static List<String> listOfChecked = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("START OF FILE");
        if (args.length < 1) {
            System.err.println("Not Enough Arguments.");
            printHelp();
            return;
        }

        boolean error = parseArgs(args);
        if(error)
            return;

        /* Required Args Check */
        if (path == null) {
            System.err.println("No Path Given!");
            printHelp();
        }

        parent = new File(path);
        if (!parent.exists()) {
            System.err.println("Could not locate path");
            return;
        }


        verbose(1,"Path: " + parent.getAbsolutePath());
        if(unzip)
            unzipAll();
        addFilesToList();
        checkForDuplicates();
    }

    private static void printHelp() {
        System.out.println("Duplicate Checker - Version 1.0");
        System.out.println("Required Parameters:");
        System.out.println("-p <path>\t: Path to working directory");
        System.out.println("Optional Parameters:");
        System.out.println("-e <ext>\t: File Extension \t\t\t [Default: x68]");
        System.out.println("-h\t\t\t: Print this Help Message");
        System.out.println("-(v|vv|vvv)\t: Verbose Level");
        System.out.println("-u\t\t\t: Unzip the zips in the First Level of PATH");
        System.out.println("-x <int>\t: Detection Percentage\t\t [Default: 10]");

    }

    private static void unzipAll() {
        File[] listOfFiles = parent.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.contains(".zip"))
                    return true;
                return false;
            }
        });

        for (File zipChild : listOfFiles) {
            if(verboseLevel>0)System.out.println(zipChild.getPath());
            unzip(zipChild.getPath(), zipChild.getPath().replace(".zip", ""));
        }
    }

    private static void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                if(verboseLevel > 0)System.out.println("Unzipping to "+newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void addFilesToList() {
        File[] list = parent.listFiles();
        for(File child : list) {
            String path = child.getPath();

            if (path.endsWith("zip") || !child.isDirectory())
            { continue; }
            File[] child_list = child.listFiles();
            if(verboseLevel > 1)
                System.out.println("LIST: " + path);
            for(File sub: child_list) {
                //System.out.println("Inner For: " + sub.getPath());
                if (sub.getPath().toLowerCase().endsWith(extension)) {
                    try {
                        if(verboseLevel > 0)
                            System.out.println("Added " + sub.getPath());
                        codeList.add(new x68_File(sub));
                    }
                    catch (IOException ioex) {
                        JOptionPane.showMessageDialog(null,
                                "Datei " + sub.getPath() + " konnte nicht eingelesen werden!\n"+ioex.getMessage(),
                                "Datei konnte nicht eingelesen werden",
                                JOptionPane.ERROR_MESSAGE);

                    }
                }
            }
        }
    }

    private static void checkForDuplicates() {

        for (x68_File first : codeList) {
            for(x68_File second : codeList) {
                if (first.equals(second)
                        || (listOfChecked.contains(first.getId() + second.getId()))
                        || (listOfChecked.contains(second.getId() + first.getId()))) {
                    continue;
                }
                listOfChecked.add(first.getId() + second.getId());
                first.resemblance(second);
            }
        }
    }

    private static boolean parseArgs(String[] args) {
        for(int i = 0; i < args.length; i++) {
            String curr = args[i];
            if (curr.equalsIgnoreCase("-p")) {
                path = args[i+1];
                i++;
            }
            else if (curr.equalsIgnoreCase("-e")) {
                extension = args[i+1];
                i++;
                if(verboseLevel > 0)System.out.print("Extension: " + extension);
            }
            else if (curr.equalsIgnoreCase("-x")) {
                percentage = Integer.parseInt(args[i+1]);
                i++;
                if(verboseLevel > 0)System.out.print("Percentage: " + percentage);
            }
            else if (curr.equalsIgnoreCase("-v")) {verboseLevel = 1;}
            else if (curr.equalsIgnoreCase("-vv")) {verboseLevel = 2;}
            else if (curr.equalsIgnoreCase("-vvv")) {verboseLevel = 3;}
            else if (curr.equalsIgnoreCase("-u")) {
                unzip = true;
            }
            else if (curr.equalsIgnoreCase("-h")) {
                printHelp();
                return true;
            }
        }
        return false;
    }

    private static void verbose(int level, String message) {
        if(level >= verboseLevel)
            System.out.println(message);
    }
}


