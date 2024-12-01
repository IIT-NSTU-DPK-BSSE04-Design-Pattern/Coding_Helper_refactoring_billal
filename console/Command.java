package console;

import code_clone.CloneCheck;
import huffman.mainDecode;
import huffman.mainEncode;
import metrices.Average_LOC;
import metrices.FileCount;
import metrices.LineOfCode;
import metrices.MethodCount;
import searching.Search;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command {
    private static final Map<String, Runnable> commandMap = new HashMap<>();
    private static String currentPath;

    static {
        // Initialize the command map
        commandMap.put("help", Command::printHelp);
        commandMap.put("clone", Command::selectProjectsForClone);
        commandMap.put("1", Command::selectProjectsForClone);
        commandMap.put("File_Compress & File_Decompress", Command::selectCompressionOption);
        commandMap.put("2", Command::selectCompressionOption);
        commandMap.put("fcom", Command::compressFile);
        commandMap.put("dcom", Command::decompressFile);
        commandMap.put("search", Command::search);
        commandMap.put("3", Command::search);
        commandMap.put("Metrics", Command::printMetricsHelp);
        commandMap.put("4", Command::printMetricsHelp);
        commandMap.put("mc", Command::countMethods);
        commandMap.put("method_count", Command::countMethods);
        commandMap.put("LOC", Command::countLinesOfCode);
        commandMap.put("Line_Of_Code", Command::countLinesOfCode);
        commandMap.put("a_loc", Command::averageLinesOfProject);
        commandMap.put("average LOc of a Class", Command::averageLinesOfProject);
        commandMap.put("fc", Command::countFiles);
        commandMap.put("File_Cunt", Command::countFiles);
        commandMap.put("cd", Command::changeDirectory);
        commandMap.put("exit", Command::exit);
        commandMap.put("5", Command::exit);
    }

    private final Scanner scanner = new Scanner(System.in);

    public void command() throws IOException {
        while (true) {
            displayPrompt();
            String choice = scanner.nextLine().trim();
            processCommand(choice);
        }
    }

    private void displayPrompt() {
        if (currentPath == null) {
            currentPath = getCurrentPath();
        }
        System.out.print(currentPath + ">");
    }

    private String getCurrentPath() {
        return FileSystems.getDefault().getPath("").toAbsolutePath().toString();
    }

    private void processCommand(String choice) throws IOException {
        Matcher forwardMatcher = Pattern.compile("(?i)\\b(cd)\\b\\s+(.+)").matcher(choice);
        Matcher specialCharMatcher = Pattern.compile("[\"*<>\\/://?\\|\\.]+").matcher(choice);

        if (specialCharMatcher.find()) {
            System.out.println("Invalid command");
            return;
        }

        if (commandMap.containsKey(choice.toLowerCase())) {
            commandMap.get(choice.toLowerCase()).run();
        } else if (forwardMatcher.find()) {
            forwardDirectory(forwardMatcher.group(2));
        } else if (choice.equalsIgnoreCase("dir")) {
            listDirectory(currentPath);
        } else {
            System.out.println("'" + choice + "' is not recognized as a command");
        }
    }

    private static void printHelp() {
        System.out.println("\t1.clone");
        System.out.println("\t2.File_Compress & File_Decompress");
        System.out.println("\t3.Search");
        System.out.println("\t4.Metrics\n\t\tFile Count-->fc\n\t\tMethod Count-->mc\n\t\tLine of Code-->loc\n\t\tAverage line of Code-->a_loc");
        System.out.println("\t5.exit");
    }

    private static void selectProjectsForClone() {
        try {
            System.out.println("\tSelect two projects:");
            String firstProject = inputProjectName();
            projectExist(firstProject);

            String secondProject = inputProjectName();
            projectExist(secondProject);

            CloneCheck cloneCheck = new CloneCheck();
            cloneCheck.Code_clone(firstProject, secondProject);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void selectCompressionOption() {
        System.out.println("\tFor Compress-->fcom");
        System.out.println("\tFor Decompress-->dcom");
    }

    private static void compressFile() {
        new mainEncode().Compress(currentPath);
    }

    private static void decompressFile() {
        new mainDecode().Decompress(currentPath);
    }

    private static void search() {
        System.out.print("\tWrite \"query\" and projectname:");
        String queryWithFile = scanner.nextLine().trim();
        try {
            String query = extractQuery(queryWithFile);
            String projectName = extractProjectName(queryWithFile);

            if (query.isEmpty() || projectName.isEmpty()) {
                System.out.println("Wrong Command");
                return;
            }

            String projectPath = currentPath + "\\" + projectName;
            if (Files.exists(Paths.get(projectPath))) {
                new Search().processProject(projectPath, projectName);
                new Search().SearchingResult(query, projectPath);
            } else {
                System.out.println("The program cannot find '" + projectName + "'");
            }
        } catch (Exception e) {
            System.out.println("Wrong Command");
        }
    }

    private static String extractQuery(String queryWithFile) {
        return queryWithFile.substring(queryWithFile.indexOf("\"") + 1, queryWithFile.lastIndexOf("\"")).trim();
    }

    private static String extractProjectName(String queryWithFile) {
        return queryWithFile.substring(queryWithFile.lastIndexOf("\"") + 1).trim();
    }

    private static void printMetricsHelp() {
        System.out.println("\t4.Metrics\n\t\tJava File Count-->fc\n\t\tMethod Count-->mc\n\t\tLine of Code-->loc\n\t\tAverage LOC of a class");
    }

    private static void countMethods() throws IOException {
        String projectName = inputProjectName();
        String projectPath = currentPath + "\\" + projectName;
        if (Files.exists(Paths.get(projectPath)) && Files.isDirectory(Paths.get(projectPath))) {
            new MethodCount().getTotalMethods(projectPath, projectName);
        } else {
            System.out.println("The program cannot find '" + projectName + "'");
        }
    }

    private static void countLinesOfCode() throws IOException {
        System.out.print("\tWrite the file name:");
        String fileName = scanner.nextLine().trim();
        String filePath = currentPath + "\\" + fileName;
        Path path = Paths.get(filePath);

        if (Files.exists(path) && !Files.isDirectory(path)) {
            int totalLines = new LineOfCode().countLines(filePath);
            System.out.println("\tLine of " + fileName + " is " + totalLines);
        } else {
            System.out.println("The program cannot find '" + fileName + "'");
        }
    }

    private static void averageLinesOfProject() throws IOException {
        String projectName = inputProjectName();
        String projectPath = currentPath + "\\" + projectName;
        Path path = Paths.get(projectPath);

        if (Files.exists(path) && Files.isDirectory(path)) {
            new Average_LOC().totalClass(projectPath);
        } else {
            System.out.println("The program cannot find '" + projectName + "'");
        }
    }

    private static void countFiles() throws IOException {
        String projectName = inputProjectName();
        String projectPath = currentPath + "\\" + projectName;
        if (Files.exists(Paths.get(projectPath)) && Files.isDirectory(Paths.get(projectPath))) {
            new FileCount().classCount(projectPath);
        } else {
            System.out.println("The program cannot find '" + projectName + "'");
        }
    }

    private static void changeDirectory() {
        currentPath = getCurrentPath();
    }

    private static void exit() {
        System.exit(0);
    }

    private static String inputProjectName() {
        System.out.print("\tEnter project name: ");
        return scanner.nextLine().trim();
    }

    private static boolean projectExist(String projectName) throws IOException {
        Path projectPath = Paths.get(currentPath + "\\" + projectName);
        if (!Files.exists(projectPath)) {
            System.out.println("The program cannot find project '" + projectName + "'");
            return false;
        }
        return true;
    }

    private static void forwardDirectory(String dirName) {
        String forwardPath = currentPath + "\\" + dirName;
        checkFileExist(forwardPath);
    }

    private static void checkFileExist(String path) {
        Path p = Paths.get(path);
        if (Files.exists(p)) {
            currentPath = p.toString();
        } else {
            System.out.println("File not found!");
        }
    }

    private static void listDirectory(String path) throws IOException {
        Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file.toString());
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                System.out.println("Failed to access: " + file);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
