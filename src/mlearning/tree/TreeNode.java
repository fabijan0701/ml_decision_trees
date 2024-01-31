package mlearning.tree;

import datatools.DataSeries;
import datatools.DataSet;

public class TreeNode {

    public final String featureName;
    public final double treshold;
    public final double index;
    public final DataSet X;
    public final DataSeries y;
    private TreeNode left;
    private TreeNode right;

    public TreeNode(String feature, DataSet X, DataSeries y, double treshold, double index) {
        this.X = X;
        this.y = y;
        this.featureName = feature;
        this.treshold = treshold;
        this.index = index;
        this.left = null;
        this.right = null;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }
}
