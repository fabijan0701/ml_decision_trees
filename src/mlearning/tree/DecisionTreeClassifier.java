package mlearning.tree;

import datatools.DataSeries;
import datatools.DataSet;

import java.util.*;

public final class DecisionTreeClassifier extends DecisionTree{

    /**
     * Predstavlja uređene parove pri računanju najoptimalnijeg
     * gini indeksa. Pamti podatke o pragu na kojemu je pronađen
     * svaki gini indeks, oznaci podatka (label) i sam gini indeks
     * najboljeg praga.
     * */
    private record GiniData(
            String label,
            double treshold,
            double gini,
            DataSeries leftData,
            DataSeries rightData
    ) {}


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

    /**
     * Metoda koja vraća polje pragova koji služe za podjelu
     * podatka u lijevi i desni skup. Zadani podaci se promatraju
     * u sortiranom poretku. Zatim se za svaka dva jedinstvena
     * elementa računa njihova prosječna vrijednost i sprema
     * kao prag.*/
    static double[] findTresholds(DataSeries dataSeries) {

        // Sortirani niz.
        Object[] arr = dataSeries.toArray();
        Arrays.sort(arr);

        // Podaci koje će metoda vratiti.
        ArrayList<Double> tresholds = new ArrayList<>();

        // Izračun srednjih vrijednosti.
        for (int i = 0; i < arr.length - 1; i++) {
            double data = Double.parseDouble(arr[i].toString());
            double next = Double.parseDouble(arr[i + 1].toString());
            double treshold = (data + next) / 2;
            if (!tresholds.contains(treshold)) {
                tresholds.add(treshold);
            }
        }

        double[] result = new double[tresholds.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = tresholds.get(i);
        }
        return result;
    }

    /**
     * Rastavlja podatke u dva skupa prema zadanom pragu.
     * */
    private static DataSeries[] splitData(DataSeries series, double treshold) {

        DataSeries left = new DataSeries();
        DataSeries right = new DataSeries();

        for (Object data: series) {
            if (Double.parseDouble(data.toString()) <= treshold) {
                left.add(data);
            }
            else {
                right.add(data);
            }
        }

        return new DataSeries[] { left, right };
    }


    public static GiniData optimalGini(DataSet dataSet) {

        // Tu ćemo spremati sve podatke.
        HashMap<Double, GiniData> map = new HashMap<>();

        // Pretražujemo sve značajke (features).
        for (String dataLabel: dataSet.getLabels()) {

            // Dohvaćamo svaki stupac (feature) DataSet-a.
            DataSeries column = dataSet.getColumn(dataLabel);

            // Računamo pragove za stupac.
            double[] tresholds = findTresholds(column);

            // Tražimo najmanji gini za svaki stupac.
            for (double treshold: tresholds) {

                // Dijelimo podatke u lijevi i desni skup.
                DataSeries[] dataSplit = splitData(column, treshold);
                DataSeries left = dataSplit[0];
                DataSeries right = dataSplit[1];

                // Računamo udio svakog dijela.
                int nl = left.count() / column.count();
                int nr = right.count() / column.count();

                // Računamo gini.
                double gini = (double) nl * gini(left) + (double) nr * gini(right);

                // Spremamo podatke.
                map.put(gini, new GiniData(dataLabel, treshold, gini, left, right));
            }
        }

        // Traženje minimuma.
        double minGini = Collections.min(map.keySet());

        // Vraćamo podatak sa najmanjim gini indeksom.
        return map.get(minGini);
    }

    /**
     * Metoda koja služi za treniranje klasifikacijskog modela
     * stabla odluke. Trenira se rekurzivno.*/
    @Override
    public void fit(DataSet xTrain, DataSet yTrain) {

    }


    @Override
    public void predict(DataSeries x) {

    }
}
