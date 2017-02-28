package com.example.usuario.memoria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by micastillo on 26/2/2017.
 */

public class MyRandomizer{
    private final static Random random = new Random();


    public static List<String> random(Collection<String> lista,int cant) {
        List<String> list_aux =new ArrayList<String>();
        List<String> list_resultado=new ArrayList<String>();
        list_aux.addAll(lista);
        for (int i = 0; i < cant; i++) {
            int pos = random.nextInt(list_aux.size());
            list_resultado.add(list_aux.remove(pos));
        }
        return list_resultado;
    }


}
