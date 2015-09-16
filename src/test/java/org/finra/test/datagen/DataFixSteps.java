package org.finra.test.datagen;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.finra.test.datagen.model.RecordConstraint;

import java.util.List;

/**
 * Created on 9/16/2015.
 */
public class DataFixSteps {
	@Given("^detail data based on display rule version (\\d+)$")
	public void setupDisplayRules(int version) {

	}

	@When("^generate data with the following constraints$")
	public void setupFieldConstraints(List<RecordConstraint> constraints) {

	}

	@Then("^the generated data should satisfy all constraints and total record should be less than (\\d+)$")
	public void verifyRecords(int maxCount) {

	}
}
