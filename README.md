# Step Plugin Example for Java 

This repository contains an example step plugin written in Java that uses the
[Cycle Step Plugin SDK for Java with Spring Boot](https://dev.azure.com/cyclelabs/cycle/_git/cycle-step-plugin-sdk-java).


## Commands

```bash
# Build the project
mvn clean compile

# Run unit tests
mvn clean test

# Build the JAR file
mvn clean package

# Run Spring Boot
mvn spring-boot:run
```


## Steps

This plugin's steps are specified in [`steps.yml`](src/main/resources/steps.yml).
They manage an internal counter.
The counter's value is stored as part of the scenario state.
Steps enable the tester to:

* reset the counter
* increment the counter
* assign the counter's value to a Cycle variable
* verify the value of the counter

These steps exhibit the basic behaviors of step inputs and outputs.
They are meant to be an instructional example,
not a "real" step plugin for Cycle that customers or partners would use.


## Code

This plugin project follows Maven's [Standard Directory Layout](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html).

The code that a plugin author must write is fairly minimal:
the main class, the scenario fixture, and the step definitions.
Everything else for the plugin service is handled by the plugin SDK.

### Main class

[`CounterPluginApplication`](src/main/java/io/cyclelabs/stepplugin/example/CounterPluginApplication.java) is the main class.
It is a subclass of `OpenApiGeneratorApplication` from the Java plugin SDK, and its code is mostly boilerplate.
Its `@ComponentScan` attribute must define this project's `basePackages` (as `io.cyclelabs.stepplugin.example`)
in order fo the SDK code to pick up the steps and fixtures.

### Scenario fixture

[`CounterScenarioFixture`](src/main/java/io/cyclelabs/stepplugin/example/fixtures/CounterScenarioFixture.java)
implements the SDK's `ScenarioFixture` interface to provide scenario start and end logic.
It must bear the `@Component` annotations so that Spring Boot can autowire it to the SDK service as a bean.
This plugin must initialize its internal counter to zero when a new scenario starts.
There is no logic that must be performed when a scenario ends.

### Step definitions

All the plugin's steps are implemented under the
[`io.cyclelabs.stepplugin.example.steps`](src/main/java/io/cyclelabs/stepplugin/example/steps/) package.
Each step is its own class that implements the SDK's `StepDefinition` interface.
It must be annotated with `@StepIdentifier` with the step ID listed in `steps.yml` to link spec to definition.
It must also bear the `@Component` annotation so that Spring Boot can autowire it to the SDK step registry.


## Tests

The [`src/test/`](src/test/`) directory contains all the unit tests for the plugin.
Each step has a unit test that calls the step with a fake execution context.
The tests verify the correctness of the step responses, returned variables, and counter values.

API-level integration tests are arguably not worthwhile.
The Java plugin SDK has unit tests for all API handlers that cover calls made to `/scenario/*` endpoints.
Writing API tests (or even handler) tests here in the plugin would redundantly cover
both the API mechanics at the integration level and the step definition logic at the unit level.
There is not much additional risk mitigated.

However, it would be worthwhile to write end-to-end tests with Cycle.
Feature files should have scenarios that call this plugin's steps and verify that they work correctly.
This kind of end-to-end testing would make sure that the steps work in totality.
