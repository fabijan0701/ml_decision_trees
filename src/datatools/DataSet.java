package datatools;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.*;

/**
 * Reprezentira DataSet, strukturu sličnu DataFrame bibilioteke Pandas (programskog
 * jezika Python). Struktura DataSet je organizirana na način da svaki stupac ima
 * svoju jedinstvenu oznaku (tipa String). Stupci su ne-generički, što znači da je
 * svaki element koji se nalazi u ovoj strukturi tipa 'Object' i potrebno ga je naknadno
 * cast-ati u željeni tip podatka. Prilikom čitanja podataka iz datoteke, automatski
 * se prepoznaje tip podatka koji će biti spremljen u određeni stupac. Mogući tipovi
 * podataka koji mogu biti učitani su 'int', 'double', 'boolean' i 'String'.
 * */
public class DataSet {


    /**
     * Mapa u koju su spremljeni podaci koje struktura sadrži. Ključevi su oznake
     * (eng. labels), a vrijednosti su liste tipa ArrayList<> koje predstavljaju
     * stupce koji su spremljeni u strukturu.
     * */
    private final HashMap<String, ArrayList<Object>> map;


    /**
     * Mapa u koju su spremljeni tipovi podataka za svaki stupac. Ključevi su
     * oznake stupaca (eng. labels), a vrijednosti su tipovi podataka koji su
     * spremljeni u tom stupcu. Naravno, svi spremljeni podaci su tipa 'Object',
     * pa ova mapa služi kako bismo mogli naknadno pretvoriti podatke u željeni
     * tip podataka.
     * */
    public HashMap<String, Class<?>> dataTypes;


    /**
     * Zadani i jedini konstruktor klase 'DataSet'. Ne prima nikakve argumente
     * i inicijalizira polja ove klase na zadane vrijednosti.
     * */
    public DataSet() {
        map = new HashMap<>();
        dataTypes = new HashMap<>();
    }


    /**
     * Dohvaća sve oznake koje se nalaze u DataSet-u. Dohvaćene oznake se
     * spremaju u polje Stringova koje metoda vraća.
     */
    public Set<String> getLabels() {
        return this.map.keySet();
    }


    /**
     * U bilo kojem trenutku vraća oblik DataSet-a u obliku instance
     * jednostavne 'record' klase naziva 'DataShape'.
     */
    public DataShape getShape() {
        if (this.map.isEmpty()) {
            return new DataShape(0, 0);
        }

        int columns = map.size();
        int rows = map.get(map.keySet().stream().toList().get(0)).size();

        return new DataShape(rows, columns);
    }


    /**
     * Vraća stupac prema željenoj oznaci (label) u obliku strukture
     * tipa 'ArrayList<>'.
     **/
    public ArrayList<Object> getColumn(String label) {
        return this.map.get(label);
    }


