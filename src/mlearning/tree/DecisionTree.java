package mlearning.tree;

import datatools.DataSeries;
import datatools.DataSet;

import java.util.*;

public abstract class DecisionTree {

    private TreeNode root;
    public final int maxDepth;
    public final int minSamplesSplit;


    /**
     * Konstruktor koji prima podatke koji prikazuju maksimalnu moguću
     * dubinu stabla (maxDepth) i minimalnu količinu podataka koji se mogu
     * nalaziti u rastavljenom čvoru (minSamplesSplit).
     * */
    public DecisionTree(int maxDepth, int minSamplesSplit) {
        this.root = null;
        this.maxDepth = maxDepth;
        this.minSamplesSplit = minSamplesSplit;
    }


    /**
     * Dohvaća korijen stabla.*/
    public TreeNode getRoot() {
        return root;
    }


    /**
     * Postavlja zadani čvor kao korijen stabla.*/
    public void setRoot(TreeNode root) {
        this.root = root;
    }

    /**
     * Metoda traži čvor u stablu.
     * */
    protected TreeNode findNode(DataSeries x, String[] labels) {

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

        return temp;
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


    /**
     * Traži optimalni (minimizirani) indeks u zadanom trenutku. */
    public Minimizer minimized(DataSet X, DataSeries y) {

        // Postavljamo minimum.
        Minimizer min = new Minimizer("", 0, Double.MAX_VALUE);

        // Pretražujemo sve značajke (features).
        for (String dataLabel : X.getLabels()) {

            // Dohvaćamo svaki stupac (feature) DataSet-a.
            DataSeries column = X.getColumn(dataLabel);

            // Pragovi (za podjelu).
            Double[] tresholds = Minimizer.findTresholds(column);

            // Za svaki treshold.
            for (double treshold: tresholds) {

                // Računamo gini indeks trenutne podjele podataka
                Minimizer d = this.valueOfSplit(column, y, treshold);

                // Tražimo najmanji indeks u skupu podataka.
                if (d.index < min.index) {
                    min = d;
                }
            }
        }

        // Vraćamo podatak sa najmanjim indeksom.
        return min;
    }


    /**
     * Pronalazi indeks za rastavljene podatke po zadanom
     * pragu (eng. treshold). */
    public abstract Minimizer valueOfSplit(DataSeries series, DataSeries y, double treshold);



    /**
     * Metoda koja služi za nadzirano treniranje i izgradnju
     * stabla odluke.
     * */
    public void fit(DataSet xTrain, DataSeries yTrain) {

        // 'Granica' koja prati dodavanje čvorova.
        Stack<TreeNode> nodeStack = new Stack<>();

        // Izračun optimalnog indeksa za korijen.
        Minimizer gdRoot = this.minimized(xTrain, yTrain);

        // Postavljanje korijena stabla (prvog čvora odluke).
        this.setRoot(new TreeNode(gdRoot.feature, xTrain, yTrain, gdRoot.treshold, gdRoot.index));

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
            if (currentSamples >= this.minSamplesSplit && currentDepth < this.maxDepth && !tempNode.featureName.equals("")) {

                // Dijelimo podatke trenutnog čvora na dva dijela.
                DataSplit dataSplit = DataSplit.makeSplit(tempNode.X, tempNode.y, tempNode.treshold, tempNode.featureName);

                // Računamo gini podatke za oba dijela.
                Minimizer minLeft = this.minimized(dataSplit.xLeft(), dataSplit.yLeft());
                Minimizer minRight = this.minimized(dataSplit.xRight(), dataSplit.yRight());

                // Stvaramo čvorove (lijevi i desni).
                TreeNode leftNode = new TreeNode(minLeft.feature, dataSplit.xLeft(), dataSplit.yLeft(), minLeft.treshold, minLeft.index);
                TreeNode rightNode = new TreeNode(minRight.feature, dataSplit.xRight(), dataSplit.yRight(), minRight.treshold, minRight.index);

                // Dodajemo ih trenutnom.
                tempNode.setLeft(leftNode);
                tempNode.setRight(rightNode);

                // Dodajemo ih u granicu kako bi se postupak mogao nastaviti.
                nodeStack.push(leftNode);
                nodeStack.push(rightNode);
            }
        }
    }



    /**
     * Predviđanje jednog podatka - potrebno je implementirati način na koji
     * stablo predviđa jedan podatak na osnovu jedne serije podataka.*/
    public abstract double predictOne(DataSeries x, String[] labels);


    /**
     * Predviđa seriju podataka (y) na osnovu ulaznog skupa podataka.
     * */
    public DataSeries predict(DataSet X) {

        String[] features = X.getLabels().toArray(new String[0]);
        DataSeries y = new DataSeries();

        for (int i = 0; i < X.getShape().rows(); i++) {
            DataSeries row = X.getRow(i);
            double predValue = this.predictOne(row, features);
            y.add(predValue);
        }

        return y;
    }
}
