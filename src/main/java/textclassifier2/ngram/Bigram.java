package textclassifier2.ngram;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

// decorator
class Bigram implements NGramStrategy {

    private final NGRAM_TYPES ngramType;
    private final NGramStrategy nGramStrategy;

    Bigram(NGramStrategy nGramStrategy) {
        this.nGramStrategy = nGramStrategy;
        this.ngramType = nGramStrategy.getNGramType();
    }

    @Override
    public Set<String> getNGram(String text) {
        List<String> unigram = new ArrayList<>(nGramStrategy.getNGram(text));

        // concatenate words to bigrams
        // example: "How are you doing?" => {"how are", "are you", "you doing"}

        Set<String> uniqueValues = new LinkedHashSet<>();

        for (int i = 0; i < unigram.size() - 1; i++) {
            uniqueValues.add(unigram.get(i) + " " + unigram.get(i + 1));
        }

        return uniqueValues;
    }

    @Override
    public NGRAM_TYPES getNGramType() {
        return ngramType;
    }
}
