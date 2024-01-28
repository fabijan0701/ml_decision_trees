package mlearning.tree;

import datatools.DataSeries;
import datatools.DataSet;

import java.util.*;

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
     * Računa dubinu stabla pomoću pretrage po širini (eng. Bredth First Search).
     * */
    public int depth() {

        if (this.getRoot() == null) {
            return 0; // Tree is empty
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(this.getRoot());
        int depth = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            // Process nodes at the current level
            for (int i = 0; i < levelSize; i++) {
                TreeNode currentNode = queue.poll();

                // Add children to the queue
                if (currentNode.getLeft() != null) {
                    queue.offer(currentNode.getRight());
                }
                if (currentNode.getLeft() != null) {
                    queue.offer(currentNode.getRight());
                }
            }

            depth++; // Move to the next level
        }

        return depth;
    }

    @Override
    public void fit(DataSet xTrain, DataSeries yTrain) {

        // 'Granica' koja prati dodavanje čvorova.
        Stack<TreeNode> nodeStack = new Stack<>();

        // Izračun optimalnog gini indeksa za korijen.
        Gini gdRoot = Gini.minimized(xTrain, yTrain);

        // Postavljanje korijena stabla (prvog čvora odluke).
        this.setRoot(new TreeNode(gdRoot.feature(), xTrain, yTrain, gdRoot.treshold(), gdRoot.coefficient()));

        // Dodajemo korijen u granicu.
        nodeStack.push(this.getRoot());

        // Dodavanje se odvija sve dok nešto postoji unutar granice.
        while (!nodeStack.isEmpty()) {

            // Dohvaćanje čvora iz granice.
            TreeNode tempNode = nodeStack.pop();

            // Dohvaćanje broja podataka.
            int currentSamples = tempNode.y.count();

            // Trenutna dubina stabla
            int currentDepth = this.depth();

            // Ako vrijede uvjeti razdvajanja...
            if (currentSamples >= this.minSamplesSplit && currentDepth <= this.maxDepth) {

                // Dijelimo podatke trenutnog čvora na dva dijela.
                DataSplit dataSplit = DataSplit.makeSplit(tempNode.X, tempNode.y, tempNode.treshold, tempNode.featureName);

                // Računamo gini podatke za oba dijela.
                Gini giniLeft = Gini.minimized(dataSplit.xLeft(), dataSplit.yLeft());
                Gini giniRight = Gini.minimized(dataSplit.xRight(), dataSplit.yRight());

                // Stvaramo čvorove (lijevi i desni).
                TreeNode leftNode = new TreeNode(giniLeft.feature(), dataSplit.xLeft(), dataSplit.yLeft(), giniLeft.treshold(), giniLeft.coefficient());
                TreeNode rightNode = new TreeNode(giniRight.feature(), dataSplit.xRight(), dataSplit.yRight(), giniRight.treshold(), giniRight.coefficient());

                // Dodajemo ih trenutnom.
                tempNode.setLeft(leftNode);
                tempNode.setRight(rightNode);

                // Dodajemo ih u granicu kako bi se postupak mogao nastaviti.
                nodeStack.push(leftNode);
                nodeStack.push(rightNode);
            }
        }
    }


    @Override
    public double predict(DataSeries x, String[] labels) {

        TreeNode temp = this.getRoot();

        double data;

        while (!temp.isLeaf()) {

            int featureIndex = List.of(labels).indexOf(temp.featureName);
            data = Double.parseDouble(x.get(featureIndex).toString());

            if (data <= temp.treshold) {
                temp = temp.getLeft();
            }
            else {
                temp = temp.getRight();
            }
        }

        return temp.y.mode();
    }


    @Override
    public DataSeries predict(DataSet X) {

        String[] features = X.getLabels().toArray(new String[0]);
        DataSeries y = new DataSeries();

        for (int i = 0; i < X.getShape().rows(); i++) {
            DataSeries row = X.getRow(i);
            double predValue = this.predict(row, features);
            y.add(predValue);
        }

        return y;
    }


    void bfs() {

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(this.getRoot());

        while (!queue.isEmpty()) {

            TreeNode tmp = queue.poll();

            System.out.println(tmp);
            System.out.println();

            if (tmp.getLeft() != null) queue.offer(tmp.getLeft());
            if (tmp.getRight() != null) queue.offer(tmp.getRight());
        }
    }
}
