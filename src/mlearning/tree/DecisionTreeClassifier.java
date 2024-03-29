package mlearning.tree;

import datatools.DataOps;
import datatools.DataSeries;

public final class DecisionTreeClassifier extends DecisionTree{

    /**
     * Konstruktor koji prima podatke koji prikazuju maksimalnu moguću
     * dubinu stabla (maxDepth) i minimalnu količinu podataka koji se mogu
     * nalaziti u rastavljenom čvoru (minSamplesSplit).
     * */
    public DecisionTreeClassifier(int maxDepth, int minSamplesSplit) {
        super(maxDepth, minSamplesSplit);
    }


    /**
     * Metoda koja služi za računanje Gini indeksa na nekom
     * skupu podataka. */
    public static double gini(DataSeries dataSeries) {

        double gini = 1;

        for (Integer f: dataSeries.frequencies().values()) {
            double localP = (double)f / dataSeries.count();
            gini -= Math.pow(localP, 2);
        }

        return gini;
    }


    @Override
    public Minimizer valueOfSplit(DataSeries series, DataSeries y, double treshold) {

        DataSeries below = y.getAll(series.indicesWhere(x -> DataOps.toDouble(x) < treshold));
        DataSeries above = y.getAll(series.indicesWhere(x -> DataOps.toDouble(x) > treshold));

        int belowCount = below.count();
        int aboveCount = above.count();
        int total = belowCount + aboveCount;

        double belowPart = (double) belowCount / total;
        double abovePart = 1 - belowPart;
        double gini = belowPart * gini(below) + abovePart * gini(above);

        return new Minimizer(series.getLabel(), treshold, gini);
    }


    @Override
    public double predictOne(DataSeries x, String[] labels) {

        TreeNode leaf = this.findNode(x, labels);
        return leaf.y.mode();
    }
}
