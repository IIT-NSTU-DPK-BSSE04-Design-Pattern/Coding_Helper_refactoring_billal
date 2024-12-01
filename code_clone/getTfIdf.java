package code_clone;
import java.util.List;
public class getTfIdf {

    public double getTf(String[] fileContent, String term) {
        double fileLength = fileContent.length;
        int count = 0;
        for (String s : fileContent) {
            if (s.equalsIgnoreCase(term)) {
                count++;
            }
        }

        return count / fileLength;
    }

    public double getIdf(List allFile, String term) {
        double count = 0;
        double idf;
  
        for (int i = 0; i < allFile.size(); i++) {

            String[] fileContent;
            fileContent = allFile.get(i).toString().split(" ");
            for (String ss : fileContent) {

                if (ss.equalsIgnoreCase(term)) {
                    count++;
                    break;

                }
            }
        }
        return 1 + Math.log(allFile.size() / count);
    }
}