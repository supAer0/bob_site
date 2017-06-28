import models.GroupProduct;
import models.Product;
import org.junit.Test;
import play.libs.F.Callback;
import play.test.TestBrowser;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

public class IntegrationTest {

    /**
     * add your integration test here
     * in this example we just check if the welcome page is being shown
     */
    @Test
    public void test() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                browser.goTo("http://localhost:3333");
                assertThat(browser.pageSource()).contains("Your new application is ready.");
            }
        });
    }
    @Test
    public void test1() {
        Product product1 = new Product("product1");
        Product product2 = new Product("product2");
        GroupProduct group1 = new GroupProduct("group1");
        product1.setGroup(group1);
        product2.setGroup(group1);

        GroupProduct group11 = new GroupProduct("group1");
        GroupProduct group2 = new GroupProduct("group1");

    }

}
