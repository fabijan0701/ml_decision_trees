package datatools;

import java.util.HashMap;

public interface DescriptiveStatistics {

    /**
     * Provjerava jesu li svi elementi zadanog skupa podataka
     * kategoričkog tipa. Ako jesu vraća true inače vraća false.
     * */
    boolean isCategorical();

    /**
     * Provjerava jesu li svi elementi zadanog skupa podataka
     * numeričkog tipa. Ako jesu vraća true inače vraća false.
     * */
    boolean isNumerical();

    /**
     * Računa sumu svih elemenata zadanog skupa podataka.
     * @throws ArithmeticException ukoliko svi elementi skupa nisu numerički podaci
     * */
    double sum() throws ArithmeticException;

    /**
     * Računa aritmetičku sredinu svih elemenata zadanog skupa podataka.
     * @throws ArithmeticException ukoliko svi elementi skupa nisu numerički podaci
     * */
    double mean() throws ArithmeticException;

    /**
     * Računa medijan svih elemenata zadanog skupa podataka.
     * @throws ArithmeticException ukoliko svi elementi skupa nisu numerički podaci
     * */
    double median() throws ArithmeticException;

    /**
     * Računa modalnu vrijednost svih elemenata zadanog skupa podataka.
     * @throws ArithmeticException ukoliko svi elementi skupa nisu numerički podaci
     * */
    double mode() throws ArithmeticException;

    /**
     * Traži najmanji element zadanog skupa podataka.
     * @throws ArithmeticException ukoliko svi elementi skupa nisu numerički podaci
     * */
    double min() throws ArithmeticException;

    /**
     * Traži najveći zadanog skupa podataka.
     * @throws ArithmeticException ukoliko svi elementi skupa nisu numerički podaci
     * */
    double max() throws ArithmeticException;

    /**
     * Računa raspon podataka zadanog skupa.
     * @throws ArithmeticException ukoliko svi elementi skupa nisu numerički podaci
     * */
    double range() throws ArithmeticException;

    /**
     * Vraća broj elemenata zadanog skupa podataka.
     * @throws ArithmeticException ukoliko svi elementi skupa nisu numerički podaci
     * */
    int count() throws ArithmeticException;

    /**
     * Računa vrijednost od koje je točno 1/4 ostalih vrijednosti manja,
     * a 3/4 ostalih vrijednosti veće.
     * @throws ArithmeticException ukoliko svi elementi skupa nisu numerički podaci
     * */
    double firstQuartile() throws ArithmeticException;

    /**
     * Računa vrijednost od koje je točno 3/4 ostalih vrijednosti manja,
     * a 1/4 ostalih vrijednosti veće.
     * @throws ArithmeticException ukoliko svi elementi skupa nisu numerički podaci
     * */
    double thirdQuartile() throws ArithmeticException;

    /**
     * Računa varijancu nad danim skupom podataka.
     * @throws ArithmeticException ukoliko svi elementi skupa nisu numerički podaci
     */
    double variance() throws ArithmeticException;

    /**
     * Računa standardnu devijaciju nad danim skupom podataka.
     * @throws ArithmeticException ukoliko svi elementi skupa nisu numerički podaci
     */
    double standardDeviation() throws ArithmeticException;

    /**
     * Vraća frekvencije svakog od elemenata zadanog skupa.
     * Radi samo na numeričkim podacima.*/
    HashMap<Double, Integer> frequencies();

    /**
     * Računa mjeru kovarijance sa nekim drugim skupom podataka.*/
    double covariance(DataSeries other);

    /**
     * Računa korelaciju sa nekim drugim skupom podataka. */
    double correlation(DataSeries other);
}
