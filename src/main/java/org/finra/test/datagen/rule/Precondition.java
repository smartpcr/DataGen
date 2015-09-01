package org.finra.test.datagen.rule;

import java.util.List;

/**
 * Created on 9/1/2015.
 */
public interface Precondition {
	List<FieldTrigger> getFieldTriggers();
}
