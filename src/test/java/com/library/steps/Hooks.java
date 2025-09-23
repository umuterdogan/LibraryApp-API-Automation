package com.library.steps;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import com.library.utility.Driver;

public class Hooks {

    @After
    public void tearDown(Scenario scenario) {
        Driver.closeDriver();
    }
}
