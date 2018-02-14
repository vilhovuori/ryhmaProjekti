package fi.academy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Milla koodaa junien hakua junan numeron perusteella
public class Milla  extends Hakija{

    public static void main(String[] args) {
        new Milla().haeNumeronPerusteella();
    }

    public void haeNumeronPerusteella() {

        //String junanNumero = "169";


        Scanner lukija = new Scanner(System.in);
        System.out.print("\nAnna junan numero: ");
        String junanNumero = lukija.nextLine();


        String alkuUrl = "https://rata.digitraffic.fi/api/v1/";
        try {
            URL urlLiike = new URL(alkuUrl + "trains/latest/" + junanNumero);
            ObjectMapper mapper = new ObjectMapper();
            CollectionType millan = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Juna.class);
            List<Juna> junaLista = mapper.readValue(urlLiike, millan);

            if (!junaLista.get(0).isRunningCurrently()) {
                System.out.println("\nJuna ei ole tällä hetkellä liikkeessä");
            } else {
                try {
                    URL urlAsemat = new URL(alkuUrl + "train-tracking/latest/" + junanNumero + "?version=1000");
                    ObjectMapper mapperUusi = new ObjectMapper();
                    CollectionType millanUusi = mapperUusi.getTypeFactory().constructCollectionType(ArrayList.class, Juna.class);
                    List<Juna> junaUusiLista = mapperUusi.readValue(urlAsemat, millanUusi);
                    String nykyinenAsema = haeAsema(junaUusiLista.get(0).getStation());
                    if (nykyinenAsema.equals("VIRHE")) { nykyinenAsema = junaUusiLista.get(0).getStation();}
                    String seuraavaAsema = haeAsema(junaUusiLista.get(0).getNextStation());
                   if (seuraavaAsema.equals("VIRHE")) { seuraavaAsema = junaUusiLista.get(0).getNextStation();}
                    String asemat = "\nVälillä: " + nykyinenAsema + " - "
                            + seuraavaAsema;
                    System.out.println(asemat);
                } catch(IOException f){
                    System.out.println(f); } }
        } catch (IOException e) {
            System.out.println(e); }

}}
//Date d = new Date();
//            d.getTime();