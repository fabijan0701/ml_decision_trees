package datatools;

import mlearning.ModelMetrics;
import mlearning.tree.DecisionTree;
import mlearning.tree.DecisionTreeRegressor;

import java.util.*;

/**
 * Klasa sa statičkim metodama koje služe za rukovanje tipovima podataka
 * */
public class DataOps {

    /**
     * Prima literal u obliku Stringa i pretvara ga u željeni tip podatka
     * (koji može biti String, bool, int, double). Vraća podatak pretvoren
     * u 'Object', jer se svi podaci u DataSet spremaju kao 'Object'.
     * */
    public static Object convert(String data, Class<?> dtype) {
        if (dtype == int.class) {
            return Integer.valueOf(data);
        } else if (dtype == double.class) {
            if (data.contains(",")) {
                data = data.replace(",", ".");
            }
            return Double.valueOf(data);
        } else if (dtype == boolean.class) {
            return Boolean.parseBoolean(data);
        } else {
            return data;
        }
    }

    /**
     * Prima literal u obliku Stringa i procjenjuje koji je to tip podatka,
     * odnosno vraća njegov tip podatka (koji može biti String, bool, int, double).
     * */
    public static Class<?> resolveType(String literal) {
        if (isInt(literal)) {
            return int.class;
        } else if (isDouble(literal)) {
            return double.class;
        } else if (literal.equalsIgnoreCase("true") || literal.equalsIgnoreCase("false")) {
            return boolean.class;
        } else {
            return String.class;
        }
    }

    /**
     * Provjerava može li se literal tipa 'String' parsirati u int.
     * */
    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /**
     * Provjerava može li se literal tipa 'String' parsirati u double.
     * */
    public static boolean isDouble(String s) {
        s = s.replace(',', '.');
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public static String corrMatrixToStr(DataSet ds) {

        StringBuilder txt = new StringBuilder();
        double[][] matrix = ds.corrMatrix();
        String[] labels = ds.numerical().getLabels().toArray(new String[0]);

        // Longest label.
        int lenLongest = 0;
        for (String lbl: labels) {
            if (lbl.length() > lenLongest) {
                lenLongest = lbl.length();
            }
        }
        lenLongest += 5;

        // Defining space.
        String space = String.format("%" + lenLongest + "s", "");

        // First row - columns
        for (String lbl: labels) {
            txt.append(space).append(lbl);
        }
        txt.append("\n");

        for (int i = 0; i < matrix.length; i++) {
            int diff = lenLongest - labels[i].length();
            txt.append(labels[i]).append(String.format("%" + diff + "s", ""));
            for (int j = 0; j < matrix.length; j++) {
                String strVal = String.format("%-15.2f", matrix[i][j]);
                if (matrix[i][j] >= 0) {
                    strVal = " " + strVal;
                }
                diff = labels[j].length() - strVal.length();
                txt.append(strVal).append(String.format("%" + (diff + lenLongest) + "s", ""));
            }
            txt.append("\n");
        }


        return txt.toString();
    }


    /**
     * Služi za podjelu podataka u skupove za treniranje i testiranje.
     * @param dataSet predstavlja skup podataka koji dijelimo.
     * @param testDataPrecentage udio skupa za testiranje u podacima.
     * @param randomSeed sjeme za nasumični broj, kako bi se uvijek dobivali isti rezultati.
     * */
    public static DataSet[] splitData(DataSet dataSet, double testDataPrecentage, int randomSeed) {

        String[] labels = dataSet.getLabels().toArray(new String[0]);

        DataSet train = new DataSet(dataSet.getLabels());
        DataSet test = new DataSet(dataSet.getLabels());

        List<Integer> indices = new ArrayList<>();
        for(int i = 0; i < dataSet.getShape().rows(); i++) indices.add(i);

        Random random = new Random(randomSeed);

        double done = 1 - ((double)indices.size() / dataSet.getShape().rows());
        while (done < testDataPrecentage) {

            int index = indices.get(random.nextInt(0, indices.size()));
            DataSeries series = dataSet.getRow(index);
            indices.remove((Integer) index);

            for (int i = 0; i < labels.length; i++) {
                String label = labels[i];
                Object value = series.get(i);
                test.getColumn(label).add(value);
            }

            done = 1 - ((double)indices.size() / dataSet.getShape().rows());
        }

        for (int index = 0; index < dataSet.getShape().rows(); index++) {

            if (indices.contains(index)) {

                DataSeries series = dataSet.getRow(index);

                for (int i = 0; i < labels.length; i++) {
                    String label = labels[i];
                    Object value = series.get(i);
                    train.getColumn(label).add(value);
                }
            }
        }

        return new DataSet[] { train, test };
    }

    /**
     * Služi za podjelu podataka u skupove za treniranje i testiranje.
     * @param dataSet predstavlja skup podataka koji dijelimo.
     * @param testDataPrecentage udio skupa za testiranje u podacima.
     * */
    public static DataSet[] splitData(DataSet dataSet, double testDataPrecentage) {

        String[] labels = dataSet.getLabels().toArray(new String[0]);

        DataSet train = new DataSet(dataSet.getLabels());
        DataSet test = new DataSet(dataSet.getLabels());

        List<Integer> indices = new ArrayList<>();
        for(int i = 0; i < dataSet.getShape().rows(); i++) indices.add(i);

        Random random = new Random();

        double done = 1 - ((double)indices.size() / dataSet.getShape().rows());
        while (done < testDataPrecentage) {

            int index = indices.get(random.nextInt(0, indices.size()));
            DataSeries series = dataSet.getRow(index);
            indices.remove((Integer) index);

            for (int i = 0; i < labels.length; i++) {
                String label = labels[i];
                Object value = series.get(i);
                test.getColumn(label).add(value);
            }

            done = 1 - ((double)indices.size() / dataSet.getShape().rows());
        }

        for (int index = 0; index < dataSet.getShape().rows(); index++) {

            if (indices.contains(index)) {

                DataSeries series = dataSet.getRow(index);

                for (int i = 0; i < labels.length; i++) {
                    String label = labels[i];
                    Object value = series.get(i);
                    train.getColumn(label).add(value);
                }
            }
        }

        return new DataSet[] { train, test };
    }


    public static Double toDouble(Object value) {
        return ((Number) value).doubleValue();
    }


}
