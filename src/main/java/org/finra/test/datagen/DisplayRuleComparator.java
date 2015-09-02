package org.finra.test.datagen;

import java.util.Comparator;

/**
 * Created by xd on 9/2/2015.
 */
public class DisplayRuleComparator implements Comparator<ColumnDisplayRule> {
    @Override
    public int compare(ColumnDisplayRule o1, ColumnDisplayRule o2) {
        return o1.displayOrder - o2.displayOrder;
    }
}
