package textclassifier2.classifier;

import org.slf4j.Logger;
import textclassifier2.CharacteristicUtils;
import textclassifier2.model.Characteristic;
import textclassifier2.model.ClassifiableFactory;
import textclassifier2.model.VocabularyWord;
import textclassifier2.ngram.NGramStrategy;
import textclassifier2.ngram.VocabularyBuilder;
import textclassifier2.testdata.ExcelFileReader;
import textclassifier2.testdata.TestDataReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// BUILDER + COMPOSITE
public final class ClassifierBuilder {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ClassifierBuilder.class);

    private final TestDataReader reader;

    private final ClassifiableFactory textFactory;

    private final List<ClassifierUnitProxy> classifierUnits = new ArrayList<>();

    private final int AMOUNT_OF_TEXTS_FOR_CHECKING = 5;

    public ClassifierBuilder(TestDataReader reader, ClassifiableFactory textFactory) {

        if (reader == null || textFactory == null)
            throw new IllegalArgumentException();

        this.reader = reader;
        this.textFactory = textFactory;
    }

    // CONSTRUCTORS

    public static ClassifierBuilder fromExcel(File file, ClassifiableFactory factory) {
        return new ClassifierBuilder(new ExcelFileReader(file, 1, factory), factory);
    }

    public static ClassifierBuilder fromReader(TestDataReader reader, ClassifiableFactory factory) {
        return new ClassifierBuilder(reader, factory);
    }

    // CLIENT SECTION

    public ClassifierBuilder addNeroClassifierUnit(String characteristicName, NGramStrategy nGramStrategy) {
        addNeroClassifierUnit(null, characteristicName, null, nGramStrategy);
        return this;
    }

    public ClassifierBuilder addNeroClassifierUnit(File trainedClassifier, String characteristicName, List<VocabularyWord> vocabulary, NGramStrategy nGramStrategy) {
        classifierUnits.add(
                new ClassifierUnitProxy(
                        NeroClassifierUnit::new,
                        trainedClassifier,
                        nGramStrategy,
                        vocabulary,
                        textFactory.newCharacteristic(characteristicName)
                ));
        return this;
    }

    public Classifier build() throws IOException {
        if (!initialized()) {
            throw new IllegalArgumentException("Error. No classifier units were specified!");
        }
        List<ClassifierUnit> units = buildClassifiers();
        shutDownClassifiers(units);
        return new Classifier(units);
    }

    // INNER SECTION

    private List<ClassifierUnit> buildClassifiers() throws IOException {

        TestDataReader.ClassifiableData data = reader.readAll();

        Set<Characteristic> characteristics = data.getCharacteristics();

        List<ClassifierUnit> units = new ArrayList<>();
        for (ClassifierUnitProxy proxy : classifierUnits) {

            proxy.setVocabulary(new VocabularyBuilder(proxy.getNGramStrategy()).getVocabulary(
                data.getClassifiableTexts(), textFactory));

            proxy.setCharacteristic(
                    CharacteristicUtils.findByValue(
                            characteristics,
                            proxy.getCharacteristic().getName(),
                            textFactory::newCharacteristic)
            );

            ClassifierUnit unit = proxy.get();

            unit.build(data.getClassifiableTexts());

            units.add(unit);
        }

        return units;
    }

    private boolean initialized() {
        return !(reader == null || classifierUnits.size() == 0);
    }

    private void shutDownClassifiers(List<ClassifierUnit> units) {
        for (ClassifierUnit classifier : units) {
            classifier.shutdown();
        }
    }

    class ClassifierUnitProxy {
        private final ClassifierUnitSupplier supplier;
        private final File trainedClassifier;
        private final NGramStrategy nGramStrategy;
        private List<VocabularyWord> vocabulary;
        private Characteristic characteristic;

        public ClassifierUnitProxy(ClassifierUnitSupplier supplier, File trainedClassifier, NGramStrategy nGramStrategy) {
            this.supplier = supplier;
            this.trainedClassifier = trainedClassifier;
            this.nGramStrategy = nGramStrategy;
        }

        public ClassifierUnitProxy(ClassifierUnitSupplier supplier, File trainedClassifier, NGramStrategy nGramStrategy, List<VocabularyWord> vocabulary, Characteristic characteristic) {
            this.supplier = supplier;
            this.trainedClassifier = trainedClassifier;
            this.nGramStrategy = nGramStrategy;
            this.vocabulary = vocabulary;
            this.characteristic = characteristic;
        }

        public ClassifierUnit get() {
            return supplier.get(trainedClassifier, characteristic, vocabulary, nGramStrategy);
        }

        public File getTrainedClassifier() {return this.trainedClassifier;}

        public NGramStrategy getNGramStrategy() {return this.nGramStrategy;}

        public List<VocabularyWord> getVocabulary() {return this.vocabulary;}

        public Characteristic getCharacteristic() {return this.characteristic;}

        public void setVocabulary(List<VocabularyWord> vocabulary) {this.vocabulary = vocabulary; }

        public void setCharacteristic(Characteristic characteristic) {this.characteristic = characteristic; }

    }

    @FunctionalInterface
    interface ClassifierUnitSupplier {
        ClassifierUnit get(File trainedClassifier, Characteristic characteristic, List<VocabularyWord> vocabulary, NGramStrategy nGramStrategy);
    }
}

