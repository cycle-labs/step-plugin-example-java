package io.cyclelabs.stepplugin.example.fixtures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import io.cyclelabs.stepplugin.context.ScenarioContext;
import io.cyclelabs.stepplugin.context.ScenarioState;
import io.cyclelabs.stepplugin.context.SuiteContext;
import io.cyclelabs.stepplugin.context.SuiteState;
import io.cyclelabs.stepplugin.example.constants.CounterConstants;

import java.util.UUID;

public class CounterScenarioFixtureTest {

    private SuiteContext suiteContext;
    private ScenarioContext scenarioContext;
    private CounterScenarioFixture scenarioFixture;
    private CounterSuiteFixture suiteFixture;
    
    @BeforeEach
    public void setUp() {
        suiteContext = new SuiteContext(null, null, new SuiteState());
        scenarioContext = new ScenarioContext(new ScenarioState(), UUID.randomUUID());
        suiteFixture = new CounterSuiteFixture();
        scenarioFixture = new CounterScenarioFixture();
        ReflectionTestUtils.setField(scenarioFixture, "suiteFixture", suiteFixture);
        
        // Initialize suite state as it would be in real execution
        suiteFixture.markSuiteStart(suiteContext);
    }

    @Test
    public void testMarkScenarioStart() {
        scenarioFixture.markScenarioStart(suiteContext, scenarioContext);
        
        // Verify that the counter was initialized to 0
        int counter = scenarioContext.getScenarioState().get(CounterConstants.KEY_COUNTER);
        assertEquals(0, counter);
        
        // Verify scenario start time was set
        Long startTime = scenarioContext.getScenarioState().get(CounterConstants.KEY_SCENARIO_START_TIME);
        assertNotNull(startTime);
        assertTrue(startTime > 0);
    }

    @Test
    public void testMarkScenarioEnd() {
        // Start scenario first
        scenarioFixture.markScenarioStart(suiteContext, scenarioContext);
        scenarioContext.getScenarioState().set(CounterConstants.KEY_COUNTER, 7);
        
        // End scenario
        scenarioFixture.markScenarioEnd(suiteContext, scenarioContext);
        
        // Verify scenario state is still accessible
        int counter = scenarioContext.getScenarioState().get(CounterConstants.KEY_COUNTER);
        assertEquals(7, counter);
    }

    @Test
    public void testScenarioLifecycle() {
        // Test the full scenario lifecycle
        scenarioFixture.markScenarioStart(suiteContext, scenarioContext);
        
        // Verify counter is initialized
        int counter = scenarioContext.getScenarioState().get(CounterConstants.KEY_COUNTER);
        assertEquals(0, counter);
        
        // Modify the counter
        scenarioContext.getScenarioState().set(CounterConstants.KEY_COUNTER, 5);
        
        // End the scenario
        scenarioFixture.markScenarioEnd(suiteContext, scenarioContext);
        
        // Verify final state
        int finalCounter = scenarioContext.getScenarioState().get(CounterConstants.KEY_COUNTER);
        assertEquals(5, finalCounter);
    }

    @Test
    public void testMultipleScenarios() {
        // Simulate multiple scenarios running
        for (int i = 1; i <= 3; i++) {
            // Each scenario gets a fresh scenario state
            ScenarioContext freshScenarioContext = new ScenarioContext(new ScenarioState(), UUID.randomUUID());
            CounterScenarioFixture freshScenarioFixture = new CounterScenarioFixture();
            ReflectionTestUtils.setField(freshScenarioFixture, "suiteFixture", suiteFixture);
            
            freshScenarioFixture.markScenarioStart(suiteContext, freshScenarioContext);
            
            // Verify counter starts at 0 for each scenario
            int counter = freshScenarioContext.getScenarioState().get(CounterConstants.KEY_COUNTER);
            assertEquals(0, counter);
            
            // Do some work
            freshScenarioContext.getScenarioState().set(CounterConstants.KEY_COUNTER, i * 10);
            
            freshScenarioFixture.markScenarioEnd(suiteContext, freshScenarioContext);
        }
        
        // Suite fixture state is tracked via instance variable
        assertEquals(3, suiteFixture.getScenarioCount());
    }

    @Test
    public void testScenarioAndFixtureStateIsolation() {
        // Start first scenario
        scenarioFixture.markScenarioStart(suiteContext, scenarioContext);
        scenarioContext.getScenarioState().set(CounterConstants.KEY_COUNTER, 100);
        scenarioFixture.markScenarioEnd(suiteContext, scenarioContext);
        
        // Start second scenario with fresh scenario state
        ScenarioContext freshScenarioContext = new ScenarioContext(new ScenarioState(), UUID.randomUUID());
        CounterScenarioFixture freshScenarioFixture = new CounterScenarioFixture();
        ReflectionTestUtils.setField(freshScenarioFixture, "suiteFixture", suiteFixture);
        
        freshScenarioFixture.markScenarioStart(suiteContext, freshScenarioContext);
        
        // Verify counter is reset for new scenario
        int counter = freshScenarioContext.getScenarioState().get(CounterConstants.KEY_COUNTER);
        assertEquals(0, counter);
    }
}
