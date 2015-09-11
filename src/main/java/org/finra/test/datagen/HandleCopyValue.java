package org.finra.test.datagen;

import java.util.Map;

/**
 * Created on 9/11/2015.
 */
public interface HandleCopyValue {
	void updateDependentFields(Map<String, Object> record, Object value);
}
