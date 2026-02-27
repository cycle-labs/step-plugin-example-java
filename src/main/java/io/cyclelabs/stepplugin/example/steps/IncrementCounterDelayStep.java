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

@StepIdentifier("incrementCounterDelay")
@Component
public class IncrementCounterDelayStep implements StepDefinition {

    private static final Logger logger = LoggerFactory.getLogger(IncrementCounterDelayStep.class);

    @Override
    public StepResponse call(SuiteContext suiteContext, ScenarioContext scenarioContext, StepInputs inputs) {
        logger.info("Executing incrementCounterDelay step");

        // Get the timeout values.
        int timeout = inputs.getParameterAsNumber("timeout").intValue();
        String units = inputs.getParameterAsString("unit");
        if (units.equalsIgnoreCase("seconds")) timeout *= 1000;
        logger.debug("Delay timeout: {} ms", timeout);

        // Sleep for the delay time.
        try {
            logger.debug("Starting delay of {} ms", timeout);
            Thread.sleep(timeout);
            logger.debug("Delay completed");
        } catch (InterruptedException e) {
            logger.warn("Delay was interrupted", e);
        }

        // Get the increment value from the step parameter.
        int increment = inputs.getParameterAsNumber("increment").intValue();
        logger.debug("Increment value: {}", increment);

        // Get the current counter value.
        int counter = scenarioContext.getScenarioState().get(CounterConstants.KEY_COUNTER);
        logger.debug("Current counter value: {}", counter);

        // Increment the counter.
        int newCounter = counter + increment;
        scenarioContext.getScenarioState().set(CounterConstants.KEY_COUNTER, newCounter);
        logger.debug("New counter value: {}", newCounter);

        // Return the step response.
        return new StepResponse()
            .status(ExecutionStatus.PASS)
            .message(
                "Incremented the counter by " + increment + " from " + counter + " to " + newCounter + 
                " after a delay of " + timeout + " ms."
            );
    }

}
