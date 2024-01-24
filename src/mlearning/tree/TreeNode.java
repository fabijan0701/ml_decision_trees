package mlearning.tree;

public record TreeNode(
        String featureLabel,
        double treshold,
        double infoGain,
        TreeNode left,
        TreeNode right,
        Object value) { }
