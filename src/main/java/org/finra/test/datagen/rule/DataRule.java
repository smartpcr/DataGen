package org.finra.test.datagen.rule;

import java.util.List;

/**
 * Created on 9/1/2015.
 */
public interface DataRule {
	List<Precondition> getPreconditions();
//	List<DataGenerator> getDataGenerators();
//	List<PostProcessor> getPostProcessors();
}
