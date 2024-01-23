package datatools;

import java.util.Arrays;
import java.util.Comparator;

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
}
