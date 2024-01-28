package mlearning.tree;

import datatools.DataSeries;
import datatools.DataSet;

public abstract class DecisionTree {

    private TreeNode root;
    public final int maxDepth;
    public final int minSamplesSplit;

    public DecisionTree(int maxDepth, int minSamplesSplit) {
        this.root = null;
        this.maxDepth = maxDepth;
        this.minSamplesSplit = minSamplesSplit;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public abstract void fit(DataSet xTrain, DataSeries yTrain);

    public abstract double predict(DataSeries x, String[] labels);

    public abstract DataSeries predict(DataSet X);
}
