package io.cyclelabs.stepplugin.example.fixtures;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.cyclelabs.stepplugin.context.ScenarioContext;
import io.cyclelabs.stepplugin.context.SuiteContext;
import io.cyclelabs.stepplugin.example.constants.CounterConstants;
import io.cyclelabs.stepplugin.service.ScenarioFixture;

@Component
public class CounterScenarioFixture implements ScenarioFixture {

    private static final Logger logger = LoggerFactory.getLogger(CounterScenarioFixture.class);

    @Autowired(required = false)
    private CounterSuiteFixture suiteFixture;

    @Override
    public void markScenarioStart(SuiteContext suiteContext, ScenarioContext scenarioContext) {
        logger.info("Scenario started - initializing counter");
        
        // Initialize the counter to zero.
        scenarioContext.getScenarioState().set(CounterConstants.KEY_COUNTER, 0);
        
        // Track scenario start time in scenario state
        long startTime = System.currentTimeMillis();
        scenarioContext.getScenarioState().set(CounterConstants.KEY_SCENARIO_START_TIME, startTime);
        
        // Increment suite-level scenario count using fixture state
        if (suiteFixture != null) {
            suiteFixture.incrementScenarioCount();
            logger.debug("Counter initialized to 0, scenario {} started at {}", 
                suiteFixture.getScenarioCount(), startTime);
        } else {
            logger.debug("Counter initialized to 0, started at {}", startTime);
        }
    }

    @Override
    public void markScenarioEnd(SuiteContext suiteContext, ScenarioContext scenarioContext) {
        logger.info("Scenario ended");
        
        // Log scenario duration
        long startTime = scenarioContext.getScenarioState().get(CounterConstants.KEY_SCENARIO_START_TIME);
        long duration = System.currentTimeMillis() - startTime;
        int finalCounter = scenarioContext.getScenarioState().get(CounterConstants.KEY_COUNTER);
        
        logger.info("Scenario completed in {} ms with final counter value: {}", duration, finalCounter);
    }

}
