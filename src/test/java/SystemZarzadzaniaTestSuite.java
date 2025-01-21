import model.FasadaAktualizacji_TestMockit;
import model.Przedmiot_Test;
import model.FasadaAktualizacji_Test;
import org.junit.platform.suite.api.*;
import view.WidokStudenta_Test;
import org.junit.jupiter.api.BeforeAll;

/**
 * Główny zestaw testów aplikacji
 */
@Suite
@SuiteDisplayName("System Zarządzania Zapisami - Testy")
@SelectClasses({
        FasadaAktualizacji_TestMockit.class,
        FasadaAktualizacji_Test.class,
        WidokStudenta_Test.class,
        Przedmiot_Test.class
})
@IncludeTags({"model", "view"})
@SelectPackages({"model", "view"})
public class SystemZarzadzaniaTestSuite {

    @BeforeAll
    public static void setUpTestSuite() {
        System.out.println("==========================================");
        System.out.println("Rozpoczęcie wykonywania zestawu testów");
        System.out.println("Testowane pakiety: model, view");
        System.out.println("==========================================");
    }
}