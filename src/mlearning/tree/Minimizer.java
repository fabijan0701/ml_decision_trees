package mlearning.tree;

import datatools.DataOps;
import datatools.DataSeries;

import java.util.ArrayList;

public class Minimizer {

    public final double treshold;
    public final String feature;
    public final double index;


    public Minimizer(String feature, double treshold, double index) {
        this.treshold = treshold;
        this.index = index;
        this.feature = feature;
    }

    /**
     * Traži pragove (tresholds) za zadane podatke. Svi podaci moraju
     * biti numeričkog tipa.
     * */
    static Double[] findTresholds(DataSeries dataSeries) {

        DataSeries sorted = dataSeries.sorted().unique();
        ArrayList<Double> tresholds = new ArrayList<>();

        for (int i = 0; i < sorted.count() - 1; i++) {

            Double temp = DataOps.toDouble(sorted.get(i));
            Double next = DataOps.toDouble(sorted.get(i + 1));
            Double mean = (temp + next) / 2;

            if (!tresholds.contains(mean)) {
                tresholds.add(mean);
            }
        }

        return tresholds.toArray(new Double[0]);
    }
}
