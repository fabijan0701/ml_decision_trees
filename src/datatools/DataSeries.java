package datatools;

import com.sun.jdi.Value;

import java.util.*;

public class DataSeries extends ArrayList<Object> implements DescriptiveStatistics {

    private String label;

    public DataSeries() {
        this.label = "";
    }

    public DataSeries(String label) {
        this.label = "";
    };

    public DataSeries(Object[] array) {
        this.addAll(List.of(array));
        this.label = "";
    }

    public DataSeries(String label, Object[] array) {
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DataSeries) {
            for (Object element : this) {
                if (!((DataSeries) o).contains(element)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean isCategorical() {
        for (Object val: this) {
            if (val instanceof String || val instanceof Boolean) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isNumerical() {
        for (Object val: this) {
            if (val instanceof Integer || val instanceof Double) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double sum() {

        double s = 0;

        for (Object element: this) {
            s += Double.parseDouble(element.toString());
        }

        return s;
    }

    @Override
    public double mean() {
        return this.sum() / this.count();
    }

    @Override
    public double median() {
        ArrayList<Double> doubles = new ArrayList<>();
        for (Object element: this) doubles.add(Double.parseDouble(element.toString()));
        Collections.sort(doubles);
        if (doubles.size() % 2 != 0) {
            return doubles.get(doubles.size() / 2);
        }
        int k = doubles.size() / 2;
        return (doubles.get(k) + doubles.get(k - 1)) / 2;
    }

    @Override
    public double mode() {

        HashMap<Double, Integer> counter = this.frequencies();

        int max = 0;
        Double mode = Double.MIN_VALUE;
        for (Double key: counter.keySet()) {
            if (counter.get(key) > max) {
                mode = key;
                max = counter.get(key);
            }
        }

        return mode;
    }

    @Override
    public double min()  {
        double minVal = Double.MAX_VALUE;
        for (Object elem : this) {
            double d = Double.parseDouble(elem.toString());
            if (d < minVal) {
                minVal = d;
            }
        }
        return minVal;
    }

    @Override
    public double max() {
        double maxValue = Double.MIN_VALUE;
        for (Object elem : this) {
            double d = Double.parseDouble(elem.toString());
            if (d > maxValue) {
                maxValue = d;
            }
        }
        return maxValue;
    }


    @Override
    public double range() {
        double max = this.max();
        double min = this.min();

        if (max >= 0 && min >= 0 || max <= 0 && min <= 0) {
            return Math.abs(Math.abs(max) - Math.abs(min));
        }
        else {
            return max + Math.abs(min);
        }
    }

    @Override
    public int count() {
        return this.size();
    }

    @Override
    public double firstQuartile() throws ArithmeticException {
        ArrayList<Double> doubles = new ArrayList<>();
        for (Object element: this) doubles.add(Double.parseDouble(element.toString()));
        Collections.sort(doubles);
        if (doubles.size() % 2 == 0) {
            int index1 = doubles.size() / 4;
            int index2 = (doubles.size() / 4) + 1;
            return (doubles.get(index1 - 1) + doubles.get(index2 - 1)) / 2;
        }
        else {
            int index = (doubles.size()) / 4;
            return doubles.get(index);
        }
    }

    @Override
    public double thirdQuartile() throws ArithmeticException {
        ArrayList<Double> doubles = new ArrayList<>();
        for (Object element: this) doubles.add(Double.parseDouble(element.toString()));
        Collections.sort(doubles);
        if (doubles.size() % 2 == 0) {
            int index1 = doubles.size() * 3 / 4;
            int index2 = (doubles.size() * 3 / 4) + 1;
            return (doubles.get(index1 - 1) + doubles.get(index2 - 1)) / 2;
        }
        else {
            int index = 3 * doubles.size() / 4;
            return doubles.get(index);
        }
    }

    @Override
    public double variance() throws ArithmeticException {

        double sum = 0;
        double m = this.mean();

        for (Object e: this) {
            double val = Double.parseDouble(e.toString());
            sum += Math.pow(val - m  , 2);
        }

        return sum / this.count();
    }

    @Override
    public double standardDeviation() throws ArithmeticException {
        return Math.sqrt(this.variance());
    }

    @Override
    public HashMap<Double, Integer> frequencies() {

        HashMap<Double, Integer> counter = new HashMap<>();

        for (Object element: this) {
            Double d = Double.parseDouble(element.toString());
            if (!counter.containsKey(d)) {
                counter.put(d, 1);
            } else {
                int value = counter.get(d);
                counter.replace(d, value + 1);
            }
        }

        return counter;
    }

    @Override
    public double covariance(DataSeries other) {
        double sum = 0;
        double m = this.mean();

        for (int i = 0; i < this.count(); i++) {
            double v1 = Double.parseDouble(this.get(i).toString());
            double v2 = Double.parseDouble(other.get(i).toString());
            sum += ((v1 - m) * (v2 - m));
        }

        return sum / this.count();
    }

    @Override
    public double correlation(DataSeries other) {
        return this.covariance(other) / (this.standardDeviation() * other.standardDeviation());
    }

    public void codeValues(HashMap<Object, Object> map) {

        /*for (int i = 0; i < this.size(); i++) {
            Object data = this.get(i);
            double mapValue = map.get(data);
            this.set(i, mapValue);
        }*/

        this.replaceAll(map::get);
    }
}
