package api.tests;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import api.Helper.Constants;
import api.base.BaseAPI;
import api.dataProvider.Testdata;
import api.pojos.PostClass;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestFullPost {
    BaseAPI baseAPI = new BaseAPI();

    @Test(description = "Test with year 2000", dataProvider = "data-provider", dataProviderClass = Testdata.class)
    public void testResponse(String desc, int year, double price, String modal, String size) throws IOException, ProcessingException {
        // String url = "https://api.restful-api.dev/objects";
        final String url = Constants.BASE_URL;
        final String jsonFilePath = Constants.PAYLOAD_PATH;
        final String payload = baseAPI.getUpdatedPayload(jsonFilePath, year, price, modal, size);
       // System.out.println(payload);
        Response response = baseAPI.getPostResponse(url, payload);

        Assert.assertEquals(response.getStatusCode(), 200);
        //Schema validation
        final String schemaPath = Constants.SCHEMA_PATH;
        final String getResponse = response.getBody().asString();
        baseAPI.ValidateResponseSchema(getResponse, schemaPath);

        //functional validation
        final int getYear = response.path("data.year");
        final float getpriceApi = response.path("data.price");

        //this is for round the decimal value has to go in until
        DecimalFormat df = new DecimalFormat("#.##");
        final float getprice = Float.parseFloat(df.format(getpriceApi));
        final float expPrice = Float.parseFloat(df.format(price));

        // test condition validations using data provider
        Assert.assertEquals(getYear, year);
        Assert.assertEquals(getprice, expPrice, "eronh");
        Assert.assertEquals(response.jsonPath().getString("data.'CPU model'"), modal);
        Assert.assertEquals(response.jsonPath().getString("data.'Hard disk size'"), size);
    }
}