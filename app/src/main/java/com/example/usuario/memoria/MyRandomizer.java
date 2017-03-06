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
        List<String> list=new ArrayList<String>(lista);
        List<String> list_resultado=new ArrayList<String>();
        for (int i = 0; i < cant; i++) {
            String item=anyItem(list);
             list_resultado.add(item);
             list.remove(item);
        }
        return list_resultado;
    }
    public  static String anyItem(List<String> list){
        int index=random.nextInt(list.size());
        return list.get(index);
    }


}
