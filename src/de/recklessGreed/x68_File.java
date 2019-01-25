package de.recklessGreed;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

import org.apache.commons.text.similarity.CosineDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;

public class x68_File {
    private List<String> content;
    private String text = "";
    private String id;
    private String name;
    Duplicates duplicates;

    x68_File(File codeFile, Duplicates duplicates) throws IOException {
        this.duplicates = duplicates;
        content = Files.readAllLines(codeFile.toPath(), Charset.forName("windows-1252"));
        for(String line : content) {
            text += line + "\n";
        }
        //System.out.println();
        System.out.println("Added " + codeFile.getName()
        );
        name = codeFile.getParent().replace(duplicates.path + "\\", "");
        this.id = codeFile.getAbsolutePath();
    }


    void resemblance(x68_File second) {
        // Check this against another code File in percent!
        if (duplicates.verboseLevel > 0)System.out.println(name + " <-> " + second.getName());
        LevenshteinDistance lev = new LevenshteinDistance();
        float levDist = lev.apply(text, second.getText());
        if (duplicates.verboseLevel > 0)System.out.println("\t Levenshtein: " + levDist);


        CosineDistance cos = new CosineDistance();
        double cosDist = cos.apply(text, second.getText());
        if (duplicates.verboseLevel > 0)System.out.println("\t CosDist: " + cosDist);
        if (cosDist < (duplicates.percentage / 100.0)) {
            new DuplicateScreen(this, second, cosDist);
            System.out.println("CHECK NEEDED: " + name + " <-> " + second.getName());
        }
        //return 0.0;
    }

    public List<String> getContent() {
        return content;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean equals(x68_File second) {
        return id.equals(second.getId());
    }
}
