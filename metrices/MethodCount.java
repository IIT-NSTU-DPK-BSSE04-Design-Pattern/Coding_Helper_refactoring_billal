package metrices;

import IO.ProjectReader;
import java.io.IOException;
import searching.Search;

public class MethodCount {

    public void getTotalMethods(String projectPath, String ProjectName) throws IOException {
        try{
        if (ProjectName.endsWith(".java")) {
            new Search().getFile(projectPath, ProjectName);
        } else {
            new Search().processProject(projectPath, ProjectName);
        }
        int totalMethods = ProjectReader.count;
        System.out.println("\tTotal methods:" + totalMethods);
        ProjectReader.count = 0;
        Search.ProjectFileName.clear();
    }catch(Exception e){
    
    
    }}
}







