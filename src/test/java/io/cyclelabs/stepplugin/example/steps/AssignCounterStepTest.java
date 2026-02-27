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

public class AssignCounterStepTest {

    private SuiteContext suiteContext;
    private ScenarioContext scenarioContext;
    private AssignCounterStep step;
    
    @BeforeEach
    public void setUp() {
        suiteContext = new SuiteContext(null, null, new SuiteState());
        scenarioContext = new ScenarioContext(new ScenarioState(), UUID.randomUUID());
        step = new AssignCounterStep();
    }

    @Test
    public void call() {
        scenarioContext.getScenarioState().set(CounterConstants.KEY_COUNTER, 5);

        StepInputsBody rawInputs = new StepInputsBody().putParametersItem("variableName", "TEST_VAR");
        StepInputs inputs = new StepInputs(rawInputs);
        
        StepResponse response = step.call(suiteContext, scenarioContext, inputs);

        assertEquals(ExecutionStatus.PASS, response.getStatus());
        assertEquals("Assigned the counter value of '5' to variable 'TEST_VAR'.", response.getMessage());

        Object value = response.getVariables().get("TEST_VAR");
        assertEquals(5, value);
    }
    
}
