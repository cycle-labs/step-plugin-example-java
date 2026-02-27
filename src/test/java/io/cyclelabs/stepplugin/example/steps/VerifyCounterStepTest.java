package io.cyclelabs.stepplugin.example.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.cyclelabs.stepplugin.context.ScenarioContext;
import io.cyclelabs.stepplugin.context.ScenarioState;
import io.cyclelabs.stepplugin.context.StepInputs;
import io.cyclelabs.stepplugin.context.SuiteContext;
import io.cyclelabs.stepplugin.context.SuiteState;
import io.cyclelabs.stepplugin.example.constants.CounterConstants;
import io.cyclelabs.stepplugin.models.ExecutionStatus;
import io.cyclelabs.stepplugin.models.StepInputsBody;
import io.cyclelabs.stepplugin.models.StepResponse;

import java.util.UUID;

public class VerifyCounterStepTest {

    private SuiteContext suiteContext;
    private ScenarioContext scenarioContext;
    private VerifyCounterStep step;
    
    @BeforeEach
    public void setUp() {
        suiteContext = new SuiteContext(null, null, new SuiteState());
        scenarioContext = new ScenarioContext(new ScenarioState(), UUID.randomUUID());
        step = new VerifyCounterStep();
    }

    @Test
    public void call_PASS() {
        scenarioContext.getScenarioState().set(CounterConstants.KEY_COUNTER, 5);

        StepInputsBody rawInputs = new StepInputsBody().putParametersItem("total", 5);
        StepInputs inputs = new StepInputs(rawInputs);
        
        StepResponse response = step.call(suiteContext, scenarioContext, inputs);

        assertEquals(ExecutionStatus.PASS, response.getStatus());
        assertEquals("The counter value is 5.", response.getMessage());
    }

    @Test
    public void call_FAIL() {
        scenarioContext.getScenarioState().set(CounterConstants.KEY_COUNTER, 5);

        StepInputsBody rawInputs = new StepInputsBody().putParametersItem("total", 6);
        StepInputs inputs = new StepInputs(rawInputs);
        
        StepResponse response = step.call(suiteContext, scenarioContext, inputs);

        assertEquals(ExecutionStatus.FAIL, response.getStatus());
        assertEquals("The counter value should be 6, but it is actually 5.", response.getMessage());
    }
    
}
