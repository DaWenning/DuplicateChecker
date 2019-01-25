package de.recklessGreed;

import net.lingala.zip4j.core.ZipFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Duplicates {
            String         path         ;
    private File           parent       = null;
    private List<x68_File> codeList     = new ArrayList<>();
            int            verboseLevel = 0;
    private String         extension    = "x68";
            int            percentage   = 10;
    //        boolean        unzip        = false;
            boolean        debug        = false;
            List<String>   x68Files     = new ArrayList<>();
            
    private static List<String> listOfChecked = new ArrayList<>();

    public Duplicates(String path, String extension, int verboseLevel, int percentage) {
        this.path = path;
        this.extension = extension.toLowerCase();
        this.verboseLevel = verboseLevel;
        this.percentage = percentage;
        System.out.println(path + "|" + extension + "|" + verboseLevel + "|" + percentage);

        File parent = new File(path);
        start(parent);

    }


    private void start(File parent) {
        if (!parent.exists()) {
            System.err.println("Error 01: Given File does not exist!");
            return;
        }
        else if (parent.isDirectory())
            checkDirectory(parent);
        else if (parent.isFile())
            checkFile(parent);

        /*if (parent.isDirectory())
            checkDirectory(parent, false);
        else if (parent.isFile())
            checkFile(parent, false);*/
        for (String s : x68Files)
        {
            try {
                codeList.add(new x68_File(new File(s), this));
                verbose(0, "Added: " + s);
                //System.out.println("TEST");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        checkForDuplicates();
    }

    private void checkDirectory(File dir) {

        File[] allChilds = dir.listFiles();

        for (File child : allChilds) {
            checkFile(child);
        }
    }

    private void checkFile(File file) {
        String name = file.getName().toLowerCase();
        //System.out.println("Checking File: " + file.getAbsolutePath());
        if (file.isDirectory()) {
            checkDirectory(file);
        }
        else if (name.endsWith(".zip")) {
            isZip(file);
            verbose(2, "Unzipping " + name);
        }
        else if (name.endsWith(extension)){
            isMatchingExtension(file);
        }
    }

    private void isZip(File zip) {
        String src  = zip.getPath();
        String dest = zip.getPath().replace(".zip", "");

        try {
            ZipFile zipFile = new ZipFile(src);
            zipFile.extractAll(dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkDirectory(new File(dest));
    }

    private void isMatchingExtension(File extension) {
        x68Files.add(extension.getAbsolutePath());
    }

    void verbose(int level, String message) {
        if(level <= verboseLevel)
            System.out.println(message);
    }

    private void checkForDuplicates() {

        for (x68_File first : codeList) {
            for(x68_File second : codeList) {
                if (first.getId().equals(second.getId())
                        || (listOfChecked.contains(first.getId() + second.getId()))
                        || (listOfChecked.contains(second.getId() + first.getId()))) {
                    continue;
                }
                listOfChecked.add(first.getId() + second.getId());
                //System.out.println(first.getId() + " | " + second.getId());
                first.resemblance(second);
            }
        }
    }
}


