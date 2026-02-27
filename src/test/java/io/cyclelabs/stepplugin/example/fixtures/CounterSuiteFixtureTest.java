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

public class CounterSuiteFixtureTest {

    private SuiteContext suiteContext;
    private CounterSuiteFixture suiteFixture;
    
    @BeforeEach
    public void setUp() {
        suiteContext = new SuiteContext(null, null, new SuiteState());
        suiteFixture = new CounterSuiteFixture();
    }

    @Test
    public void testMarkSuiteStart() {
        suiteFixture.markSuiteStart(suiteContext);
        
        // Verify that suite fixture state was initialized
        assertTrue(suiteFixture.getSuiteStartTime() > 0);
        assertEquals(0, suiteFixture.getScenarioCount());
    }

    @Test
    public void testMarkSuiteEnd() {
        // Initialize suite first
        suiteFixture.markSuiteStart(suiteContext);
        
        // Simulate some scenarios
        suiteFixture.incrementScenarioCount();
        suiteFixture.incrementScenarioCount();
        suiteFixture.incrementScenarioCount();
        
        // End suite and verify it doesn't throw
        suiteFixture.markSuiteEnd(suiteContext);
        
        // Verify suite fixture state still accessible after end
        assertEquals(3, suiteFixture.getScenarioCount());
    }

    @Test
    public void testSuiteLifecycle() {
        // Test the full suite lifecycle
        suiteFixture.markSuiteStart(suiteContext);
        
        // Verify initialization
        assertEquals(0, suiteFixture.getScenarioCount());
        
        // Simulate multiple scenarios
        for (int i = 0; i < 5; i++) {
            suiteFixture.incrementScenarioCount();
        }
        
        // End the suite
        suiteFixture.markSuiteEnd(suiteContext);
        
        // Verify final state
        assertEquals(5, suiteFixture.getScenarioCount());
    }

    @Test
    public void testSuiteStatePersistence() {
        // Start suite
        suiteFixture.markSuiteStart(suiteContext);
        
        long startTime = suiteFixture.getSuiteStartTime();
        assertNotNull(startTime);
        
        // Increment scenario count multiple times
        for (int i = 1; i <= 10; i++) {
            suiteFixture.incrementScenarioCount();
        }
        
        // Verify count
        assertEquals(10, suiteFixture.getScenarioCount());
        
        // Verify start time hasn't changed
        assertEquals(startTime, suiteFixture.getSuiteStartTime());
    }

    @Test
    public void testCompleteLifecycle() {
        // This test demonstrates the complete lifecycle of a suite with multiple scenarios
        
        // ========== SUITE START ==========
        suiteFixture.markSuiteStart(suiteContext);
        
        long suiteStartTime = suiteFixture.getSuiteStartTime();
        assertTrue(suiteStartTime > 0, "Suite should have a start time");
        assertEquals(0, suiteFixture.getScenarioCount(), "Suite should start with 0 scenarios");
        
        // ========== SCENARIO 1 ==========
        ScenarioContext scenario1Context = new ScenarioContext(new ScenarioState(), UUID.randomUUID());
        CounterScenarioFixture scenario1Fixture = new CounterScenarioFixture();
        ReflectionTestUtils.setField(scenario1Fixture, "suiteFixture", suiteFixture);
        
        scenario1Fixture.markScenarioStart(suiteContext, scenario1Context);
        Integer counter1 = scenario1Context.getScenarioState().get(CounterConstants.KEY_COUNTER);
        assertEquals(0, counter1.intValue(), "Scenario 1 counter should start at 0");
        
        // Simulate scenario 1 work
        scenario1Context.getScenarioState().set(CounterConstants.KEY_COUNTER, 5);
        
        scenario1Fixture.markScenarioEnd(suiteContext, scenario1Context);
        counter1 = scenario1Context.getScenarioState().get(CounterConstants.KEY_COUNTER);
        assertEquals(5, counter1.intValue(), "Scenario 1 counter should end at 5");
        
        // ========== SCENARIO 2 ==========
        ScenarioContext scenario2Context = new ScenarioContext(new ScenarioState(), UUID.randomUUID());  // Fresh scenario state
        CounterScenarioFixture scenario2Fixture = new CounterScenarioFixture();
        ReflectionTestUtils.setField(scenario2Fixture, "suiteFixture", suiteFixture);
        
        scenario2Fixture.markScenarioStart(suiteContext, scenario2Context);
        Integer counter2 = scenario2Context.getScenarioState().get(CounterConstants.KEY_COUNTER);
        assertEquals(0, counter2.intValue(), "Scenario 2 counter should start fresh at 0");
        
        // Simulate scenario 2 work
        scenario2Context.getScenarioState().set(CounterConstants.KEY_COUNTER, 12);
        
        scenario2Fixture.markScenarioEnd(suiteContext, scenario2Context);
        counter2 = scenario2Context.getScenarioState().get(CounterConstants.KEY_COUNTER);
        assertEquals(12, counter2.intValue(), "Scenario 2 counter should end at 12");
        
        // ========== SCENARIO 3 ==========
        ScenarioContext scenario3Context = new ScenarioContext(new ScenarioState(), UUID.randomUUID());  // Fresh scenario state
        CounterScenarioFixture scenario3Fixture = new CounterScenarioFixture();
        ReflectionTestUtils.setField(scenario3Fixture, "suiteFixture", suiteFixture);
        
        scenario3Fixture.markScenarioStart(suiteContext, scenario3Context);
        Integer counter3 = scenario3Context.getScenarioState().get(CounterConstants.KEY_COUNTER);
        assertEquals(0, counter3.intValue(), "Scenario 3 counter should start fresh at 0");
        
        // Simulate scenario 3 work
        scenario3Context.getScenarioState().set(CounterConstants.KEY_COUNTER, 8);
        
        scenario3Fixture.markScenarioEnd(suiteContext, scenario3Context);
        counter3 = scenario3Context.getScenarioState().get(CounterConstants.KEY_COUNTER);
        assertEquals(8, counter3.intValue(), "Scenario 3 counter should end at 8");
        
        // ========== SUITE END ==========
        suiteFixture.markSuiteEnd(suiteContext);
        
        // Verify suite-level state was maintained throughout
        assertEquals(suiteStartTime, suiteFixture.getSuiteStartTime(),
            "Suite start time should remain constant");
        
        // Verify scenario count was tracked via the suite fixture
        assertEquals(3, suiteFixture.getScenarioCount(),
            "Suite should have tracked 3 scenarios");
        
        // We can verify the suite finished without errors
        assertTrue(suiteFixture.getSuiteStartTime() > 0, 
            "Suite should still have valid state after completion");
    }
}
