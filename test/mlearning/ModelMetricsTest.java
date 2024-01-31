package mlearning;

import datatools.DataSeries;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModelMetricsTest {

    @Test
    void meanSquaredError() {

        DataSeries y = new DataSeries(new Object[]{2, 5, 8});
        DataSeries yPred = new DataSeries(new Object[]{3, 4, 7});
        double expectedMSE = 1.0;

        Assertions.assertEquals(expectedMSE, ModelMetrics.meanSquaredError(y, yPred));
    }

    @Test
    void meanAbsError() {

        DataSeries y = new DataSeries(new Object[]{2, 5, 8});
        DataSeries yPred = new DataSeries(new Object[]{3, 4, 7});
        double expectedMAE = 1.0;

        Assertions.assertEquals(expectedMAE, ModelMetrics.meanAbsoluteError(y, yPred));
    }
}