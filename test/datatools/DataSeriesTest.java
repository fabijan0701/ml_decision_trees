package datatools;

import files.TestFiles;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

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

        // Podaci koji se spremaju u seriju.
        Object[] obj = { "Anna", "Simon", "Theo" };

        // Stvaranje serije podataka.
        DataSeries testSeries = new DataSeries(obj);

        // Odabir indeksa prema kriteriju.
        Integer[] indices = testSeries.indicesWhere(x -> x.toString().length() == 5);

        /*
        * Stvaranje nove serije koja sadrži sve elemente iz
        * originalne serije podataka koji se nalaze na
        * odabranim indeksima.
        * */
        DataSeries filtered = testSeries.getAll(indices);



        System.out.println(filtered);
    }

    @Test
    void descriptive2() {

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

        String PLAYERS_FILE = TestFiles.PLAYERS_FILE;

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

    @Test
    void addColumnTest() {

        // Columns
        DataSeries colName = new DataSeries(new Object[] { "Anna", "Marco", "Simon" } );
        DataSeries colAge = new DataSeries(new Object[] { 20, 20, 20 } );

        // DataSet
        DataSet ds = new DataSet();
        ds.addColumn("Name", colName);
        ds.addColumn("Age", colAge);

        Assertions.assertEquals(ds.getShape(), new DataShape(3, 2));
    }

    @Test
    void categoricalNumerical() {

        // Test data.
        DataSeries dsNum = new DataSeries( new Object[] { 12, 13, 14 });
        DataSeries dsCat = new DataSeries( new Object[] { "Anna", "Marco" });

        // Assertions
        Assertions.assertAll(()-> {
            Assertions.assertTrue(dsNum.isNumerical());
            Assertions.assertTrue(dsCat.isCategorical());
        });
    }

    @Test
    void where() {

        // Actual.
        DataSeries series = new DataSeries(new Object[] { "Jure" , "Mate", "Jure", "Ana"});
        Integer[] result = series.indicesWhere(x -> x == "Jure");

        // Expected.
        Integer[] indices = { 0, 2 };

        // Assertions.
        Assertions.assertArrayEquals(indices, result);
    }

    @Test
    void extractNumerical() throws IOException {

        // Getting data.
        DataSet dataSet = new DataSet();
        dataSet.fromCSV(TestFiles.PLAYERS_FILE, ";");

        int expectedNumValues = 11;
        Assertions.assertEquals(expectedNumValues, dataSet.numerical().getShape().columns());
    }

    @Test
    void uniqueValues() {

        // Actual
        DataSeries dataSeries = new DataSeries(new Object[]{ 21, 22, 22, 21, 21, 22} );

        // Expected
        DataSeries expected = new DataSeries(new Object[]{ 21, 22 });

        // Asserts
        Assertions.assertEquals(expected, dataSeries.unique());
    }

    @Test
    void codingValues() {

        DataSeries ds = new DataSeries(new Object[]{ "Jure", "Mate", "Šime", "Mate", "Jure" });
        DataSeries expected = new DataSeries(new Object[]{ 1, 2, 3, 2, 1 });

        HashMap<Object, Object> coding = new HashMap<>();
        coding.put("Jure", 1);
        coding.put("Mate", 2);
        coding.put("Šime", 3);
        ds.codeValues(coding);

        Assertions.assertEquals(expected, ds);
    }

    @Test
    void sorted() {

        DataSeries ds = new DataSeries(new Object[] { 4, 2, 3, 1 });
        DataSeries dsSorted = new DataSeries(new Object[] { 1, 2, 3, 4 });
        Assertions.assertEquals(ds.sorted(), dsSorted);
    }

    @Test
    void getAll() {
        DataSeries x = new DataSeries(new Object[] { 1, 1, 0, 1, 0, 0, 0, 0, 1 });
        Integer[] indices = { 2, 3, 4 };

        DataSeries filtered = x.getAll(indices);

        System.out.println(filtered);
    }

    @Test
    void corrMatrix() throws IOException {

        // Getting data.
        DataSet dataSet = new DataSet();
        dataSet.fromCSV(TestFiles.PLAYERS_FILE, ";");

        // Executing method.
        double[][] corrMatrix = dataSet.corrMatrix();

        // Expected values.
        int expectedLength = dataSet.numerical().getShape().columns();

        // Assertion.
        Assertions.assertEquals(corrMatrix.length, expectedLength);
    }
}