    /**
     * Vraća željeni redak prema njegovom indeksu. Indeksi se kreću
     * od 0 do N - 1, ako je N broj redaka. Dobiveni rezultat se vraća
     * u obliku polja koje sadrži elemente tipa 'Object'. */
    public Object[] getRow(int index) {

        Object[] row = new Object[this.getShape().columns()];

        String[] labels = this.getLabels().toArray(new String[0]);

        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];
            row[i] = this.map.get(label).get(index);
        }

        return row;
    }


    /**
     * Vraća novi DataSet koji uključuje prvih n redaka originalnog DataSet-a.
     * @param n predstavlja broj redaka u novom DataSet-u.
     * */
    public DataSet head(int n) {

        DataSet data = new DataSet();
        data.dataTypes = this.dataTypes;

        for (String label: map.keySet()) {
            data.map.put(label, new ArrayList<>());
            ArrayList<Object> column = this.map.get(label);
            System.out.println(label + "  " + column.size());
        }

        return  data;
    }


    /**
     * Učitava DataSet iz CSV (eng. 'Comma Separated Values') datoteke.
     * Potrebno je da struktura datoteke bude organizirana na način da prvi
     * redak datoteke sadrži oznake ('labels') stupaca i da svaki sljedeći
     * redak sadrži podataka onoliko koliko je oznaka stupaca u prvom retku
     * datoteke. Potrebno je i da se za razdvajanje podataka koristi jedinstveni
     * simbol (preferira se ',' ili ';'). Prilikom učitavanja automatski se
     * prepoznaju podaci koji su spremljeni u svakom stupcu, iako su svi elementi
     * unutar strukture tipa 'Object'. To se radi u svrhu naknadnog pretvaranja
     * podataka.
     *
     * @param filePath putanja do datoteke koja sadrži podatke.
     * @param separator 'simbol' koji razdvaja podatke u datoteci, preferira se da bude ',' ili ';'.
     * @param filterColumns oznake stupaca koji će biti uključeni u DataFrame. Ako se ne navede učitavaju se svi podaci.
     * @throws IOException ukoliko dođe do problema prilikom učitavanja datoteke.*/
    public void fromCSV(String filePath, String separator, Set<String> filterColumns) throws IOException {

        // Objekt koji čita datoteku.
        BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8);

        // Varijabla u koju će se spremati svaka pročitana linija.
        String line;

        // Brojač linija
        int lineCounter = 0;

        // Dopušteni indeksi.
        ArrayList<Integer> validIndices = new ArrayList<>();

        // Sve oznake stupaca u datoteci.
        String[] allLabels = null;

        // Petljom prolazimo kroz datoteku.
        while ((line = reader.readLine()) != null) {

            // Podijelimo liniju u niz prema znaku koji razdvaja podatke
            String[] lineData = line.split(separator);

            if (lineCounter == 0) {    // učitavamo samo oznake.
                allLabels = lineData;
                for (int i = 0; i < lineData.length; i++) {
                    String title = lineData[i];
                    if (filterColumns.contains(title)) {
                        map.put(title, new ArrayList<>());
                        validIndices.add(i);
                    }
                }
            }
            else if (lineCounter == 1) {    // Uz podatke učitavamo i tipove podataka.
                for (int i = 0; i < lineData.length; i++) {
                    if (validIndices.contains(i)) {
                        String data = lineData[i];
                        Class<?> dataType = DataOps.resolveType(data);
                        this.dataTypes.put(allLabels[i], dataType);
                        Object convertedData = DataOps.convert(data, dataType);
                        map.get(allLabels[i]).add(convertedData);
                    }
                }
            }
            else {      // Učitavamo samo podatke.
                for (int i = 0; i < lineData.length; i++) {
                    if (validIndices.contains(i)) {
                        String data = lineData[i];
                        Class<?> dataType = DataOps.resolveType(data);
                        Object convertedData = DataOps.convert(data, dataType);
                        map.get(allLabels[i]).add(convertedData);
                    }
                }
            }

            lineCounter++;
        }

        reader.close();
    }


    /**
     * Učitava DataSet iz CSV (eng. 'Comma Separated Values') datoteke.
     * Potrebno je da struktura datoteke bude organizirana na način da prvi
     * redak datoteke sadrži oznake ('labels') stupaca i da svaki sljedeći
     * redak sadrži podataka onoliko koliko je oznaka stupaca u prvom retku
     * datoteke. Potrebno je i da se za razdvajanje podataka koristi jedinstveni
     * simbol (preferira se ',' ili ';'). Prilikom učitavanja automatski se
     * prepoznaju podaci koji su spremljeni u svakom stupcu, iako su svi elementi
     * unutar strukture tipa 'Object'. To se radi u svrhu naknadnog pretvaranja
     * podataka. Radi optimalnijeg korištenja memorije može se kao argument poslati
     * skup (HashSet) podataka koji su tipa 'String' i označavaju samo odabrane stupce
     * iz datoteke koji će biti učitani u DataSet.
     *
     * @param filePath putanja do datoteke koja sadrži podatke.
     * @param separator 'simbol' koji razdvaja podatke u datoteci, preferira se da bude ',' ili ';'.
     * @throws IOException ukoliko dođe do problema prilikom učitavanja datoteke.*/
    public void fromCSV(String filePath, String separator) throws IOException {

        // Objekt koji čita datoteku.
        BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8);

        // Varijabla u koju će se spremati svaka pročitana linija.
        String line;

        // Brojač linija
        int lineCounter = 0;

        String[] allLabels = null;

        // Petljom prolazimo kroz datoteku.
        while ((line = reader.readLine()) != null) {

            // Podijelimo liniju u niz prema znaku koji razdvaja podatke
            String[] lineData = line.split(separator);

            if (lineCounter == 0) {
                allLabels = lineData;
                for (String title : lineData) {
                    map.put(title, new ArrayList<>());
                }
            }
            else if (lineCounter == 1) {
                for (int i = 0; i < lineData.length; i++) {
                    String data = lineData[i];
                    Class<?> dataType = DataOps.resolveType(data);
                    this.dataTypes.put(allLabels[i], dataType);
                    Object convertedData = DataOps.convert(data, dataType);
                    map.get(allLabels[i]).add(convertedData);
                }
            }
            else {
                for (int i = 0; i < lineData.length; i++) {
                    String data = lineData[i];
                    Class<?> dataType = DataOps.resolveType(data);
                    Object convertedData = DataOps.convert(data, dataType);
                    map.get(allLabels[i]).add(convertedData);
                }
            }

            lineCounter++;
        }

        reader.close();
    }
}
