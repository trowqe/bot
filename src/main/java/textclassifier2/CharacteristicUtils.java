package textclassifier2;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class CharacteristicUtils {
    @SuppressWarnings("unchecked")
    public static <T> T findByValue(Collection<T> collection, String value, Function<String, T> supplier) {
        List<T> available = Arrays.asList((T[]) collection.toArray());
        try {
            return available.get(available.indexOf(supplier.apply(value)));
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            return null;
        }
    }
}
