package huffman;

import console.Command;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class mainDecode {

    public void Decompress(String path) throws IOException {
        Decompress de;
        String p = new Command().pathGenerate(path);
        String compressFile = compressFile(p);
        String decompressFile = decompressFile(p);

        de = new Decompress(compressFile, decompressFile);

        de.decompressFile();

    }

    public String compressFile(String path) throws IOException {

        Scanner sc = new Scanner(System.in);
        System.out.print("\tEnter a compressed filename:");


        String filename = sc.nextLine().trim();
        String compressFilepath = path+"\\"+filename;
        boolean exist = new mainEncode().checkFileExist(compressFilepath);


        try {
            if (filename.endsWith(".zip") && exist == true) {
                
                return compressFilepath;

            }else if(!exist){
                System.out.println("\tFile not  exist");
                new Command().command();
            }
            else if(filename.isEmpty()){
                System.out.println("\tInvalid filename");
                  new Command().command();
            }
            else {
                System.out.println("\tInvalid filename");
                new Command().command();

            }
        } catch (Exception e) {
            System.out.println("\tInvalid filename");
        }
        //System.out.println(compressFilepath);
        return compressFilepath;
    }

    public String decompressFile(String path) throws IOException {

        Scanner sc = new Scanner(System.in);
        System.out.print("\tEnter Decompress file name: ");
        String decompressFileName = sc.nextLine().trim();

        if (!(decompressFileName.endsWith(".txt") | decompressFileName.endsWith(".java")) | decompressFileName.isEmpty()) {
            System.out.println("\tInvalid filename");
            new Command().command();

        }
        String decompressfilePath = path + "\\" + decompressFileName;
        boolean exist = checkfileExist(decompressfilePath);
        if (exist) {
            System.out.println("\tSame file already exist here");
           new Command().command();
        }
        return decompressfilePath;
    }
    public boolean checkfileExist(String path) throws IOException{
        boolean exist=false;
     try {
     Path p = Paths.get(path);
     File file = new File(path);
     if (Files.exists(p) && !Files.isDirectory(p)){
         exist=true;
         System.out.println("\tSame file already exist");
         new Command().command();
         
     }else{    
     exist=false;
     }
     
     }catch(Exception e){
         System.out.println("Invalid filename");
      new Command().command();
     
     }
      
    
    return exist;
    
    }
}
