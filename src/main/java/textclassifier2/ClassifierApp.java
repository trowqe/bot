package textclassifier2;


import textclassifier2.classifier.Classifier;
import textclassifier2.classifier.ClassifierBuilder;
import textclassifier2.model.CharacteristicValue;
import textclassifier2.model.ClassifiableText;
import textclassifier2.model.modelimp.DefClassifiableFactory;
import textclassifier2.ngram.NGramStrategy;
import textclassifier2.testdata.ExcelFileReader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ClassifierApp {

    private final static String CONFIG_PATH = "./config/config.ini";
    private final static Config CONFIG = new Config(CONFIG_PATH);

    public static void main(String... args) throws IOException {

        Classifier classifier = ClassifierBuilder
                .fromExcel(new File(CONFIG.getTestDataPath()), new DefClassifiableFactory())
                //.addNeroClassifierUnit("Длительность", NGramStrategy.getNGramStrategy(NGramStrategy.NGRAM_TYPES.FILTERED_BIGRAM))
                .addNeroClassifierUnit("Результат", NGramStrategy.getNGramStrategy(NGramStrategy.NGRAM_TYPES.FILTERED_UNIGRAM))
                .build();

        ExcelFileReader reader = new ExcelFileReader(new File(CONFIG.getTestDataPath()), 1, new DefClassifiableFactory());
        ClassifiableText text = reader.toClassifiableTexts().get(0);
        List<CharacteristicValue> charact = classifier.classify(text);
        System.out.println(String.format("Classified text %s", text.getText()));
        System.out.println(String.format("As %s", charact));

    }

    static {
        if (!CONFIG.isLoaded()) {
            System.out.println(String.format(
                "Config file on %s is not found or it is empty.", CONFIG_PATH));
            System.exit(1);
        }
    }
}
