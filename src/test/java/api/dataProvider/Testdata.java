package api.dataProvider;

import org.testng.annotations.DataProvider;

public class Testdata {

    @DataProvider(name = "data-provider")
    public Object[][] getData() {
        return new Object[][]{
                {"Test with year 2017",2017, 2017.17, "Intel Core 17", "1 TB"},
                {"Test with year 2018",2018, 2018.18, "Intel Core i8", "2 TB"},
                {"Test with year 2019",2019, 2019.19, "Intel Core i9", "3 TB"}
        };
    }
}
