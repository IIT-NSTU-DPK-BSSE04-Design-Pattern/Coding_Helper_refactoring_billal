package IO;

import console.Command;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ProjectReader {
    private int count;
    private int classCount;
    private LinkedHashMap<String, String> projectFiles = new LinkedHashMap<>();
    private ArrayList<String> filename = new ArrayList<>();
    
    public void fileRead(String fullPath, ProjectType projectType) throws IOException {
        Path folderToWalk = Paths.get(fullPath);
        Files.walkFileTree(folderToWalk, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path f, BasicFileAttributes attr) throws IOException {
                if (f.getFileName().toString().endsWith(".java")) {
                    processFile(f, projectType);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private void processFile(Path f, ProjectType projectType) {
        try {
            String fileContent = new String(Files.readAllBytes(f), StandardCharsets.UTF_8).trim();
            String dir = f.getParent().toString().substring(f.getParent().toString().lastIndexOf(File.separator) + 1);
            String fileNameWithPackage = dir + "$" + f.getFileName().toString();
            filename.add(f.toString()); // Store the full path

            switch (projectType) {
                case PROJECT_ONE:
                    projectFiles.put(fileNameWithPackage, fileContent);
                    break;
                case PROJECT_TWO:
                    projectFiles.put(fileNameWithPackage, fileContent);
                    break;
            }
        } catch (IOException e) {
            System.out.println("Error processing file: " + f);
        }
    }
}