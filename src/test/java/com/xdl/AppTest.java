package com.xdl;


import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void testIterator() {
        HashSet<String> nameSet = new HashSet<>();
        nameSet.add("xdl");
        nameSet.add("gj");
        Iterator<String> iterator = nameSet.iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            System.out.println(name);
            if (name == "xdl") {
                iterator.remove(); // 将会删除nameSet中的xdl元素
            }
        }
        for (String name : nameSet) {
            System.out.println(name);
        }
    }
}
