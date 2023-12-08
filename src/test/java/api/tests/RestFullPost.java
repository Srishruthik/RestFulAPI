package api.tests;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import api.Helper.Constants;
import api.base.BaseAPI;
import api.dataProvider.Testdata;
import api.pojos.PostClass;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestFullPost {
    BaseAPI baseAPI = new BaseAPI();

    @Test(description = "Test with year 2000", dataProvider = "data-provider", dataProviderClass = Testdata.class)
    public void testResponse(String desc, int year, double price, String modal, String size) throws IOException {
        // String url = "https://api.restful-api.dev/objects";
        String url = Constants.BASE_URL;
        String jsonFilePath = Constants.PAYLOAD_PATH;
        String payload = baseAPI.getUpdatedPayload(jsonFilePath, year, price, modal, size);
        Response response = baseAPI.getResponse(url, payload);

        //print response
        // System.out.println("response " + response.getBody().asString());

        Assert.assertEquals(response.getStatusCode(), 200);

        int getYear = response.path("data.year");

        float getpriceApi = response.path("data.price");
        //this is for round the decimal value has to go in until

        DecimalFormat df = new DecimalFormat("#.##");
        float getprice = Float.parseFloat(df.format(getpriceApi));
        float expPrice = Float.parseFloat(df.format(price));

        Assert.assertEquals(getYear, year);
        Assert.assertEquals(getprice, expPrice, "eronh");
        Assert.assertEquals(response.jsonPath().getString("data.'CPU model'"), modal);
        Assert.assertEquals(response.jsonPath().getString("data.'Hard disk size'"), size);
    }
}