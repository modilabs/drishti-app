package org.ei.drishti.repository;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import org.ei.drishti.domain.Alert;
import org.ei.drishti.domain.VillageAlertSummary;
import org.ei.drishti.util.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;

public class AlertRepositoryTest extends AndroidTestCase {
    private AlertRepository alertRepository;

    @Override
    protected void setUp() throws Exception {
        alertRepository = new AlertRepository();
        Session session = new Session().setPassword("password").setRepositoryName("drishti.db" + new Date().getTime());
        new Repository(new RenamingDelegatingContext(getContext(), "test_"), session, alertRepository);
        alertRepository.deleteAllAlerts();
    }

    public void testShouldSaveAnAlert() throws Exception {
        alertRepository.createAlert(new Alert("Case X", "Theresa", "bherya", "ANC 1", "Thaayi 1", 1, "2012-01-01", "2012-01-11"));
        List<Alert> alerts = alertRepository.allAlerts();

        assertEquals(asList(new Alert("Case X", "Theresa", "bherya", "ANC 1", "Thaayi 1", 1, "2012-01-01", "2012-01-11")), alerts);
    }

    public void testShouldFetchAllAlerts() throws Exception {
        Alert alert1 = new Alert("Case X", "Theresa 1", "bherya", "ANC 1", "Thaayi 1", 1, "2012-01-01", "2012-01-11");
        Alert alert2 = new Alert("Case Y", "Theresa 2", "bherya", "ANC 2", "Thaayi 2", 1, "2012-01-01", "2012-01-11");
        Alert alert3 = new Alert("Case X", "Theresa 1", "bherya", "TT 1", "Thaayi 1", 1, "2012-01-01", "2012-01-11");
        Alert alert4 = new Alert("Case Y", "Theresa 2", "bherya", "IFA 1", "Thaayi 2", 1, "2012-01-01", "2012-01-11");

        alertRepository.createAlert(alert1);
        alertRepository.createAlert(alert2);
        alertRepository.createAlert(alert3);
        alertRepository.createAlert(alert4);

        assertEquals(asList(alert1, alert2, alert3, alert4), alertRepository.allAlerts());
    }

    public void testShouldFetchAllAlertsForAVillage() throws Exception {
        Alert alert1 = new Alert("Case X", "Theresa 1", "bherya1", "ANC 1", "Thaayi 1", 1, "2012-01-01", "2012-01-11");
        Alert alert2 = new Alert("Case Y", "Theresa 2", "bherya2", "ANC 2", "Thaayi 2", 1, "2012-01-01", "2012-01-11");
        Alert alert3 = new Alert("Case X", "Theresa 1", "bherya1", "TT 1", "Thaayi 1", 1, "2012-01-01", "2012-01-11");
        Alert alert4 = new Alert("Case Y", "Theresa 2", "bherya2", "IFA 1", "Thaayi 2", 1, "2012-01-01", "2012-01-11");

        alertRepository.createAlert(alert1);
        alertRepository.createAlert(alert2);
        alertRepository.createAlert(alert3);
        alertRepository.createAlert(alert4);

        assertEquals(asList(alert1, alert3), alertRepository.allAlertsFor("bherya1"));
        assertEquals(asList(alert2, alert4), alertRepository.allAlertsFor("bherya2"));
    }

    public void testShouldFetchSummaryAllUniqueLocations() throws Exception {
        alertRepository.createAlert(new Alert("Case X", "Theresa 1", "Bherya 1", "ANC 1", "Thaayi 1", 1, "2012-01-01", "2012-01-11"));
        alertRepository.createAlert(new Alert("Case Y", "Theresa 2", "Bherya 2", "ANC 2", "Thaayi 2", 1, "2012-01-01", "2012-01-11"));
        alertRepository.createAlert(new Alert("Case Z", "Theresa 3", "Bherya 1", "TT 1", "Thaayi 3", 1, "2012-01-01", "2012-01-11"));
        alertRepository.createAlert(new Alert("Case A", "Theresa 4", "Bherya 3", "IFA 1", "Thaayi 4", 1, "2012-01-01", "2012-01-11"));

        assertEquals(asList(new VillageAlertSummary("Bherya 1", 2), new VillageAlertSummary("Bherya 2", 1), new VillageAlertSummary("Bherya 3", 1)), alertRepository.summary());
    }

    public void testShouldDeleteAlertsBasedOnCaseIDAndVisitCode() throws Exception {
        alertRepository.createAlert(new Alert("Case X", "Theresa", "bherya", "ANC 1", "Thaayi 1", 1, "2012-01-01", "2012-01-11"));
        alertRepository.createAlert(new Alert("Case Y", "SomeOtherWoman", "bherya", "ANC 2", "Thaayi 2", 1, "2012-01-01", "2012-01-11"));

        alertRepository.deleteAlertsForVisitCodeOfCase("Case X", "ANC 1");

        assertEquals(asList(new Alert("Case Y", "SomeOtherWoman", "bherya", "ANC 2", "Thaayi 2", 1, "2012-01-01", "2012-01-11")), alertRepository.allAlerts());
    }

    public void testShouldNotFailDeletionWhenNothingToDeleteExists() throws Exception {
        alertRepository.deleteAlertsForVisitCodeOfCase("Case X", "ANC 1");

        assertTrue(alertRepository.allAlerts().isEmpty());
    }

    public void testShouldDeleteAllAlertsForACase() throws Exception {
        alertRepository.createAlert(new Alert("Case X", "Theresa", "bherya", "ANC 1", "Thaayi 1", 1, "2012-01-01", "2012-01-11"));
        alertRepository.createAlert(new Alert("Case X", "Theresa", "bherya", "ANC 2", "Thaayi 1", 1, "2012-01-01", "2012-01-11"));
        alertRepository.createAlert(new Alert("Case Y", "SomeOtherWoman", "bherya", "ANC 2", "Thaayi 2", 1, "2012-01-01", "2012-01-11"));
        alertRepository.createAlert(new Alert("Case X", "Theresa", "bherya", "ANC 3", "Thaayi 1", 1, "2012-01-01", "2012-01-11"));

        alertRepository.deleteAllAlertsForCase("Case X");

        assertEquals(asList(new Alert("Case Y", "SomeOtherWoman", "bherya", "ANC 2", "Thaayi 2", 1, "2012-01-01", "2012-01-11")), alertRepository.allAlerts());
    }

    public void testShouldDeleteAllAlerts() throws Exception {
        alertRepository.createAlert(new Alert("Case X", "Theresa", "bherya", "ANC 1", "Thaayi 1", 1, "2012-01-01", "2012-01-11"));
        alertRepository.deleteAllAlerts();
        assertEquals(new ArrayList<Alert>(), alertRepository.allAlerts());
    }
}
