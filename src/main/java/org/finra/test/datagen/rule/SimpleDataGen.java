package org.finra.test.datagen.rule;

/**
 * Created on 9/1/2015.
 */
public abstract class SimpleDataGen<T> implements DataGen, Evaluatable<T> {
	protected String fieldName;
//	@Override
//	public String getValue() {
//		return null;
//	}
//
	@Override
	public String getFieldName() {
		return this.fieldName;
	}
//
//	@Override
//	public String serialize() {
//		return null;
//	}
//
//	@Override
//	public T parse(String descriptor) throws DataRuleParseException {
//		return null;
//	}

	@Override
	public boolean evaluate(RowContext context) throws DataGenException {
		try {
			context.setFieldValue(this.getFieldName(), this.getValue());
			return true;
		}
		catch (Exception e) {
			throw new DataGenException(e.getMessage());
		}
	}
}
