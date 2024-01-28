package mlearning.tree;

import datatools.DataOps;
import datatools.DataSeries;
import datatools.DataSet;
import datatools.DataShape;

import files.TestFiles;
import mlearning.ModelMetrics;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;


class DecisionTreeClassifierTest
{
    @Test
    void giniIndex() {

        // Dataset
        DataSeries series = new DataSeries(new Object[] { 0, 1, 1, 1, 0, 0, 1, 0, 1, 1 } );

        // Actual gini index (from method).
        double actual = Gini.calculate(series);

        // Expected (calculated) gini index.
        double expected = 0.48;

        // Assertion
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void zeroGini() {

        // Dataset
        DataSeries series = new DataSeries(new Object[] { 1, 1, 1, 1 } );

        // Actual gini index (from method).
        double actual = Gini.calculate(series);

        // Expected (calculated) gini index.
        double expected = 0;

        // Assertion
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void optimalGini() {

        DataSet X = new DataSet();
        X.addColumn("cijena", new DataSeries(new Object[] { 230, 460, 500, 430, 560, 1000, 600, 700, 630 }));
        X.addColumn("marka", new DataSeries(new Object[] { 2, 3, 4, 3, 2, 2, 3, 4, 1 }));
        X.addColumn("kupiti", new DataSeries(new Object[] { 1, 1, 0, 1, 0, 0, 0, 0, 0 }));

        DataSeries y = X.getColumn("kupiti");
        X.dropColumn("kupiti");

        Gini data = Gini.minimized(X, y);

        System.out.println("Minimum gini: " + data.coefficient());
        System.out.println("Treshold: " + data.treshold());
        System.out.println("Label: " + data.feature());
    }

    @Test
    void minimized() throws IOException {

        // Odabir podataka koji će biti učitani.
        HashSet<String> filter = new HashSet<>();
        filter.add("age");
        filter.add("internet");
        filter.add("school");
        filter.add("activities");
        filter.add("G3");
        filter.add("romantic");

        // Učitavanje DataSet-a.
        DataSet dataSet = new DataSet();
        dataSet.fromCSV(TestFiles.STUDENTS_FILE, ";", filter);

        // Kodiranje kategoričkih vrijednosti.
        DataSet cat = dataSet.categorical();
        System.out.println("Kategoričke vrijednosti: " + cat.getLabels());
        for (String feature: cat.getLabels()) {
            DataSeries column = dataSet.getColumn(feature);
            column.codeValues(column.autoCoding());
        }
        System.out.println("Broj kategoričkih vrijednosti nakon kodiranja: " + dataSet.categorical().getShape());
        System.out.println("Broj numeričkih vrijednosti nakon kodiranja: " + dataSet.numerical().getShape());

        // Odvajanje zavisnih i nezavisnih varijabli.
        String yLabel = "romantic";
        DataSeries y = dataSet.getColumn(yLabel);
        dataSet.dropColumn(yLabel);

        Gini data = Gini.minimized(dataSet, y);

        System.out.println("Minimum gini: " + data.coefficient());
        System.out.println("Treshold: " + data.treshold());
        System.out.println("Label: " + data.feature());

    }

    @Test
    void splitGiniTest() throws IOException {

        DataSet dataSet = new DataSet();
        dataSet.fromCSV(TestFiles.DIABETES_FILE, ";");

        DataSeries y = dataSet.getColumn("Outcome");
        dataSet.dropColumn(y.getLabel());

        System.out.println(Gini.minimized(dataSet, y));
    }

    @Test
    void split() {

        // Test data - X set.
        DataSet X = new DataSet();
        X.addColumn("cijena", new DataSeries(new Object[] { 230, 460, 500, 430, 560, 1000, 600, 700, 630 }));
        X.addColumn("marka", new DataSeries(new Object[] { 2, 3, 4, 3, 2, 2, 3, 4, 1 }));
        X.addColumn("kupiti", new DataSeries(new Object[] { 1, 1, 0, 1, 0, 0, 0, 0, 0 }));

        // Test data - y set.
        DataSeries y = X.getColumn("kupiti");
        X.dropColumn("kupiti");

        // Actual data.
        DataSplit splitted = DataSplit.makeSplit(X, y, 3.0, "marka");

        // Expected.
        DataShape xTrainLeft = new DataShape(7, 2);
        DataShape xTrainRight = new DataShape(2, 2);
        int yTrainLeft = 7;
        int yTrainRight = 2;

        // Assertions.
        Assertions.assertAll(() -> {
            Assertions.assertEquals(xTrainLeft, splitted.xLeft().getShape());
            Assertions.assertEquals(xTrainRight, splitted.xRight().getShape());
            Assertions.assertEquals(yTrainLeft, splitted.yLeft().count());
            Assertions.assertEquals(yTrainRight, splitted.yRight().count());
        });
    }

    @Test
    void fit() {

        DataSet X = new DataSet();
        X.addColumn("cijena", new DataSeries(new Object[] { 230, 460, 500, 430, 560, 1000, 600, 700, 630 }));
        X.addColumn("marka", new DataSeries(new Object[] { 2, 3, 4, 3, 2, 2, 3, 4, 1 }));
        X.addColumn("kupiti", new DataSeries(new Object[] { 1, 1, 0, 1, 0, 0, 0, 0, 0 }));

        DataSeries y = X.getColumn("kupiti");
        X.dropColumn("kupiti");

        DecisionTreeClassifier treeClassifier = new DecisionTreeClassifier(3, 4);
        treeClassifier.fit(X, y);

        DataSeries predicted = treeClassifier.predict(X);
        System.out.println(ModelMetrics.accuracy(y, predicted));

    }

    @Test
    void fitCSV() throws IOException {

        // Učitavanje DataSet-a.
        DataSet dataSet = new DataSet();
        dataSet.fromCSV(TestFiles.DIABETES_FILE, ";");

        // Stupac koji predviđamo.
        String feature = "Outcome";

        // Nasumična podjela podataka.
        DataSet[] dataSets = DataOps.splitData(dataSet, 0.2);

        // Skup za treniranje modela.
        DataSet XTrain = dataSets[0];

        // Skup y za nadzirano treniranje modela.
        DataSeries yTrain = XTrain.getColumn(feature);
        XTrain.dropColumn(feature);

        // Skup za testiranje modela.
        DataSet XTest = dataSets[1];

        // Skup y za testiranje točnosti modela.
        DataSeries yTest = XTest.getColumn(feature);
        XTest.dropColumn(feature);

        // Stvaranje i treniranje modela.
        DecisionTreeClassifier model = new DecisionTreeClassifier(5, 20);
        model.fit(XTrain, yTrain);

        // Predviđanje vrijednosti.
        DataSeries predicted = model.predict(XTest);

        // Izračun točnosti.
        double acc = ModelMetrics.accuracy(yTest, predicted);
        System.out.println("Accuracy: " + acc);
    }
}