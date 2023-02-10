import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BooleanSearchEngine implements SearchEngine {
    Map<String, List<PageEntry>> searchResults;


    public BooleanSearchEngine(File pdfsDir) throws IOException {
        searchResults = new TreeMap<>();
        for (File file : pdfsDir.listFiles()) {
            proccesFile(file);
        }

    }

    private void proccesFile(File file) throws IOException {
        var doc = new PdfDocument(new PdfReader(file));
        for (int i = 1; i < doc.getNumberOfPages(); i++) {
            Map<String, Integer> freqs = new HashMap<>();
            String[] text = PdfTextExtractor
                    .getTextFromPage(doc.getPage(i))
                    .split("\\P{IsAlphabetic}+");
            for (String word : text) {
                if (word.isEmpty()) {
                    continue;
                }
                word = word.toLowerCase();
                freqs.put(word, freqs.getOrDefault(word, 0) + 1);
            }
            for (String entry : freqs.keySet()) {
                List<PageEntry> temp = new ArrayList<>();
                temp.add(new PageEntry(file.getName(), i, freqs.get(entry)));
                searchResults.merge(entry, temp, (old, added) -> {
                    old.addAll(added);
                    Collections.sort(old, Collections.reverseOrder());
                    return old;
                });

            }

        }
    }


    @Override
    public List<PageEntry> search(String word) {
        return searchResults.getOrDefault(word, Collections.emptyList());
    }
}
