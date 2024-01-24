package mlearning.tree;

import datatools.DataSeries;
import datatools.DataSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DecisionTreeClassifierTest {

    @Test
    void giniIndex() {

        // Dataset
        DataSeries series = new DataSeries(new Object[] { 0, 1, 1, 1, 0, 0, 1, 0, 1, 1 } );

        // Actual gini index (from method).
        double actual = DecisionTreeClassifier.gini(series);

        // Expected (calculated) gini index.
        double expected = 0.48;

        // Assertion
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findTresholds() {

        DataSeries series = new DataSeries(new Object[] { 10, 20, 20, 20, 20, 32, 50 });

        double[] tresholds = DecisionTreeClassifier.findTresholds(series);
        double[] expected = { 15, 20, 26, 41 };

        Assertions.assertArrayEquals(expected, tresholds);
    }

    @Test
    void optimalGini() {

    }
}