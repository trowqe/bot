package textclassifier2.classifier;

import org.slf4j.Logger;
import textclassifier2.model.Characteristic;
import textclassifier2.model.CharacteristicValue;
import textclassifier2.model.ClassifiableText;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of composite pattern that aggregates one or several classifier units and handles all client requests
 * to classify texts. You should not explicitly create instances of <this class. Instead prefer using
 * {@link ClassifierBuilder} to construct Classifier.
 *
 * @author Ripreal
 */
public final class Classifier {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Classifier.class);
    private final List<ClassifierUnit> classifierUnits;

    @java.beans.ConstructorProperties({"classifierUnits"})
    public Classifier(List<ClassifierUnit> classifierUnits) {
        this.classifierUnits = classifierUnits;
    }

    /**
     *  Rebuilds all classifier units containing in this classifier based on source.
     *
     *  @param texts {@link ClassifiableText} source of texts to build classifier untis from.
     */
    public void build(List<ClassifiableText> texts) {
        classifierUnits.forEach((item) -> item.build(texts));
    }

    /**
     *  Stops all jobs that some classifier units might be doing. It can be helpful in case needed to prevent redundant
     *  server memory space consumption. Classifier does not shutdown its own ClassifierUnits. It simply
     *  delegates this task to them.
     */
    public void shutdown() {
        classifierUnits.forEach(ClassifierUnit::shutdown);
    }

    /**
     * Performs text classification according to each {@link ClassifierUnit} and returns list of
     * {@link CharacteristicValue} describing passed text. Make sure that Classifer units are ready for
     * classification and build them if the are not. Otherwise empty or not completed list will be returned.
     *
     * @param classifiableText - text you want to classify
     * @return {@link List} based on classification with classifier units. Can be empty.
     */
    public List<CharacteristicValue> classify( ClassifiableText classifiableText) {
        List<CharacteristicValue> values = new ArrayList<>();
        classifierUnits.forEach(unit -> {
            unit.classify(classifiableText).map(values::add);
        });
        return values;
    }

    /**
     * Saves current state of every {@link ClassifierUnit} into separate files. Saved classifer units
     * can be loaded from files into new Classifier via {@link ClassifierBuilder}.
     *
     * @param dir {@link File} in which classifier will store its classifier units state.
     */
    public void saveClassifiers( File dir) {
        if (!dir.isDirectory())
            throw new IllegalArgumentException("need directory not a file!");

        for (ClassifierUnit classifier : classifierUnits) {
            classifier.saveClassifier(dir);
        }
    }

    /**
     * Saves current state of every {@link ClassifierUnit} into separate files. Saved classifer units
     * can be loaded from files into new Classifier via {@link ClassifierBuilder}.
     *
     * @param stream {@link OutputStream} stream classifier will store its classifier units state to
     */
    public void saveClassifiers(OutputStream stream) {
        for (ClassifierUnit classifier : classifierUnits) {
            classifier.saveClassifier(stream);
        }
    }

    /**
     * Check to see if the classifier units are ready to classify texts. You must pass correctly classified texts to
     *  know classifier accurancy. Accuracy persentage outputs for every {@link ClassifierUnit} and dispatches to
     *  listeners.
     *
     * @param textForTesting - correctly classified texts for testing on each {@link ClassifierUnit}
     */
    public void checkClassifiersAccuracy(List<ClassifiableText> textForTesting) {

        for (ClassifierUnit unit : classifierUnits) {
            Characteristic characteristic = unit.getCharacteristic();
            int correctlyClassified = 0;

            for (ClassifiableText classifiableText : textForTesting) {
                CharacteristicValue idealValue = classifiableText.getCharacteristicValue(characteristic.getName());
                Optional<CharacteristicValue> classifiedValue = unit.classify(classifiableText);

                if (classifiedValue.isPresent() && classifiedValue.get().getValue().equals(idealValue.getValue())) {
                    correctlyClassified++;
                }
            }

            double accuracy =((double) correctlyClassified / textForTesting.size()) * 100;

            log.info(String.format("Accuracy of Classifier for '" + characteristic.getName()
                    + "' characteristic: %.2f%%", accuracy));
        }
    }

}
