package datatools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Arrays;
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
    void dsFromCSV_players() throws IOException {


        // Stvaranje skupa podataka.
        DataSet dataSet = new DataSet();

        // Filteri za odabrane podatke.
        HashSet<String> filter = new HashSet<>();
        filter.add("Name");
        filter.add("Country");
        filter.add("Assists");
        filter.add("Goals");
        filter.add("Market Value");

        // Čitanje podataka iz datoteke.
        dataSet.fromCSV(TestFiles.PLAYERS_FILE, ";", filter);

        // Dohvaćanje vrijednosti.
        int samples = dataSet.getShape().rows();
        int features = dataSet.getShape().columns();

        // Novi DataSet koji sadrži samo numeričke vrijednosti
        // iz originalnog DataSet-a.
        DataSet numericalOnly = dataSet.numerical();


        int a = samples+features;
    }

}