package reporting;

import com.gemini.generic.reporting.GemTestReporter;
import com.gemini.generic.reporting.STATUS;

/**
 * Ver 1.0.0
 * Author: Sparsh Sondhi, Charu Srivastava, Touqeer Subhani
 **/
public class ReportUtilsGemImpl implements ReportUtils {

    public void addStepToReport(String title, String description) {
        GemTestReporter.addTestStep(title, description, STATUS.PASS);
    }

    public void addStepToReport(String title, String description, String status) {
        GemTestReporter.addTestStep(title, description, STATUS.valueOf(status.toUpperCase()));
    }
}