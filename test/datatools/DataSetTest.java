package datatools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


class DataSetTest {

    // Project root.
    private static final String root = System.getProperty("user.dir");

    // Test data path.
    private static final String PENGUINS_FILE = root + "\\data\\penguins_size.csv";
    private static final String PLAYERS_FILE = root + "\\data\\top_players.csv";


    @Test
    @DisplayName("Loading file 'penguins_size.csv' test.")
    void dsFromCSV_penguins() {

        DataSet dataSet = new DataSet();

        try {
            dataSet.fromCSV(PENGUINS_FILE, ",");
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
        }

        // All labels that DataSet is expected to have after reading from file.
        String[] labels = {
                "species",
                "island",
                "culmen_length_mm",
                "culmen_depth_mm",
                "flipper_length_mm",
                "body_mass_g",
                "sex"
        };

        // Converting them to set.
        Set<String> expectedLabels = new HashSet<>(Arrays.stream(labels).toList());

        // Row with index zero.
        Object[] zeroIndexRow = { "Adelie", "Torgersen", 39.1, 18.7, 181, 3750, "MALE" };

        // Assertions.
        Assertions.assertAll(() -> {
            Assertions.assertEquals(expectedLabels, dataSet.getLabels());
            Assertions.assertEquals(new HashSet<>(List.of(zeroIndexRow)), new HashSet<>(List.of(dataSet.getRow(0))));
            Assertions.assertEquals(dataSet.getShape().rows(), dataSet.getColumn("species").size());
            Assertions.assertEquals(dataSet.getShape().columns(), dataSet.getLabels().size());
        });
    }

    @Test
    @DisplayName("Loading file '.csv' test.")
    void dsFromCSV_imdb() {
        DataSet dataSet = new DataSet();

        HashSet<String> filter = new HashSet<>();
        filter.add("Name");
        filter.add("Country");
        filter.add("Assists");
        filter.add("Goals");
        filter.add("Markey Value In Millions(£)");

        try {
            dataSet.fromCSV(PLAYERS_FILE, ";"/*, filter*/);
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
        }

        System.out.println(dataSet.getLabels());
        for (int i = 0; i < 10; i++) {
            System.out.println(Arrays.toString(dataSet.getRow(i)));
        }
    }

}