package searching;

import IO.ProjectReader;
import IO.Filereader;
import console.Command;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class Search {
    public static ArrayList<String> projectFileNames = new ArrayList<>();

    public static void processProjectFiles(String projectPath, String processFilePath) throws IOException {
        ArrayList<String> filenames = new ArrayList<>();
        Path folderPath = Paths.get(projectPath);

        Files.walkFileTree(folderPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.getFileName().toString().endsWith(".java")) {
                    String fileNameWithPackage = "";
                    byte[] fileContentBytes = Files.readAllBytes(file);
                    String fileContent = new String(fileContentBytes, StandardCharsets.UTF_8).trim();
                    String filePath = file.getParent() + "\\" + file.getFileName();
                    String directory = file.getParent().toString()
                            .substring(file.getParent().toString().lastIndexOf(File.separator) + 1);

                    fileNameWithPackage = directory + "$" + file.getFileName();
                    filenames.add(fileNameWithPackage);

                    if (!fileNameWithPackage.isEmpty() && !fileContent.isEmpty()) {
                        MethodFind methodFinder = new MethodFind();
                        methodFinder.getMethod(fileNameWithPackage, fileContent, filePath, processFilePath);
                        methodFinder.getConstructor(fileNameWithPackage, fileContent, filePath, processFilePath);
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });

        if (filenames.isEmpty()) {
            System.out.println("\tProject does not contain any Java files.");
        }
    }
    public void processProject(String projectPath, String projectName) throws IOException {
        String sanitizedPath = sanitizePath(projectPath);
        String processFilePath = "H:\\2-1\\project\\ProcessAllFiles\\ProcessMethod$" + sanitizedPath;

        File processFile = new File(processFilePath);
        if (!processFile.exists()) {
            Files.createDirectories(Paths.get(processFilePath));
            processProjectFiles(projectPath, processFilePath);
        }

        getProjectFileList(projectName);
    }

    public String getProcessFilePath(String projectName) {
        String currentPath = Command.currentPath;
        String sanitizedPath = sanitizePath(new Command().pathGenerate(currentPath));
        return "H:\\2-1\\project\\ProcessAllFiles\\ProcessMethod$" + sanitizedPath + "-" + projectName;
    }


    public void getProjectFileList(String projectName) throws IOException {
        String processFilePath = getProcessFilePath(projectName);
        ProjectReader.getFileList(projectName, processFilePath, projectFileNames);
    }


    public void processSingleFile(String projectPath, String fileName) throws IOException {
        String currentPath = Command.currentPath;
        String processFilePath = getProcessFilePath(fileName);
        File file = new File(currentPath + "\\" + fileName);

        if (file.getName().endsWith(".java") && !Files.isDirectory(file.toPath())) {
            boolean isFileEmpty = new Filereader().fileEmpty(file.getAbsolutePath());
            if (isFileEmpty) {
                System.out.println("File is empty.");
                return;
            }

            String fileContent = Files.readString(file.toPath(), StandardCharsets.UTF_8).trim();
            File processDir = new File(processFilePath);

            if (!processDir.exists()) {
                Files.createDirectories(Paths.get(processFilePath));
                MethodFind methodFinder = new MethodFind();
                methodFinder.getMethod(file.getName(), fileContent, file.getAbsolutePath(), processFilePath);
                methodFinder.getConstructor(file.getName(), fileContent, file.getAbsolutePath(), processFilePath);
            }

            getProjectFileList(file.getName());
        }
    }


    public void searchResults(String query, String projectPath) throws IOException {
        String processFilePath = "H:\\2-1\\project\\ProcessAllFiles\\ProcessMethod$" + sanitizePath(projectPath);
        String processedQuery = new ProcessSearchFile().queryProcess(query);

        TfIdfCalculate tfIdf = new TfIdfCalculate();
        tfIdf.fileRead(processFilePath);
        tfIdf.Idfcal();
        tfIdf.UniqueQueryTerms(processedQuery);
        tfIdf.queryTfIdfCal(processedQuery);
        tfIdf.ProjectTfIdfCal();

        Similarity similarity = new Similarity();
        similarity.getCosine();
        similarity.getResult();

        clearSearchData();
    }


    private void clearSearchData() {
        projectFileNames.clear();
        TfIdfCalculate.queryTerms.clear();
        TfIdfCalculate.queryTfIdfVector.clear();
        TfIdfCalculate.tfidfvectorProject.clear();
    }

    private String sanitizePath(String path) {
        return path.replaceAll("\\\\", "-").replace(":", "");
    }
}
