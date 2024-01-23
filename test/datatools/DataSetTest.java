package datatools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import files.TestFiles;


class DataSetTest {

    @Test
    @DisplayName("Loading file 'penguins_size.csv' test.")
    void dsFromCSV_penguins() {

        DataSet dataSet = new DataSet();

        try {
            dataSet.fromCSV(TestFiles.PENGUINS_FILE, ",");
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
        Set<String> expectedLabels = new HashSet<>(List.of(labels));

        // Row with index zero.
        Object[] zeroIndexRowArr = { "Adelie", "Torgersen", 39.1, 18.7, 181, 3750, "MALE" };
        DataSeries zeroIndexRow = new DataSeries(zeroIndexRowArr);

        // Assertions.
        Assertions.assertAll(() -> {
            Assertions.assertEquals(expectedLabels, dataSet.getLabels());
            Assertions.assertEquals(zeroIndexRow, dataSet.getRow(0));
            Assertions.assertEquals(dataSet.getShape().rows(), dataSet.getColumn("species").size());
            Assertions.assertEquals(dataSet.getShape().columns(), dataSet.getLabels().size());
        });
    }

    @Test
    @DisplayName("Loading file '.csv' test.")
    void dsFromCSV_players() {
        DataSet dataSet = new DataSet();

        HashSet<String> filter = new HashSet<>();
        filter.add("Name");
        filter.add("Country");
        filter.add("Assists");
        filter.add("Goals");
        filter.add("Markey Value In Millions(£)");

        try {
            dataSet.fromCSV(TestFiles.PLAYERS_FILE, ";", filter);
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
        }

        System.out.println(dataSet.getLabels());
        System.out.println(dataSet.getShape());
    }
}