package mlearning;

import datatools.DataOps;
import datatools.DataSeries;

import javax.xml.crypto.Data;

public class ModelMetrics {

    public static double accuracy(DataSeries y, DataSeries yPredicted) {

        System.out.println("Y: " + y);
        System.out.println("Y pred: " + yPredicted);

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
}
