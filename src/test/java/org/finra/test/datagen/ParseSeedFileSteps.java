package org.finra.test.datagen;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.finra.test.datagen.schema.SeedFileParser;
import org.finra.test.datagen.util.TableColumn;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created on 9/16/2015.
 */
public class ParseSeedFileSteps {
	private String seedFilePath;
	private List<TableColumn> columns;

	@Given("^a seed file with name \"(.+)\"$")
	public void setupSeedFile(String seedFileName) {
		URL fileUrl = TableColumn.class.getClassLoader().getResource(seedFileName);
		Preconditions.checkNotNull(fileUrl);
		this.seedFilePath = fileUrl.getFile();
	}

	@When("^parse seed file$")
	public void parseSeedFile() {
		try {
			this.columns = SeedFileParser.parseSeedFile(this.seedFilePath);
		}
		catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Then("^I should get (\\d+) table columns$")
	public void verifySeedFile(int totalCount, List<TableColumn> columnsToVerify) {
		assertThat(this.columns.size(), equalTo(totalCount));
		for(final TableColumn column : columnsToVerify) {
			TableColumn columnFound = Iterables.find(this.columns, new Predicate<TableColumn>() {
				@Override
				public boolean apply(TableColumn tableColumn) {
					return tableColumn.ordinal== column.ordinal;
				}
			});
			assertNotNull(columnFound);
			assertThat(columnFound.name, equalToIgnoringCase(column.name));
			assertThat(columnFound.dbType, equalTo(column.dbType));
			if(columnFound.dbType == DbType.Varchar) {
				assertThat(columnFound.size, equalTo(column.size));
			}
		}
	}
}
