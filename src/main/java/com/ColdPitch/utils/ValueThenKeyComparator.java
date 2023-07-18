/**
 * @project ColdPitch
 * @author ARA
 * @since 2023-07-19 AM 4:33
 */

package com.ColdPitch.utils;

import java.util.Comparator;
import java.util.Map;

public class ValueThenKeyComparator
        <
            K extends Comparable<? super K>,
            V extends Comparable<? super V>
        > implements Comparator<Map.Entry<K, V>> {

    @Override
    public int compare(Map.Entry<K, V> a, Map.Entry<K, V> b) {
        int cmp1 = a.getValue().compareTo(b.getValue()) * -1;
        if (cmp1 != 0) {
            return cmp1;
        } else {
            return a.getKey().compareTo(b.getKey());
        }
    }
}
