package mlearning.tree;

import datatools.DataSeries;
import datatools.DataSet;

import java.util.List;

record DataSplit(DataSet xLeft, DataSet xRight, DataSeries yLeft, DataSeries yRight) {

    /**
     * Rastavlja podatke u dva skupa prema zadanom pragu.
     * */
    public static DataSplit makeSplit(DataSet X, DataSeries y, double treshold, String feature) {

        List<String> features = List.of(X.getLabels().toArray(new String[0]));

        DataSet xLeft = new DataSet(X.getLabels());
        DataSet xRight = new DataSet(X.getLabels());
        DataSeries yLeft = new DataSeries(y.getLabel());
        DataSeries yRight = new DataSeries(y.getLabel());

        for(int i = 0; i < X.getShape().rows(); i++) {

            DataSeries row = X.getRow(i);

            int featureIndex = features.indexOf(feature);

            if (Double.parseDouble(row.get(featureIndex).toString()) <= treshold) {
                //System.out.println("Row " + row + " added left");
                for (int j = 0; j < row.count(); j++) {
                    Object value = row.get(j);
                    String label = features.get(j);
                    xLeft.getColumn(label).add(value);
                }
                yLeft.add(y.get(i));
            }
            else {
                //System.out.println("Row " + row + " added right");
                for (int j = 0; j < row.count(); j++) {
                    Object value = row.get(j);
                    String label = features.get(j);
                    xRight.getColumn(label).add(value);
                }
                yRight.add(y.get(i));
            }

        }
        return new DataSplit(xLeft, xRight, yLeft, yRight);
    }

}