package org.finra.test.datagen.rule;

import java.io.IOException;

/**
 * Created on 9/1/2015.
 */
public interface Evaluatable<T> {
	String serialize();
	T parse(String descriptor) throws DataRuleParseException;
	boolean evaluate(RowContext context) throws DataGenException;
}
