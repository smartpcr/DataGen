package org.finra.test.datagen.rule;

import com.google.common.base.Preconditions;
import org.finra.test.datagen.util.DataSourceManager;
import org.finra.test.datagen.util.DbConnection;
import org.finra.test.datagen.util.DbReader;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 9/1/2015.
 */
public class PickReferencedValue extends SimpleDataGen<PickReferencedValue> {
	final String operator = "InValues";
	private String dataSourceName;
	private String selectValueSql;
	private List<String> allowedValues;
	private static Random random = new Random(System.currentTimeMillis());

	public PickReferencedValue() {
	}

	public PickReferencedValue(String fieldName, String dsName, String sql, List<String> allowedValues) {
		this.fieldName = fieldName;
		this.dataSourceName = dsName;
		this.selectValueSql = sql;
		this.allowedValues = allowedValues;
	}

	@Override
	public String getValue() {
		if(allowedValues!=null && allowedValues.size()>0) {
			int idx = random.nextInt(allowedValues.size());
			String value = allowedValues.get(idx);
			return value;
		}
		return null;
	}

	@Override
	public String serialize() {
		Preconditions.checkNotNull(this.fieldName);
		Preconditions.checkNotNull(this.dataSourceName);
		Preconditions.checkNotNull(this.selectValueSql);
		return String.format("%s %s (%s:%s)", this.fieldName, this.operator, this.dataSourceName, this.selectValueSql);
	}

	@Override
	public PickReferencedValue parse(String descriptor) throws DataRuleParseException {
		Pattern pattern = Pattern.compile("^(\\w+)\\s+" + this.operator + "\\((\\w+):(.+)\\)$");
		Matcher m = pattern.matcher(descriptor);
		if(m.find()){
			String fieldName = m.group(1);
			String dataSource = m.group(2);
			String sql = m.group(3);
			try {
				DbConnection dbConn = DataSourceManager.getDbConnectionSettings(dataSource);
				List<String> allowedValues = DbReader.readDistinctValues(dbConn, sql);
				PickReferencedValue prv = new PickReferencedValue(fieldName, dataSource, sql, allowedValues);
				return prv;
			}
			catch (Exception e) {
				throw new DataRuleParseException(e.getMessage());
			}
		}
		else {
			throw new DataRuleParseException("Unable to parse");
		}
	}

	@Override
	public boolean evaluate(RowContext context) throws DataGenException {
		Preconditions.checkNotNull(this.fieldName);
		Preconditions.checkNotNull(this.dataSourceName);
		Preconditions.checkNotNull(this.selectValueSql);
		return super.evaluate(context);
	}
}
