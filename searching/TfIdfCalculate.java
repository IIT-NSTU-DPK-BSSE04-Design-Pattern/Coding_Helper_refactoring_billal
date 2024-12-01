package searching;

import code_clone.getTfIdf;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class TfIdfCalculate {

    private ArrayList<String> allTerms = new ArrayList<>();
    private ArrayList<String> fileContents = new ArrayList<>();
    private ArrayList<String[]> fileWordsList = new ArrayList<>();
    private HashMap<String, Double> idfMap = new HashMap<>();
    public static ArrayList<String> queryTerms = new ArrayList<>();
    public static ArrayList<double[]> queryTfIdfVector = new ArrayList<>();
    public static ArrayList<double[]> tfidfVectors = new ArrayList<>();

    public void readFiles(String directoryPath) throws IOException {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.getName().endsWith(".txt")) {
                Path filePath = Paths.get(directoryPath, file.getName());
                String content = Files.readString(filePath, StandardCharsets.UTF_8).trim();
                String[] terms = content.split(" ");
                for (String term : terms) {
                    if (!allTerms.contains(term)) {
                        allTerms.add(term);
                    }
                }
                fileContents.add(content);
                fileWordsList.add(terms);
            }
        }
    }

    public void calculateIdf() {
        getTfIdf tfIdfCalculator = new getTfIdf();
        for (String term : allTerms) {
            double idf = tfIdfCalculator.getIdf(fileContents, term);
            idfMap.put(term, idf);
        }
    }

    public void extractUniqueQueryTerms(String query) {
        String[] terms = query.trim().split(" ");
        for (String term : terms) {
            if (!queryTerms.contains(term)) {
                queryTerms.add(term);
            }
        }
    }

    public void calculateProjectTfIdf() {
        getTfIdf tfIdfCalculator = new getTfIdf();

        for (String[] fileWords : fileWordsList) {
            double[] tfidfVector = new double[queryTerms.size()];
            int index = 0;

            for (String term : queryTerms) {
                double tf = tfIdfCalculator.getTf(fileWords, term);
                double idf = idfMap.getOrDefault(term, 0.0);
                tfidfVector[index++] = tf * idf;
            }
            tfidfVectors.add(tfidfVector);
        }
    }

    public void calculateQueryTfIdf(String query) {
        String[] queryWords = query.trim().split(" ");
        getTfIdf tfIdfCalculator = new getTfIdf();
        double[] queryVector = new double[queryTerms.size()];
        int index = 0;

        for (String term : queryTerms) {
            double tf = tfIdfCalculator.getTf(queryWords, term);
            double idf = idfMap.getOrDefault(term, 0.0);
            queryVector[index++] = tf * idf;
        }
        queryTfIdfVector.add(queryVector);
    }
}
