package util;

import java.util.Arrays;

public class LongList {
    private long[] array = new long[1];
    private int length = 0;

    public void extend(int minCapacity) {
        if (length < minCapacity) {
            array = Arrays.copyOf(array, Math.max(length + (length >> 1) + 1, minCapacity));
            length = minCapacity;
        }
    }

    public void addElement(long el) {
        if (length == array.length) {
            extend(length + 1);
        }
        array[length] = el;
        length++;
    }

    public int getLength() {
        return length;
    }

    public long get(int index) {
        return array[index];
    }

    public void set(int index, long element) {
        array[index] = element;
    }
}