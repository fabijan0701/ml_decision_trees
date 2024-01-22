package datatools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.HashSet;


class DataSetTest {

    // Project root.
    private static final String root = System.getProperty("user.dir");

    // Test data path.
    private static final String testFile = root + "\\data\\penguins_size.csv";

    @ Test
    void type_check() {
        Assertions.assertEquals(DataSet.resolveType("Anna"), String.class);
        Assertions.assertEquals(DataSet.resolveType("1"), int.class);
        Assertions.assertEquals(DataSet.resolveType("20.2"), double.class);
        Assertions.assertEquals(DataSet.resolveType("true"), boolean.class);
    }


    @Test
    @DisplayName("Loading file test.")
    void dsFromCSV() {

        HashSet<String> columns = new HashSet<>();
        columns.add("species");
        columns.add("island");
        columns.add("sex");

        DataSet dataSet = new DataSet();
        try {
            dataSet.fromCSV(testFile, ",", columns);
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
        }

        System.out.println(dataSet.getShape());
    }



}