package mlearning;

import datatools.DataOps;
import datatools.DataSeries;

public class ModelMetrics {

    public static double accuracy(DataSeries y, DataSeries yPredicted) {

        int successful = 0;

        for (int i = 0; i < y.count(); i++) {
            double d1 = DataOps.toDouble(y.get(i));
            double d2 = DataOps.toDouble(yPredicted.get(i));
            if (d1 == d2) {
                successful++;
            }
        }

        return ((double) successful) / ((double) y.count());
    }

    public static double meanAbsoluteError(DataSeries y, DataSeries yPredicted) {

        double total = 0;

        for (int i = 0; i < y.count(); i++) {
            double d1 = DataOps.toDouble(y.get(i));
            double d2 = DataOps.toDouble(yPredicted.get(i));
            total += Math.abs(d1 - d2);
        }
        return total / y.count();
    }

    public static double meanSquaredError(DataSeries y, DataSeries yPredicted) {

        double total = 0;

        for (int i = 0; i < y.count(); i++) {
            double d1 = DataOps.toDouble(y.get(i));
            double d2 = DataOps.toDouble(yPredicted.get(i));
            total += Math.pow((d1 - d2), 2);
        }
        return total / y.count();
    }

    public static double rootMeanSquaredError(DataSeries y, DataSeries yPredicted) {
        return Math.sqrt(meanAbsoluteError(y, yPredicted));
    }
}
