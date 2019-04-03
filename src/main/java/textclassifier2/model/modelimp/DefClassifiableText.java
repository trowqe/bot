package textclassifier2.model.modelimp;

import textclassifier2.model.CharacteristicValue;
import textclassifier2.model.ClassifiableText;

import java.util.Set;

public class DefClassifiableText implements ClassifiableText {

    private final String id;
    private String text;
    private Set<CharacteristicValue> characteristics;

    @java.beans.ConstructorProperties({"id", "text", "characteristics"})
    public DefClassifiableText(String id, String text, Set<CharacteristicValue> characteristics) {
        this.id = id;
        this.text = text;
        this.characteristics = characteristics;
    }

    @Override
    public CharacteristicValue getCharacteristicValue(String characteristicName) {
        //todo: check and make appropriate handler for missing charactericivValue via Optional
        return characteristics.stream()
            .filter(value -> value.getCharacteristic().equals(new DefCharacteristic(characteristicName)))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("characteristic value not exists!"));
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof DefClassifiableText) && this.text.equals(((DefClassifiableText) o).getText()) && this.characteristics.equals(((DefClassifiableText) o).getCharacteristics());
    }

    @Override
    public int hashCode() {
        return this.text.hashCode();
    }

    public String getId() {return this.id;}

    public String getText() {return this.text;}

    public Set<CharacteristicValue> getCharacteristics() {return this.characteristics;}

    public void setText(String text) {this.text = text; }

    public void setCharacteristics(Set<CharacteristicValue> characteristics) {this.characteristics = characteristics; }

    public String toString() {return "DefClassifiableText(id=" + this.getId() + ", text=" + this.getText() + ", characteristics=" + this.getCharacteristics() + ")";}
}
