package com.example.usuario.memoria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Created by micastillo on 26/2/2017.
 */

public class MyRandomizer{
    private final static Random random = new Random();


    public static List<String> random(List<String> lista,int cant) {
        List<String> aux = new ArrayList<String>();
        for (int i = 0; i < cant; i++) {
            int pos = random.nextInt(lista.size());
            aux.add(lista.remove(pos));
        }
        lista.addAll(aux);
        return aux;
    }


}
