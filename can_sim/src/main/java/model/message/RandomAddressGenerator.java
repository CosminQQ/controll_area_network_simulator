package model.message;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class RandomAddressGenerator {

    private ArrayList<Integer> L0, L1, L2, L3, L4, L5;

    public RandomAddressGenerator(){
        this.setUp();
    }

    private void setUp() {
        L0 = new ArrayList<>(100);
        L1 = new ArrayList<>(100);
        L2 = new ArrayList<>(100);
        L3 = new ArrayList<>(100);
        L4 = new ArrayList<>(100);
        L5 = new ArrayList<>(100);

        for (int i = -100; i <= -1; i++)
            L2.add(i);

        for (int i = -300; i <= -201; i++)
            L1.add(i);

        for (int i = -500; i <= -401; i++)
            L0.add(i);

        for (int i = 1; i <= 100; i++)
            L3.add(i);

        for (int i = 201; i <= 300; i++)
            L4.add(i);

        for (int i = 401; i <= 500; i++)
            L5.add(i);

        Collections.shuffle(L0);
        Collections.shuffle(L1);
        Collections.shuffle(L2);
        Collections.shuffle(L3);
        Collections.shuffle(L4);
        Collections.shuffle(L5);
    }

    public Integer generateRandomAddress(Priority priority) {
        Integer value = null;
        switch (priority) {
            case LEVEL_5:
                value = L5.getFirst();
                L5.removeFirst();
                return value;
            case LEVEL_4:
                value = L4.getFirst();
                L4.removeFirst();
                return value;
            case LEVEL_3:
                value = L3.getFirst();
                L3.removeFirst();
                return value;
            case LEVEL_2:
                value = L2.getFirst();
                L2.removeFirst();
                return value;
            case LEVEL_1:
                value = L1.getFirst();
                L1.removeFirst();
                return value;
            case LEVEL_0:
                value = L0.getFirst();
                L0.removeFirst();
                return value;
            default:
                return value;
        }
    }
}
