package outlook.main;



import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


@RunWith(Cucumber.class)

@CucumberOptions( 
format = { "pretty","html: cucumber-html-reports",
        "json: cucumber-html-reports/cucumber.json" },
features = {"src/main/resources/feature/"},
glue = "stepDefinition" ,
tags="@otc")

public class RunOtc {

}



