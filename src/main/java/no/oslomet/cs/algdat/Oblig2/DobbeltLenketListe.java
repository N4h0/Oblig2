package no.oslomet.cs.algdat.Oblig2;


////////////////// class DobbeltLenketListe //////////////////////////////


import java.util.*;


public class DobbeltLenketListe<T> implements Liste<T> { //....

    /**
     * Node class
     *
     * @param <T>
     */

/*    public static void main(String[] args) {  //for å teste ting.
    }

*/
    private static final class Node<T> {
        private T verdi;                   // nodens verdi
        private Node<T> forrige, neste;    // pekereØ

        private Node(T verdi, Node<T> forrige, Object o) {
            this.verdi = verdi;
            this.forrige = forrige;
            this.neste = neste;
        }

        private Node(T verdi) {
            this(verdi, null, null);
        }
    }

    // instansvariabler
    private Node<T> hode;          // peker til den første i listen
    private Node<T> hale;          // peker til den siste i listen
    private int antall;            // antall noder i listen
    private int endringer;         // antall endringer i listen

    public DobbeltLenketListe() { //Lager ei dobbellenka liste som er tom.
        hode = hale = null;
        antall = endringer = 0;
    }

    public DobbeltLenketListe(T[] a) {  //Lager ei dobbeltlenka liste med verdiene frå A, utan å ta med NULL-verdiar.

        Objects.requireNonNull(a, "Tabellen a er null!"); //Sjekker at a ikkje er null

        hode = hale = null;

        int i = 0;
        for (; i < a.length && a[i] == null; i++) ; //Finn inksen til den fyrste veriden i lista som ikkje er 0.

        if (i < a.length) {
            Node<T> p = hode = new Node<>(a[i]);  // den fyrste noden
            p.forrige = null;

            antall++;

            for (i++; i < a.length; i++) {
                if (a[i] != null) {
                    Node<T> q = p;
                    p = p.neste = new Node<>(a[i]);
                    p.forrige = q;
                    antall++;

                }
            }
            hale = p; //Sett halen
            hode.forrige = hale.neste = null;
        }
    }

    public Liste<T> subliste(int fra, int til) {

        fratilKontroll(fra, til); //Fratilkontroll :)
        Liste<T> liste = new DobbeltLenketListe<>(); //Tom liste :)

        if (fra == til) {
            return liste;  //Returnere tom liste om intervallet er 0 :)
        }

        Node<T> p = finnNode(fra); //Finn fyrste node!

        for (int i = fra; i < til; i++) { //Looper frå fyrste node til intervallengden.
            liste.leggInn(p.verdi);  //Legger inn neste verdi med legginnmetoden.
            p = p.neste;
        }
        return liste;
    }

    private void fratilKontroll(int fra, int til) {
        if (fra < 0 || til < 0 || til > antall) throw new IndexOutOfBoundsException();
        if (fra > til) throw new IllegalArgumentException();
    }

    @Override
    public int antall() {
        // throw new UnsupportedOperationException();
        return antall;
    }

    @Override
    public boolean tom() {
        // throw new UnsupportedOperationException();
        return antall == 0; //Returnerer 1 hvis antall = 0, returnerer 0 hvis ikkje.
    }

    @Override
    public boolean leggInn(T verdi) {

        Objects.requireNonNull(verdi, "Nullverdiar er ikkje tillatt"); //Sjekker at verdi ikkje er null.
        Node<T> p = new Node<>(verdi);

        if (antall == 0) {
            hode = hale = p;
            hode.forrige = hale.neste = null;
            hode.neste = hale.forrige = null;
        } else {
            hode.forrige = null;
            hale.neste = p;
            p.forrige = hale;
            p.neste = null;
            hale = p;
        }

        antall++;
        endringer++;

        return true;
    }

    @Override
    public void leggInn(int indeks, T verdi) {


        indeksKontroll(indeks, true);
        Objects.requireNonNull(verdi, "Nullverdiar er ikkje tillatt"); //Sjekker at verdi ikkje er null.


        if (antall == 0) leggInn(verdi); //Tilfelle 1: tom liste
        else if (indeks == 0) { //Tilfelle 2: verdi lagt fremst. Veit at lista har minst ein verdi frå før, då dette er testa for tidlegare.
            Node<T> p = new Node<T>(verdi);
            p.neste = hode;
            hode.forrige = p;
            hode = p;
            antall++;
            endringer++;
        } else if (indeks == antall) leggInn(verdi); //Tilfelle 3: verdi lagt bakerst
        else { //Det berykta vanskeligaste tilfelle, mellom to verdiar.

            Node<T> p = new Node<T>(verdi); //Noden med verdien som skal bli sett inn
            Node<T> q = finnNode(indeks - 1); //q peiker på noden før p skal bli sett inn
            p.neste = q.neste; //p sin neste er q sin neste
            q.neste = p; // q sin neste er p
            p.forrige = q; // samme med q
            p.neste.forrige = p; // samme med q
            antall++;
            endringer++;
        }

    }

    @Override
    public boolean inneholder(T verdi) {
        if (indeksTil(verdi) != -1) return true;
        return false;
    }

    @Override
    public T hent(int indeks) {

        Node<T> p = finnNode(indeks);
        return p.verdi;
    }

