import IO.Filewriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PreProcessing {

    public String processFile(String filename, String content, String outputPath) throws IOException {
        String cleanedContent = cleanContent(content);
        String stemmedContent = stemWords(cleanedContent);

        Filewriter writer = new Filewriter();
        return writer.createProcessFile(filename, stemmedContent.trim(), outputPath);
    }

    private String cleanContent(String content) throws IOException {
        String withoutPunctuation = removePunctuation(content);
        String withoutKeywords = removeKeywords(withoutPunctuation);
        return removeExtraSpaces(withoutKeywords);
    }

    private String removePunctuation(String text) {
        return text.replaceAll("\\p{Punct}", " ");
    }

    private String removeExtraSpaces(String text) {
        return text.trim().replace("\n", " ").replace("\r", "").replaceAll("\\s+", " ");
    }

    private String removeKeywords(String content) throws IOException {
        Set<String> keywords = loadKeywords();
        StringBuilder result = new StringBuilder();

        for (String word : content.split(" ")) {
            if (!keywords.contains(word.trim())) {
                result.append(word).append(" ");
            }
        }

        return result.toString().trim();
    }

    private Set<String> loadKeywords() throws IOException {
        Set<String> keywords = new HashSet<>();
        try (FileInputStream fis = new FileInputStream("H:\\2-1\\Coding_Helper\\keyword.java")) {
            byte[] bytes = fis.readAllBytes();
            for (String keyword : new String(bytes).trim().split(" ")) {
                keywords.add(keyword.trim());
            }
        }
        return keywords;
    }

    private String stemWords(String content) {
        Porter_stemmer stemmer = new Porter_stemmer();
        StringBuilder stemmedContent = new StringBuilder();

        for (String word : content.split(" ")) {
            stemmedContent.append(stemmer.stemWord(word)).append(" ");
        }

        return stemmedContent.toString().trim();
    }
}
