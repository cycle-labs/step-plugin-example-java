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

@StepIdentifier("resetCounter")
@Component
public class ResetCounterStep implements StepDefinition {

    private static final Logger logger = LoggerFactory.getLogger(ResetCounterStep.class);

    @Override
    public StepResponse call(SuiteContext suiteContext, ScenarioContext scenarioContext, StepInputs inputs) {
        logger.info("Executing resetCounter step");

        // Reset the counter.
        scenarioContext.getScenarioState().set(CounterConstants.KEY_COUNTER, 0);
        logger.debug("Counter has been reset to 0");

        // Return the step response.
        return new StepResponse()
            .status(ExecutionStatus.PASS)
            .message("Reset the counter to zero.");
    }

}
