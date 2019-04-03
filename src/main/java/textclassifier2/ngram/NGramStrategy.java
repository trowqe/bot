package textclassifier2.ngram;

import java.util.Set;

public interface NGramStrategy {

    enum NGRAM_TYPES {UNIGRAM, FILTERED_UNIGRAM, BIGRAM, FILTERED_BIGRAM}

    static NGramStrategy getNGramStrategy(NGRAM_TYPES type) {
        switch (type) {
            case UNIGRAM:
                return new Unigram(NGRAM_TYPES.UNIGRAM);
            case FILTERED_UNIGRAM:
                return new FilteredUnigram(NGRAM_TYPES.FILTERED_UNIGRAM);
            case BIGRAM:
                return new Bigram(new Unigram(NGRAM_TYPES.BIGRAM));
            case FILTERED_BIGRAM:
                return new Bigram(new FilteredUnigram(NGRAM_TYPES.FILTERED_BIGRAM));
            default:
                return null;
        }
    }

    Set<String> getNGram(String text);

    NGRAM_TYPES getNGramType();

}