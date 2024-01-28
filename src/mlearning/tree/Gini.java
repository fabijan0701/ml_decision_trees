package mlearning.tree;

import datatools.DataOps;
import datatools.DataSeries;
import datatools.DataSet;

import java.util.ArrayList;

/**
 * Predstavlja uređene parove pri računanju najoptimalnijeg
 * gini indeksa. Pamti podatke o pragu na kojemu je pronađen
 * svaki gini indeks, oznaci podatka (feature) i sam gini indeks
 * najboljeg praga.
 * */
record Gini(String feature, double treshold, double coefficient) {

    /**
     * Metoda koja služi za računanje Gini indeksa na nekom
     * skupu podataka. */
    public static double calculate(DataSeries dataSeries) {

        double gini = 1;

        for (Integer f: dataSeries.frequencies().values()) {
            double localP = (double)f / dataSeries.count();
            gini -= Math.pow(localP, 2);
        }

        return gini;
    }

    /**
     * Pronalazi gini indeks za rastavljene podatke po zadanom
     * pragu (eng. treshold). */
    static Gini giniOfSplit(DataSeries series, DataSeries y, double treshold) {

        DataSeries below = y.getAll(series.indicesWhere(x -> DataOps.toDouble(x) < treshold));
        DataSeries above = y.getAll(series.indicesWhere(x -> DataOps.toDouble(x) > treshold));

        int belowCount = below.count();
        int aboveCount = above.count();
        int total = belowCount + aboveCount;

        double belowPart = (double) belowCount / total;
        double abovePart = 1 - belowPart;
        double gini = belowPart * calculate(below) + abovePart * calculate(above);

        return new Gini(series.getLabel(), treshold, gini);
    }


    /**
     * Traži pragove (tresholds) za zadane podatke (podaci moraju biti numerički)
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

    /**
     * Traži optimalni (minimizirani) gini indeks u zadanom trenutku. */
    public static Gini minimized(DataSet X, DataSeries y) {

        // Postavljamo minimum.
        Gini min = new Gini("", 0, Double.MAX_VALUE);

        // Pretražujemo sve značajke (features).
        for (String dataLabel : X.getLabels()) {

            // Dohvaćamo svaki stupac (feature) DataSet-a.
            DataSeries column = X.getColumn(dataLabel);

            // Pragovi (za podjelu).
            Double[] tresholds = findTresholds(column);

            // Za svaki treshold.
            for (double treshold: tresholds) {

                Gini d = Gini.giniOfSplit(column, y, treshold);

                if (d.coefficient() < min.coefficient()) {
                    min = d;
                }
            }
        }

        // Vraćamo podatak sa najmanjim gini indeksom.
        return min;
    }
}
