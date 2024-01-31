package mlearning.tree;

import datatools.DataOps;
import datatools.DataSeries;

public class DecisionTreeRegressor extends DecisionTree{

    /**
     * Konstruktor koji prima podatke koji prikazuju maksimalnu moguću
     * dubinu stabla (maxDepth) i minimalnu količinu podataka koji se mogu
     * nalaziti u rastavljenom čvoru (minSamplesSplit).
     *
     * @param maxDepth maksimalna dubina stabla.
     * @param minSamplesSplit minimalni broj podataka koji se može rastaviti.
     */
    public DecisionTreeRegressor(int maxDepth, int minSamplesSplit) {
        super(maxDepth, minSamplesSplit);
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

        double mse = belowPart * below.variance() + abovePart * above.variance();

        return new Minimizer(series.getLabel(), treshold, mse);
    }


    @Override
    public double predictOne(DataSeries x, String[] labels) {

        TreeNode leaf = this.findNode(x, labels);
        return leaf.y.mean();
    }
}
