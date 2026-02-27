package io.cyclelabs.stepplugin.example.fixtures;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.cyclelabs.stepplugin.context.SuiteContext;
import io.cyclelabs.stepplugin.service.SuiteFixture;

@Component
public class CounterSuiteFixture implements SuiteFixture {

    private static final Logger logger = LoggerFactory.getLogger(CounterSuiteFixture.class);

    // Suite-level fixture state (persists across scenarios)
    private long suiteStartTime;
    private int scenarioCount;

    public void markSuiteStart(SuiteContext suiteContext) {
        logger.info("Suite started - performing suite-level initialization");
        
        // Initialize suite-level fixture state
        suiteStartTime = System.currentTimeMillis();
        scenarioCount = 0;
        
        logger.debug("Suite initialization complete at {}", suiteStartTime);
    }

    public void markSuiteEnd(SuiteContext suiteContext) {
        logger.info("Suite ended - performing suite-level cleanup");
        
        // Log suite-level statistics using fixture state
        long duration = System.currentTimeMillis() - suiteStartTime;
        
        logger.info("Suite completed: {} scenarios executed in {} ms", scenarioCount, duration);
        logger.debug("Suite cleanup complete");
    }

    // Method to increment scenario count (called by scenario fixture)
    public void incrementScenarioCount() {
        scenarioCount++;
        logger.debug("Scenario count incremented to {}", scenarioCount);
    }

    // Getter methods for testing
    public long getSuiteStartTime() {
        return suiteStartTime;
    }

    public int getScenarioCount() {
        return scenarioCount;
    }

}

