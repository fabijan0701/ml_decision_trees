package mlearning.tree;

import datatools.DataOps;
import datatools.DataSeries;
import datatools.DataSet;
import files.TestFiles;
import mlearning.ModelMetrics;
import org.junit.jupiter.api.Test;

import java.io.IOException;
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

        reg.bfs();
    }

    @Test
    void test_players() throws IOException {

        HashSet<String> filters = new HashSet<>();
        filters.add("Age");
        filters.add("Matches");
        filters.add("Goals");
        filters.add("Assists");
        filters.add("Market Value");

        DataSet X = new DataSet();
        X.fromCSV(TestFiles.PLAYERS_FILE, ";",filters);

        String feature = "Market Value";
        DataSeries y = X.getColumn(feature);
        X.dropColumn(feature);

        DecisionTreeRegressor model = new DecisionTreeRegressor(4, 2);
        model.fit(X, y);

        System.out.println(model.depth());


        DataSeries yPred = model.predict(X);
        System.out.println("MAE: " + ModelMetrics.meanAbsoluteError(y, yPred));
        System.out.println("MSE: " + ModelMetrics.meanSquaredError(y, yPred));
    }

}