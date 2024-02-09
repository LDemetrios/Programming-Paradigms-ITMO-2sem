package util;

import java.util.Arrays;

public class IntList {
    private int[] array = new int[1];
    private int length = 0;

    public void extend(int minCapacity) {
        if (length < minCapacity) {
            array = Arrays.copyOf(array, Math.max(length + (length >> 1) + 1, minCapacity));
            length = minCapacity;
        }
    }

    public void addElement(int el) {
        int addTo = length;
        if (length == array.length) {
            extend(length + 1);
        }
        array[addTo] = el;
        length = addTo + 1;
    }

    public int getLength() {
        return length;
    }

    public int get(int index) {
        return array[index];
    }

    public void set(int index, int element) {
        array[index] = element;
    }

    public void addAll(IntList elements) {
        extend(length + elements.length);
        System.arraycopy(elements.array, 0, this.array, length, elements.array.length);
    }

    public String joinToString(String prefix, String suffix, String separator) {
        int iMax = length - 1;
        if (iMax == -1)
            return prefix + suffix;

        StringBuilder b = new StringBuilder(prefix);
        for (int i = 0; ; i++) {
            b.append(array[i]);
            if (i == iMax)
                return b.append(suffix).toString();
            b.append(separator);
        }
    }

    @Override
    public String toString() {
        return joinToString("util.IntList [", "]", ", ");
    }

    public int getLast() {
        return array[length - 1];
    }
}