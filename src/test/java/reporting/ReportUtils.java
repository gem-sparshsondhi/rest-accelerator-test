package reporting;

/**
 * Ver 1.0.0
 * Author: Sparsh Sondhi, Charu Srivastava, Touqeer Subhani
 **/

public interface ReportUtils {
    void addStepToReport(String title, String description);
    void addStepToReport(String title, String description, String status);
}