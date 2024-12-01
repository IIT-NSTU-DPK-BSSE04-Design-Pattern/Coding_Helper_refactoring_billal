package searching;

import IO.ProjectReader;
import IO.Filereader;
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

public class Search {
    public static ArrayList<String> ProjectFileName = new ArrayList<>();
    public static void ProjectRead(String Projectpath, String processfilePath) throws IOException {
        ArrayList<String> filename = new ArrayList<>();
        Path folderToWalk = Paths.get(Projectpath);
        Files.walkFileTree(folderToWalk, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path f, BasicFileAttributes attr) throws IOException {

                if (f.getFileName().toString().endsWith(".java")) {
                    String fileNamewithPackage = "";
                
                    byte[] p = Files.readAllBytes(f);
                    String content = new String(p, StandardCharsets.UTF_8).trim();
                    String filePath = f.getParent() + "\\" + f.getFileName();
                    String dir = f.getParent().toString().substring(f.getParent().toString().lastIndexOf(File.separator) + 1);
        
                    fileNamewithPackage = dir + "$" + f.getFileName().toString(); /
                    filename.add(fileNamewithPackage);
                    
                    if (!fileNamewithPackage.isEmpty() && !content.isEmpty()) {
                        
                        new MethodFind().getMethod(fileNamewithPackage, content, filePath, processfilePath);
                        new MethodFind().getConstructor(fileNamewithPackage, content, filePath, processfilePath);
                    }
                }
                return FileVisitResult.CONTINUE;

            }
        }
        );
        if (filename.isEmpty()) {
            System.out.println("\tProject doesnot have any java file");
        }
    }

    public void processProject(String projectpath, String Projectname) throws IOException {
        String current = projectpath.replaceAll("\\\\", "-").replace(":", "");
    
        String processfilePath = "H:\\2-1\\project\\ProcessAllFiles" + "\\ProcessMethod$" + current;
        File f1 = new File(processfilePath);
        if (!f1.exists()) {
            Path p1 = Paths.get(processfilePath);
            Files.createDirectories(p1);
            ProjectRead(projectpath, processfilePath);
        
        }
        getProjectFile(Projectname);
    }

    
    public String getProcessFilepath(String projectname){
       String currentpath = Command.currentPath;
    String path = new Command().pathGenerate(currentpath);    
    String current = path.replaceAll("\\\\", "-").replace(":", "");
    String Pathname = "H:\\2-1\\project\\ProcessAllFiles" + "\\ProcessMethod$" + current + "-" + projectname;
    return Pathname;
    
    }
    public void getProjectFile(String projectname) throws IOException {

       String Pathname=getProcessFilepath(projectname);
        ProjectReader.getFileList(projectname, Pathname, ProjectFileName);

    }

    public void getFile(String projectPath, String fileName) throws IOException {
        String currentPath = Command.currentPath;
      String processfilePath=getProcessFilepath(fileName);
        File f1 = new File(processfilePath);
          File file = new File(currentPath + "\\" + fileName);
      

        Path p = Paths.get(currentPath + "\\" + fileName);
        if (file.getName().endsWith(".java") && !Files.isDirectory(p)) {
            boolean fileEmpty=new Filereader().fileEmpty(p.toString());
             byte[] s = Files.readAllBytes(p);
            String fileContent = new String(s, StandardCharsets.UTF_8).trim();
            if(fileEmpty){
                System.out.println("File is Empty");
            }
          
         
             else {
                if (!f1.exists()) {
                    Path p1 = Paths.get(processfilePath);
                    Files.createDirectories(p1);
                    new MethodFind().getMethod(file.getName(), fileContent, file.getAbsolutePath(), processfilePath);
                    new MethodFind().getConstructor(file.getName(), fileContent, file.getAbsolutePath(), processfilePath);
                   
                }
                
            }
            getProjectFile(file.getName());
           
        }
    }

    public void SearchingResult(String query, String projectPath) throws IOException {
       
        String processfilePath = "H:\\2-1\\project\\ProcessAllFiles" + "\\ProcessMethod$" + current;
        String processquery = new ProcessSearchFile().queryProcess(query);
        TfIdfCalculate ob = new TfIdfCalculate();
        ob.fileRead(processfilePath);
        ob.Idfcal();
        ob.UniqueQueryTerms(processquery);
        ob.queryTfIdfCal(processquery);
        ob.ProjectTfIdfCal();


        new Similarity().getCosine();
        new Similarity().getResult();
        ProjectFileName.clear();
        TfIdfCalculate.queryTerms.clear();
        TfIdfCalculate.queryTfIdfVector.clear();
        TfIdfCalculate.tfidfvectorProject.clear();

    }

}
