package textclassifier2.classifier;


import textclassifier2.model.Characteristic;
import textclassifier2.model.CharacteristicValue;
import textclassifier2.model.ClassifiableText;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;


public interface ClassifierUnit {

    // PROPERTIES

    Characteristic getCharacteristic();

    // BUILDING

    void build(List<ClassifiableText> classifiableTexts);

    // COMPOSITE METHODS

    public Optional<CharacteristicValue> classify(ClassifiableText classifiableText);

    public void saveClassifier(File file);

    public void saveClassifier(OutputStream stream);

    public void shutdown();

}
