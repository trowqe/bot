package textclassifier2.model;

public interface CharacteristicValue {

    String getId();

    void setId(String id);

    void setCharacteristic(Characteristic characteristic);

    Characteristic getCharacteristic();

    int getOrderNumber();

    void setOrderNumber(int orderNumber);

    String getValue();

}
