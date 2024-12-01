package huffman;

import console.Command;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


public class mainEncode {

    public boolean exist = false;

    public void Compress(String path) throws IOException {

        Compress encode;
        String filename = mainfile(path);
        exist = false;
        String compressFileName = compressFile(path);

        encode = new Compress(filename, compressFileName);
        encode.compressFile();

    }

    public String mainfile(String path) throws IOException {
        String p = new Command().pathGenerate(path);
        Scanner sc = new Scanner(System.in);
        String mainFilePath = "";
        String filename = "";

        try {
            System.out.print("\tEnter a filename:");
            filename = sc.nextLine().trim();
            mainFilePath = p + "\\" + filename;
            checkFileExist(mainFilePath);
            if (!(filename.endsWith(".java") | filename.endsWith(".txt")) | filename.isEmpty()) {
                System.out.println("\tInvalid filename");
                new Command().command();
            }
        } catch (Exception e) {

        }

        if (!checkFileExist(mainFilePath)) {
            System.out.println("\tFile not exist.");
            new Command().command();
        }
        boolean fileEmpty = new IO.Filereader().fileEmpty(mainFilePath);

        if (fileEmpty) {
            System.out.println("\tFile is empty");
            new Command().command();
        }
        return mainFilePath;
    }

    public String compressFile(String path) throws IOException {
        String p = new Command().pathGenerate(path);
        Scanner sc = new Scanner(System.in);
        System.out.print("\tEnter compress filename: ");

        String compressFileName = sc.nextLine().trim();
        String compressfilePath = "";
        try {

            if (!compressFileName.endsWith(".zip") | compressFileName.isEmpty()  ) {
                System.out.println("\tInvalid filename");
                new Command().command();

            } 

             else {

                compressfilePath = p + "\\" + compressFileName;
                checkFileExist(compressfilePath);
            }
        } catch (Exception e) {

        }
        if (exist) {
            System.out.println("\tSame file already exist in this location ");
            new Command().command();
        }
        return compressfilePath;
    }


    public boolean checkFileExist(String path) throws IOException {
        try {
            Path p = Paths.get(path);
            File file = new File(path);
            if (Files.exists(p) && !Files.isDirectory(p)) {
                exist = true;
            } else {
                exist = false;

            }
        } catch (Exception e) {
            System.out.println("\tInvalid filename");
            new Command().command();

        }
        return exist;
    }

}