    private Node<T> finnNode(int indeks) {

        indeksKontroll(indeks, false);
        Node<T> p = null;
        if (indeks < (antall) / 2) {
            p = hode;
            for (int i = 0; i < indeks; i++) p = p.neste; //Looper gjennom til rett verdi
        } else {
            p = hale;
            for (int i = 0; i < antall - indeks - 1; i++) p = p.forrige; //
        }
        return p;
    }


    @Override
    public int indeksTil(T verdi) {
        if (verdi == null) return -1;  //Å ha denne linja fyrst gjer at test 4i går gjennom
        //sidan den kaster unntak i andre testar for null-verdiar, noko den ikkje skal gjere
        for (int i = 0; i < antall; i++) {
            if (verdi.equals(hent(i))) return i;
        }
        return -1;
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        Objects.requireNonNull(nyverdi);
        Node<T> p = finnNode(indeks);
        T gammelVerdi = p.verdi;
        p.verdi = nyverdi;
        endringer++;
        return gammelVerdi;
    }

    @Override
    public boolean fjern(T verdi) {
        Node<T> q = hode, p = null;               // hjelpepekere

        while (q != null)                         // q skal finne verdien t
        {
            if (q.verdi.equals(verdi)) break;       // verdien funnet
            p = q;
            q = q.neste;
        }

        if (q == null) return false;              // fann ikkje verdi
        else if (antall == 1) hale = hode = null;
        else if (q == hode) {
            hode = hode.neste;
            hode.forrige = null;
        } else if (q == hale) { //Fjerne siste verdi
            hale = hale.forrige;
            hale.neste = null;
        } else { //Alle andre verdiar
            p.neste = q.neste;
            q.neste.forrige = p;
        }
        endringer++;
        antall--;

        return true;
    }

    @Override
    public T fjern(int indeks) {
        indeksKontroll(indeks, false);  // Kontroll

        T temp = null;
        if (antall == 1) hale = hode = null;
        else if (indeks == 0) {  //Fjerne fyrste verdi
            temp = hode.verdi;
            hode = hode.neste;
            hode.forrige.neste = null;
            hode.forrige = null;
        } else if (antall == 1) {
            hale = null;
        } else if (indeks == antall - 1) { //Fjerne siste verdi
            temp = hale.verdi;
            hale = hale.forrige;
            hale.neste.forrige = null;
            hale.neste = null;
        } else { //Alle andre verdiar
            Node<T> p = finnNode(indeks); //Finn noden som skal vekk.
            temp = p.verdi; //Lager verdien til noden
            Node<T> q = p.forrige;  // Noden før p.
            q.neste = p.neste;  // Koblin frå q til å får til noden etter.
            p.neste.forrige = q; //Samme med noden etter p, men går før.
        }
        antall--;
        endringer++;
        return temp;
    }

    @Override
    public void nullstill() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {

        StringBuilder s = new StringBuilder();
        s.append('[');

        if (!tom()) { //Hvis den dobbeltlenkalista ikke er tom (spesifisert av listeinterfase) skjer følgande:
            Node<T> p = hode;  //Sett peikar til hodet.
            s.append(p.verdi); //Legg til verdien til hovudet.

            p = p.neste; //Flytter så peikar til neste.

            while (p != null)  // Fortsetter fram til enden av lista (altså der p-neste = hale.neste = 0.
            {
                s.append(',').append(' ').append(p.verdi);  // Legg til formatering og så neste verdi.
                p = p.neste; // Flytter peikar.
            }
        }

        s.append(']');
        return s.toString();
    }


    public String omvendtString() {
        StringBuilder s = new StringBuilder();
        s.append('[');
//hei



        if (!tom()) { //Hvis den dobbeltlenkalista ikke er tom (spesifisert av listeinterfase) skjer følgande:
            Node<T> p = hale;  //Sett peikar til halen.
            s.append(p.verdi); //Legg til verdien til hovudet.

            p = p.forrige; //Flytter så peikar til forrige.

            while (p != null)  // Fortsetter fram til enden av lista (altså der p-neste = hale.neste = 0.
            {
                s.append(',').append(' ').append(p.verdi);  // Legg til formatering og så neste verdi.

                p = p.forrige; // Flytter peikar.
            }
        }


        s.append(']');
        return s.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new DobbeltLenketListeIterator();
    }

    public Iterator<T> iterator(int indeks) {
        indeksKontroll(indeks, false);
        return new DobbeltLenketListeIterator();
    }

    private class DobbeltLenketListeIterator implements Iterator<T> {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator() {
            denne = hode;     // p starter på den første i listen
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        private DobbeltLenketListeIterator(int indeks) {
            denne = finnNode(indeks);
            fjernOK = false;
            iteratorendringer = endringer;
        }

        @Override
        public boolean hasNext() {
            return denne != null;
        }

        @Override
        public T next() {
            if (iteratorendringer != endringer) throw new ConcurrentModificationException();
            if (!hasNext()) throw new NoSuchElementException();  //Korter ned if-setningen :D

            fjernOK = true;
            T temp = denne.verdi;
            denne = denne.neste;
            return temp;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    } // class DobbeltLenketListeIterator

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        throw new UnsupportedOperationException();
    }

} // class DobbeltLenketListe