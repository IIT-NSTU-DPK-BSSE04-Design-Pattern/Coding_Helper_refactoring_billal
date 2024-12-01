package code_clone;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TfIdfCalculator {

    private final List<String[]> project1Words = new ArrayList<>();
    private final List<String[]> project2Words = new ArrayList<>();
    private final Map<String, Double> idfMap = new HashMap<>();
    private final Set<String> combinedTerms = new HashSet<>();
    private final List<String> allFilesContent = new ArrayList<>();

    public void processFiles(String project1Path, String project2Path) throws IOException {
        loadUniqueTerms(project1Path, project1Words);
        loadUniqueTerms(project2Path, project2Words);

        calculateIdf();
    }

    private void loadUniqueTerms(String projectPath, List<String[]> projectWords) throws IOException {
        File directory = new File(projectPath);
        for (File file : directory.listFiles()) {
            if (file.getName().endsWith(".txt")) {
                String fileContent = readFileContent(file);
                projectWords.add(fileContent.split(" "));
                allFilesContent.add(fileContent);
                combinedTerms.addAll(Arrays.asList(fileContent.split(" ")));
            }
        }
    }

    private String readFileContent(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(" ");
            }
        }
        return content.toString().trim();
    }

    private void calculateIdf() {
        for (String term : combinedTerms) {
            double idf = new getTfIdf().getIdf(allFilesContent, term);
            idfMap.put(term, idf);
        }
    }

    public List<double[]> calculateTfIdfVectors(List<String[]> projectWords) {
        List<double[]> tfidfVectors = new ArrayList<>();
        for (String[] words : projectWords) {
            double[] tfidfVector = new double[combinedTerms.size()];
            int index = 0;
            for (String term : combinedTerms) {
                double tf = new getTfIdf().getTf(words, term);
                double idf = idfMap.getOrDefault(term, 0.0);
                tfidfVector[index++] = tf * idf;
            }
            tfidfVectors.add(tfidfVector);
        }
        return tfidfVectors;
    }
}