package reporting;

import net.serenitybdd.core.Serenity;

public class EnableSerenityReporting implements Reporting {

    @Override
    public void reportSteps(String title, String description) {
        Serenity.recordReportData()
                .withTitle(title)
                .andContents(description);
    }
}
