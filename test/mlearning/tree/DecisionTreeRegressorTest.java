package mlearning.tree;

import datatools.DataOps;
import datatools.DataSeries;
import datatools.DataSet;
import files.TestFiles;
import mlearning.ModelMetrics;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class DecisionTreeRegressorTest {

    @Test
    public void mse() {

        DataSeries x = new DataSeries("X", new Object[] { 2, 4, 6, 8, 10 });
        DataSeries y = new DataSeries("Y", new Object[] { 10, 15, 18, 25, 30 });

        DecisionTreeRegressor reg = new DecisionTreeRegressor(0,0);
        Minimizer m = reg.valueOfSplit(x, y, 5);
        System.out.println("Total: " + m.index);
    }

    @Test
    void test2() throws IOException {

        DataSet ds = new DataSet();
        ds.fromCSV(TestFiles.SALARY_FILE, ",");

        DataSeries salary = ds.getColumn("Salary");
        ds.dropColumn("Salary");

        DecisionTreeRegressor reg = new DecisionTreeRegressor(8, 2);
        reg.fit(ds, salary);
    }

    @Test
    void test_players() throws IOException {

        // Odabir podataka koji će biti učitani
        HashSet<String> filters = new HashSet<>();
        filters.add("age");
        filters.add("price");
        filters.add("selections");
        filters.add("league");
        filters.add("goal_champ");
        filters.add("assist_champ");

        // Učitavanje skupa podataka.
        DataSet dataSet = new DataSet();
        dataSet.fromCSV(TestFiles.FOOTBALL_FILE, ";", filters);

        // Kodiranje kategoričkih vrijednosti.
        for (String label: dataSet.getLabels()) {
            DataSeries column = dataSet.getColumn(label);
            if (column.isCategorical()) {
                column.codeValues(column.autoCoding());
            }
        }

        // Atribut kojega predviđamo.
        String feature = "price";

        // Razdvajamo podatke u skupove za treniranje i testiranje.
        DataSet[] setSplit = DataOps.splitData(dataSet, 0.3, 2);

        // Skup za treniranje.
        DataSet Xtrain = setSplit[0];
        DataSeries yTrain = Xtrain.getColumn(feature);
        Xtrain.dropColumn(feature);

        // Skup za testiranje.
        DataSet Xtest = setSplit[1];
        DataSeries yTest = Xtest.getColumn(feature);
        Xtest.dropColumn(feature);

        // Stvaranje i testiranje regresijskog modela.
        DecisionTreeRegressor model = new DecisionTreeRegressor(5, 3);
        model.fit(Xtrain, yTrain);

        // Predviđene vrijednosti modela.
        DataSeries yPred = model.predict(Xtest);

        // Rezultati validacije modela.
        System.out.println("MAE: " + ModelMetrics.meanAbsoluteError(yTest, yPred));
        System.out.println("MSE: " + ModelMetrics.meanSquaredError(yTest, yPred));
        System.out.println("RMSE: " + ModelMetrics.rootMeanSquaredError(yTest, yPred));
    }

}