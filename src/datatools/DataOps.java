package datatools;

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
    private static boolean isInt(String s) {
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
    private static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
