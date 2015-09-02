package org.finra.test.datagen;

/**
 * Created on 8/31/2015.
 */
public enum ConstraintType {
	/*
	 * take one of allowed values
	 */
	In,
	/*
	 * take one from returned unique values via sql query
	 */
	InValues,
	/*
	 * take a work date between two date points
	 * i.e. BetweenDates ['06-04-1989','06-30-1989')
	 * generate: 06-04-1989, 06-05-1989, ..., 06-29-1989
	 */
	BetweenDates,
	/*
	 *  take a work time between two time points, then append it to date value,
	 *  i.e. AppendTime $(cmn_event_dt) '09:00:00' '23:59:59'
	 */
	AppendTime,
	/*
	 * InNumberRange [1, 1000)
	 */
	InNumberRange,
	/*
	 * copy value from another field
	 */
	Take,
	/*
	 * InPriceRange [1.00, 999.99]
	 */
	InPriceRange,
	/*
	 * any value that matches a regex pattern
	 */
	Match,
	/*
	 * take a size and generate unique base64 encoded string
	 */
	UniqueString,
	/*
	 * format current time with prefix or suffix
	 * i.e. UniqueNumber "99900yyyyMMddHHmmss"
	 */
	UniqueNumber;
}
