package datatools;

import java.util.HashMap;

public interface DescriptiveStatistics {

    boolean isCategorical();

    boolean isNumerical();

    double sum() throws ArithmeticException;

    double mean() throws ArithmeticException;

    double median() throws ArithmeticException;

    double mode() throws ArithmeticException;

    double min() throws ArithmeticException;

    double max() throws ArithmeticException;

    double range() throws ArithmeticException;

    int count() throws ArithmeticException;

    double firstQuartile() throws ArithmeticException;

    double thirdQuartile() throws ArithmeticException;

    double variance() throws ArithmeticException;

    double standardDeviation() throws ArithmeticException;

    HashMap<Double, Integer> frequencies();

    double covariance(DataSeries other);

    double correlation(DataSeries other);
}
