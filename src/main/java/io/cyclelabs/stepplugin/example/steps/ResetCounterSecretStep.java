package io.cyclelabs.stepplugin.example.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.cyclelabs.stepplugin.context.ScenarioContext;
import io.cyclelabs.stepplugin.context.StepInputs;
import io.cyclelabs.stepplugin.context.SuiteContext;
import io.cyclelabs.stepplugin.example.constants.CounterConstants;
import io.cyclelabs.stepplugin.models.ExecutionStatus;
import io.cyclelabs.stepplugin.models.StepResponse;
import io.cyclelabs.stepplugin.service.StepDefinition;
import io.cyclelabs.stepplugin.service.StepIdentifier;

@StepIdentifier("resetCounterSecret")
@Component
public class ResetCounterSecretStep implements StepDefinition {

    private static final Logger logger = LoggerFactory.getLogger(ResetCounterSecretStep.class);

    @Override
    public StepResponse call(SuiteContext suiteContext, ScenarioContext scenarioContext, StepInputs inputs) {
        logger.info("Executing resetCounterSecret step");

        // Get the secret variable.
        int resetValue = inputs.getVariableAsNumber(CounterConstants.SECRET_VARIABLE).intValue();
        logger.debug("Reset value from SECRET_VARIABLE: {}", resetValue);
        
        // Reset the counter.
        scenarioContext.getScenarioState().set(CounterConstants.KEY_COUNTER, resetValue);
        logger.debug("Counter has been reset to {}", resetValue);

        // Return the step response.
        return new StepResponse()
            .status(ExecutionStatus.PASS)
            .message("Reset the counter to " + resetValue + ".");
    }

}
