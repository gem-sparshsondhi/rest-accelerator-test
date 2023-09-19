package reporting;

import net.serenitybdd.core.Serenity;

/**
 * Ver 1.0.0
 * Author: Sparsh Sondhi, Charu Srivastava, Touqeer Subhani
 **/

public class ReportUtilsSerenityImpl implements ReportUtils {

    public void addStepToReport(String title, String description) {
        Serenity.recordReportData()
                .withTitle(title)
                .andContents(description);
    }

    public void addStepToReport(String title, String description, String status) {
        Serenity.recordReportData()
                .withTitle(status.toUpperCase() + ": " + title)
                .andContents(description);
    }
}