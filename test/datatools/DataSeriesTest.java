package datatools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class DataSeriesTest {

    @Test
    void addTest() {
        Object[] array = { 12, 12 ,"Anna", 14.4 };
        DataSeries series = new DataSeries(array);

        Assertions.assertArrayEquals(array, series.toArray());
    }

    @Test
    void isCategorical() {

        Object[] testArrCat = { "Anna", "Marco", "Simon", true };
        Object[] testArrNotCat = { 12, 123, 12.2, };

        DataSeries dsCat = new DataSeries(testArrCat);
        DataSeries dsNotCat = new DataSeries(testArrNotCat);

        Assertions.assertAll(() -> {
            Assertions.assertTrue(dsCat.isCategorical());
            Assertions.assertFalse(dsNotCat.isCategorical());
        });
    }

    @Test
    void isNumerical() {

        Object[] testArrNum = { 12, 123, 12.2, };
        Object[] testArrNotNum = { "Anna", "Marco", "Simon", true };

        DataSeries numerical = new DataSeries(testArrNum);
        DataSeries notNUmerical = new DataSeries(testArrNotNum);

        Assertions.assertAll(() -> {
            Assertions.assertTrue(numerical.isNumerical());
            Assertions.assertFalse(notNUmerical.isNumerical());
        });
    }

    @Test
    void descriptive() {

        // Test data.
        Object[] obj = { 21.2, 22.6, 21.9, 21, 21, 21.3 };
        // sorted -> 21, 21, 21.2, 21.3, 21.9, 22.6

        // Test Series.
        DataSeries testSeries = new DataSeries(obj);

        // Expected.
        double sum = 129;
        double mean = 21.5;
        double median = 21.25;
        double mode = 21;
        double min = 21;
        double max = 22.6;

        // Assertions.
        Assertions.assertAll(() -> {
            Assertions.assertEquals(sum, testSeries.sum());
            Assertions.assertEquals(mean, testSeries.mean());
            Assertions.assertEquals(median, testSeries.median());
            Assertions.assertEquals(mode, testSeries.mode());
            Assertions.assertEquals(min, testSeries.min());
            Assertions.assertEquals(max, testSeries.max());
        });
    }

    @Test
    void covarianceCorrelation() {

        String PLAYERS_FILE = System.getProperty("user.dir") + "\\data\\top_players.csv";

        DataSet dataSet = new DataSet();

        HashSet<String> filter = new HashSet<>();
        filter.add("Name");
        filter.add("Country");
        filter.add("Assists");
        filter.add("Goals");
        filter.add("Markey Value In Millions(£)");

        try {
            dataSet.fromCSV(PLAYERS_FILE, ";", filter);
        } catch (IOException e) {
            Assertions.fail(e.getMessage());
        }

        DataSeries goals = dataSet.getColumn("Goals");
        DataSeries values = dataSet.getColumn("Markey Value In Millions(£)");

        double corr = goals.correlation(values);
        System.out.println("Correlation: Goals - Value: " + corr);

    }
}