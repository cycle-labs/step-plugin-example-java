package io.cyclelabs.stepplugin.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

import io.cyclelabs.stepplugin.OpenApiGeneratorApplication;

@SpringBootApplication(
    nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@ComponentScan(
    basePackages = {"io.cyclelabs.stepplugin.example"},
    nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
public class CounterPluginApplication extends OpenApiGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CounterPluginApplication.class, args);
    }

}