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

@StepIdentifier("verifyCounter")
@Component
public class VerifyCounterStep implements StepDefinition {

    private static final Logger logger = LoggerFactory.getLogger(VerifyCounterStep.class);

    @Override
    public StepResponse call(SuiteContext suiteContext, ScenarioContext scenarioContext, StepInputs inputs) {
        logger.info("Executing verifyCounter step");

        // Get the expected counter value.
        int expected = inputs.getParameterAsNumber("total").intValue();
        logger.debug("Expected counter value: {}", expected);

        // Get the actual counter value.
        int actual = scenarioContext.getScenarioState().get(CounterConstants.KEY_COUNTER);
        logger.debug("Actual counter value: {}", actual);

        // Compare them.
        if (expected == actual) {
            logger.info("Counter verification PASSED: {} == {}", expected, actual);
            return new StepResponse()
                .status(ExecutionStatus.PASS)
                .message("The counter value is " + expected + ".");
        } else {
            logger.warn("Counter verification FAILED: expected {}, but got {}", expected, actual);
            return new StepResponse()
                .status(ExecutionStatus.FAIL)
                .message("The counter value should be " + expected + ", but it is actually " + actual + ".");
        }
    }

}
