package textclassifier2.testdata;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.slf4j.Logger;
import textclassifier2.CharacteristicUtils;
import textclassifier2.model.Characteristic;
import textclassifier2.model.CharacteristicValue;
import textclassifier2.model.ClassifiableFactory;
import textclassifier2.model.ClassifiableText;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExcelFileReader implements TestDataReader {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ExcelFileReader.class);
    private final File file;
    private final int sheetNumber;
    private final ClassifiableFactory textFactory;
    private boolean hasNext = true;

    public ExcelFileReader(File file, int sheetNumber, ClassifiableFactory textFactory) {
        this.file = file;
        this.sheetNumber = sheetNumber;
        this.textFactory = textFactory;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public ClassifiableData next() throws IOException {
        hasNext = false;
        List<ClassifiableText> texts = toClassifiableTexts();
        if (texts.size() > 0) {
            Set<Characteristic> characteristics = toCharacteristics(texts);
            Set<CharacteristicValue> charVals = toCharacteristicValues(texts);
            return new TestDataReader.ClassifiableData(texts, characteristics, charVals);
        }
        return TestDataReader.ClassifiableData.empty();
    }

    @Override
    public ClassifiableData readAll() throws IOException {
        return next();
    }

    @Override
    public void close() throws Exception {
    }

    public List<ClassifiableText> toClassifiableTexts() throws IOException {
        if (!file.exists() ||
                sheetNumber < 1) {
            throw new IOException(
                String.format("Excel file with path %s not exist or has wrong format!", file.getAbsolutePath()));
        }
        List<ClassifiableText> texts = new ArrayList<>();
        try (XSSFWorkbook excelFile = new XSSFWorkbook(new FileInputStream(file))) {
            XSSFSheet sheet = excelFile.getSheetAt(sheetNumber - 1);

            // at least two rows
            if (sheet.getLastRowNum() > 0) {
                texts = getClassifiableTexts(sheet);
            } else {
                log.info("Excel sheet (#" + sheetNumber + ") is empty");
            }
        } catch (IllegalArgumentException e) {
            throw new IOException("Excel sheet (#" + sheetNumber + ") is not found");
        }
        return texts;
    }

    public Set<Characteristic> toCharacteristics(List<ClassifiableText> texts) {
        return texts.stream()
                .flatMap(text -> text.getCharacteristics().stream())
                .map(CharacteristicValue::getCharacteristic)
                .distinct()
                .collect(Collectors.toSet());
    }

    private Set<CharacteristicValue> toCharacteristicValues(List<ClassifiableText> texts) {
        return texts.stream()
            .flatMap(text ->  text.getCharacteristics().stream())
            .collect(Collectors.toSet());
    }

    // WORK WITH SHEET

    private List<ClassifiableText> getClassifiableTexts(XSSFSheet sheet) {
        List<Characteristic> characteristics = getCharacteristics(sheet);
        List<ClassifiableText> classifiableTexts = new ArrayList<>();

        // start from second row
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Set<CharacteristicValue> characteristicsValues = getCharacteristicsValues(sheet.getRow(i), characteristics);

            // exclude empty rows
            if (!sheet.getRow(i).getCell(0).getStringCellValue().equals("")) {
                classifiableTexts.add(textFactory.newClassifiableText(sheet.getRow(i).getCell(0).getStringCellValue(), characteristicsValues));
            }
        }

        //todo: now it's error prone approach. Value's order and parent should be filled somewhere ese.
        for (Characteristic characteristic : characteristics) {
            int i = 1;
            for (CharacteristicValue characteristicValue : characteristic.getPossibleValues()) {
                characteristicValue.setOrderNumber(i++);
            }
        }

        return classifiableTexts;
    }

    private Set<CharacteristicValue> getCharacteristicsValues(Row row, List<Characteristic> characteristics) {
        Set<CharacteristicValue> characteristicsValues = new HashSet<>();

        for (int i = 1; i < row.getLastCellNum(); i++) {
            Characteristic characteristic = characteristics.get(i - 1);
            String valueName = row.getCell(i).getStringCellValue();

            CharacteristicValue value = CharacteristicUtils.findByValue(
                    characteristic.getPossibleValues(), valueName, (val) -> textFactory.newCharacteristicValue(val, 0, characteristic));
            if (value == null) {
                value = textFactory.newCharacteristicValue(valueName, 0, characteristic);
            }
            characteristic.addPossibleValue(value);
            characteristicsValues.add(value);
        }

        return characteristicsValues;
    }

    private List<Characteristic> getCharacteristics(XSSFSheet sheet) {

        List<Characteristic> characteristics = new ArrayList<>();

        // first row from second to last columns contains Characteristics names
        for (int i = 1; i < sheet.getRow(0).getLastCellNum(); i++) {
            characteristics.add(textFactory.newCharacteristic(sheet.getRow(0).getCell(i).getStringCellValue()));
        }

        return characteristics;
    }

}
