package reporting;

/*import com.gemini.generic.reporting.GemTestReporter;
import com.gemini.generic.reporting.STATUS;*/
public class EnableGemReporting implements Reporting {
    @Override
    public void reportSteps(String title, String description) {
//        GemTestReporter.addTestStep(title, description, STATUS.valueOf("STATUS." + status));
    }
}