package com.example.fileuploader.cucumber;

import com.example.fileuploader.FileuploaderApplication;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@RunWith(Cucumber.class)
@CucumberContextConfiguration
@SpringBootTest(classes = {FileuploaderApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberOptions(plugin = {"pretty"}, tags = "", features ="src/test/resources/feature")
@TestPropertySource(locations="classpath:test.application.properties")
public class CucumberIT {
}