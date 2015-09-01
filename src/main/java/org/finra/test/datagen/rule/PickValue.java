package org.finra.test.datagen.rule;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 9/1/2015.
 */
public class PickValue extends SimpleDataGen<PickValue> {
	final String nullValue = "blank";
	final String operator = "In";
	//String fieldName = null;

	private List<String> allowedValues;
	public List<String> getAllowedValues() {
	    return this.allowedValues;
	}
	private static Random random = new Random(System.currentTimeMillis());

	public PickValue() {
		this.allowedValues = new ArrayList<>();
	}

	public PickValue(String fieldName, String... allowedValues) {
		this.fieldName = fieldName;
		this.allowedValues = Arrays.asList(allowedValues);
	}

	@Override
	public String getValue() {
		if(allowedValues!=null && allowedValues.size()>0) {
			int idx = random.nextInt(allowedValues.size());
			String value = allowedValues.get(idx);
			if(value.equalsIgnoreCase(nullValue))
				return null;
			return value;
		}
		return null;
	}

	@Override
	public String serialize() {
		Preconditions.checkNotNull(this.fieldName, "field name must be initialized in value picker");
		Preconditions.checkArgument(this.allowedValues!=null && this.allowedValues.size()>0, "allowed values are not initialized");

		StringBuilder sb = new StringBuilder();
		for(String item : this.allowedValues){
			if(sb.length()>0)
				sb.append(",");
			sb.append(item);
		}
		return String.format("%s %s (%s)", this.fieldName, operator, sb.toString());
	}

	@Override
	public PickValue parse(String descriptor) throws DataRuleParseException {
		Pattern pattern = Pattern.compile("^(\\w+)\\s+" + operator + "\\s+\\((.+)\\)$");
		PickValue vp;
		Matcher m = pattern.matcher(descriptor);
		if(m.find()){
			String fieldName = m.group(1);
			String[] allowedValues = m.group(2).split(",");
			vp=new PickValue(fieldName, allowedValues);
		}
		else {
			throw new DataRuleParseException("Unable to parse");
		}
		return vp;
	}

	@Override
	public boolean evaluate(RowContext context) throws DataGenException {
		Preconditions.checkNotNull(this.fieldName, "field name must be initialized in value picker");
		return super.evaluate(context);
	}
}
