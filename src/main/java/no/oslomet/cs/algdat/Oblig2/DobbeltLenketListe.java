package no.oslomet.cs.algdat.Oblig2;


////////////////// class DobbeltLenketListe //////////////////////////////


import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;


public class DobbeltLenketListe<T> implements Liste<T> { //....

    /**
     * Node class
     *
     * @param <T>
     */

    public static void main(String[] args) {
        String[] s1 = {}, s2 = {"A"}, s3 = {null,"A",null,"B",null};
        DobbeltLenketListe<String> l1 = new DobbeltLenketListe<>(s1);
        DobbeltLenketListe<String> l2 = new DobbeltLenketListe<>(s2);
        DobbeltLenketListe<String> l3 = new DobbeltLenketListe<>(s3);

        System.out.println(l1.toString() + " " + l2.toString()
                + " " + l3.toString() + " " + l1.omvendtString() + " "
                + l2.omvendtString() + " " + l3.omvendtString());
        System.out.println("[] [A] [A, B] [] [A] [B, A]");
    }

    private static final class Node<T> {
        private T verdi;                   // nodens verdi
        private Node<T> forrige, neste;    // pekere

        private Node(T verdi, Node<T> forrige) {
            this.verdi = verdi;
            this.forrige = forrige;
            this.neste = neste;
        }

        private Node(T verdi) {
            this(verdi, null);
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

        if (a.length > 0) {
            int i = 0; //Erklærer i utenfor loopen for å kunne bruke videre
            for (; i < a.length; i++) {//Finner fyrste noden som ikkje er null).
                //Notat til meg sjølv:
                // Kvifor fekk eg det ikkje til å funke med for (; i < a.length && a[i] == null; i++);???
                if (a[i] != null) { //Looper berre gjennom "resten" av lista dersom det er funne ein verdi som er null
                    hode = new Node<>(a[i]);
                    antall++;
                    hale = hode; //Halen = hode når det berre er ein node i lista.
                    break;
                }
            }

            if (hale != null) {
                i++;
                for (; i < a.length; i++) { //Looper så gjennom resten av lista
                    if (a[i] != null) {
                        hale.neste = new Node(a[i]);
                        hale = hale.neste;
                        antall++;
                    }
                }
            }
        }
    }

    public Liste<T> subliste(int fra, int til) {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    @Override
    public void leggInn(int indeks, T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean inneholder(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T hent(int indeks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indeksTil(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean fjern(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T fjern(int indeks) {
        throw new UnsupportedOperationException();
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

        if (!tom()) { //Hvis den dobbeltlenkalista ikke er tom (spesifisert av listeinterfase) skjer følgande:
            Node<T> p = hale;  //Sett peikar til hodet.
            s.append(p.verdi); //Legg til verdien til hovudet.

            p = p.forrige; //Flytter så peikar til neste.

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
        throw new UnsupportedOperationException();
    }

    public Iterator<T> iterator(int indeks) {
        throw new UnsupportedOperationException();
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
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return denne != null;
        }

        @Override
        public T next() {
            throw new UnsupportedOperationException();
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
