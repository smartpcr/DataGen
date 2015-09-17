package org.finra.test.datagen;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/**
 * Created on 9/9/2015.
 */
@RunWith(Cucumber.class)
@CucumberOptions(tags = {"~@ignore", "@focus"})
public class CukeTest {
    @BeforeClass
    public static void setup(){
        System.out.println("setup");
    }

    @AfterClass
    public static void tearDown() {
        System.out.println("teardown");
    }
}
