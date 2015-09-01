package org.finra.test.datagen;

import org.finra.test.datagen.util.DisplayRuleUtil;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created on 9/1/2015.
 */
public class DataGenTest {
	@Test
	public void canReadRule() {
		try {
			List<ColumnDisplayRule> displayRules = DisplayRuleUtil.readDisplayRules();
			assertNotNull(displayRules);
			assertEquals(447, displayRules.size());
		}
		catch (Exception e){
			fail(e.getMessage());
		}
	}
}
