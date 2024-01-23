package datatools;

import files.TestFiles;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DataOpsTest {

    @Test
    void convert() {

        // Actual test values.
        String testStr = "anna";
        String testInt = "100";
        String testBool = "true";
        String testDouble = "21.2";

        // Expected values.
        Object expectedStr = "anna";
        Object expectedInt = 100;
        Object expectedBool = true;
        Object expectedDouble = 21.2;

        // Assertions.
        Assertions.assertAll(() -> {
            Assertions.assertEquals(expectedStr, DataOps.convert(testStr, String.class));
            Assertions.assertEquals(expectedInt, DataOps.convert(testInt, int.class));
            Assertions.assertEquals(expectedBool, DataOps.convert(testBool, boolean.class));
            Assertions.assertEquals(expectedDouble, DataOps.convert(testDouble, double.class));
        });
    }

    @Test
    void resolveType() {
        Assertions.assertAll(() -> {
            Assertions.assertEquals(DataOps.resolveType("Anna"), String.class);
            Assertions.assertEquals(DataOps.resolveType("1"), int.class);
            Assertions.assertEquals(DataOps.resolveType("20.2"), double.class);
            Assertions.assertEquals(DataOps.resolveType("true"), boolean.class);
        });
    }

    @Test
    void printMatrix() throws IOException {

        // Filtrianje potrebnih podataka.
        Set<String> filters = new HashSet<>();
        filters.add("Position");
        filters.add("Country");
        filters.add("Goals");
        filters.add("Assists");
        filters.add("Market Value");

        // Učitavanje podataka.
        DataSet data = new DataSet();
        data.fromCSV(TestFiles.PLAYERS_FILE, ";", filters);

        int codePos = 0;
        HashMap<Object, Object> positionMapping = new HashMap<>();
        for (Object value: data.getColumn("Position").unique()) {
            positionMapping.put(value, codePos);
            codePos++;
        }
        data.getColumn("Position").codeValues(positionMapping);

        int country = 0;
        HashMap<Object, Object> countryMapping = new HashMap<>();
        for (Object o: data.getColumn("Country").unique()) {
            countryMapping.put(o, country);
            country++;
        }
        data.getColumn("Country").codeValues(countryMapping);

        System.out.println(DataOps.corrMatrixToStr(data));
    }
}