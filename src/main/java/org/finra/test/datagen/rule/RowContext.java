package org.finra.test.datagen.rule;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 9/1/2015.
 */
public class RowContext {
	private int rowIndex;
	public int getRowIndex() {
	    return this.rowIndex;
	}
	private int totalRowCount;

	private Map<String, Object> fieldValues;
	public Map<String, Object> getFieldValues() {
	    return this.fieldValues;
	}

	private Map<String, List<Runnable>> registeredFieldTriggers;

	public RowContext(int totalRowCount) {
		this.rowIndex = 0;
		this.totalRowCount = totalRowCount;
		this.fieldValues = new HashMap<>();
		this.registeredFieldTriggers = new HashMap<>();
	}

	public boolean next() {
		if(this.totalRowCount>rowIndex+1){
			this.rowIndex++;
			return true;
		}
		return false;
	}

	public void registerFieldTrigger(String fieldName, Runnable action) {
		if(this.registeredFieldTriggers.containsKey(fieldName)){
			this.registeredFieldTriggers.get(fieldName).add(action);
		}
		else {
			this.registeredFieldTriggers.put(fieldName, Arrays.asList(new Runnable[]{action}));
		}
	}

	public void setFieldValue(String fieldName, Object value) {
		this.fieldValues.put(fieldName, value);
		if(this.registeredFieldTriggers.containsKey(fieldName)){
			List<Runnable> actions = this.registeredFieldTriggers.get(fieldName);
			for(Runnable action: actions){
				action.run();
			}
		}
	}
}
