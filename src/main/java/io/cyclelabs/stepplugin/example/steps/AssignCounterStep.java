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

@StepIdentifier("assignCounter")
@Component
public class AssignCounterStep implements StepDefinition {

    private static final Logger logger = LoggerFactory.getLogger(AssignCounterStep.class);

    @Override
    public StepResponse call(SuiteContext suiteContext, ScenarioContext scenarioContext, StepInputs inputs) {
        logger.info("Executing assignCounter step");

        // Get the counter.
        String variableName = inputs.getParameterAsString("variableName");
        int counter = scenarioContext.getScenarioState().get(CounterConstants.KEY_COUNTER);
        logger.debug("Current counter value: {}", counter);
        logger.debug("Assigning counter to variable: {}", variableName);

        // Return the step response with the assigned variable.
        return new StepResponse()
            .status(ExecutionStatus.PASS)
            .putVariablesItem(variableName, counter)
            .message("Assigned the counter value of '" + counter + "' to variable '" + variableName + "'.");
    }

}
