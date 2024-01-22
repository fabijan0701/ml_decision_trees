package datatools;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.*;

public class DataSet {

    public HashMap<String, ArrayList<Object>> map;
    public HashMap<String, Class<?>> dataTypes;

    public DataSet() {
        map = new HashMap<>();
        dataTypes = new HashMap<>();
    }

    public String[] getLabels() {
        return this.map.keySet().toArray(new String[0]);
    }

    public Shape getShape() {
        if (this.map.isEmpty()) {
            return new Shape(0, 0);
        }

        int columns = map.size();
        int rows = map.get(map.keySet().stream().toList().get(0)).size();

        return new Shape(rows, columns);
    }

    public ArrayList<Object> getColumn(String label) {
        return this.map.get(label);
    }

    public Object[] getRow(int index) {

        Object[] row = new Object[this.getShape().columns()];

        for (int i = 0; i < this.getLabels().length; i++) {
            String label = this.getLabels()[i];
            row[i] = this.map.get(label).get(index);
        }

        return row;
    }

    public DataSet head(int n) {

        DataSet data = new DataSet();
        data.dataTypes = this.dataTypes;

        for (String label: map.keySet()) {
            data.map.put(label, new ArrayList<>());
            ArrayList<Object> column = this.map.get(label);
            System.out.println(label + "  " + column.size());
        }

        return  data;
    }

    public void fromCSV(String filePath, String separator, HashSet<String> filterColumns) throws IOException {

        // Objekt koji čita datoteku.
        BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8);

        // Varijabla u koju će se spremati svaka pročitana linija.
        String line;

        // Brojač linija
        int lineCounter = 0;

        // Dopušteni indeksi.
        ArrayList<Integer> validIndices = new ArrayList<>();

        String[] allTitles = null;

        // Petljom prolazimo kroz datoteku.
        while ((line = reader.readLine()) != null) {

            // Podijelimo liniju u niz prema znaku koji razdvaja podatke
            String[] lineData = line.split(separator);

            if (lineCounter == 0) {
                allTitles = lineData;
                for (int i = 0; i < lineData.length; i++) {
                    String title = lineData[i];
                    if (filterColumns.contains(title)) {
                        map.put(title, new ArrayList<>());
                        validIndices.add(i);
                    }
                }
            }
            else if (lineCounter == 1) {
                for (int i = 0; i < lineData.length; i++) {
                    if (validIndices.contains(i)) {
                        String data = lineData[i];
                        Class<?> dataType = resolveType(data);
                        this.dataTypes.put(allTitles[i], dataType);
                        Object convertedData = convert(data, dataType);
                        map.get(allTitles[i]).add(convertedData);
                    }
                }
            }
            else {
                for (int i = 0; i < lineData.length; i++) {
                    if (validIndices.contains(i)) {
                        String data = lineData[i];
                        Class<?> dataType = resolveType(data);
                        Object convertedData = convert(data, dataType);
                        map.get(allTitles[i]).add(convertedData);
                    }
                }
            }

            lineCounter++;
        }

        reader.close();
    }

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

    public static Class<?> resolveType(String literal) {
        if (isInteger(literal)) {
            return int.class;
        } else if (isDouble(literal)) {
            return double.class;
        } else if (literal.equalsIgnoreCase("true") || literal.equalsIgnoreCase("false")) {
            return boolean.class;
        } else {
            return String.class;
        }
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
