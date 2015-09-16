package org.finra.test.datagen;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created on 9/11/2015.
 */
public class CopyValueHandler implements HandleCopyValue {
	private List<ColumnDisplayRule> dependentDisplayRules;



	public CopyValueHandler(final String sourceFieldName, List<ColumnDisplayRule> displayRules){
		this.dependentDisplayRules = new ArrayList<>();
		ColumnDisplayRule displayRule = Iterables.find(displayRules, new Predicate<ColumnDisplayRule>() {
			@Override
			public boolean apply(ColumnDisplayRule displayRule) {
				return displayRule.diverFieldName.equalsIgnoreCase(sourceFieldName);
			}
		});
		if(displayRule != null && displayRule.sourceFieldName!=null) {
			for(final String tgtColumnName : displayRule.sourceFieldName.values()) {
				ColumnDisplayRule tgtDisplayRule = Iterables.find(displayRules, new Predicate<ColumnDisplayRule>() {
					@Override
					public boolean apply(ColumnDisplayRule displayRule) {
						return tgtColumnName.equalsIgnoreCase(displayRule.diverFieldName);
					}
				});
				if(tgtDisplayRule != null){
					this.dependentDisplayRules.add(tgtDisplayRule);
				}
			}
		}
	}

	@Override
	public void updateDependentFields(Map<String, Object> record, Object value) {
		if(this.dependentDisplayRules!=null && this.dependentDisplayRules.size()>0) {
			for(ColumnDisplayRule displayRule : this.dependentDisplayRules) {
				record.put(displayRule.diverFieldName, value);
			}
		}
	}
}
