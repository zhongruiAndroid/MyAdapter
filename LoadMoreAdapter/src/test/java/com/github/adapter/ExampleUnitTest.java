package com.github.adapter;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void asfdd() {
        System.out.println("ckcsttly1".startsWith("ckcsttly"));
    }

    @Test
    public void asfd() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add(1, "3");
        System.out.println(list.get(1));
        System.out.println(list.get(2));
        /*for (int i = 0; i < list.size(); i++) {
            if(i<=1){
                list.add("m"+i);
            }
            System.out.println(i);
        }*/
    }

    @Test
    public void asdfd() {
        int a = 1;
        for (int i = 0; i < 10; i += a) {
            System.out.println(i);
            if (i == 3) {
                a = 2;
            }else{
                a = 1;
            }
        }
    }
}