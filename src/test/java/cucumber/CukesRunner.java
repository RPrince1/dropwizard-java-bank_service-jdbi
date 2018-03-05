package cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.Ignore;
import org.junit.runner.RunWith;

@Ignore
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources"
        ,glue= "steps"
)
public class CukesRunner {}