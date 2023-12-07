package api;

import java.io.File;
import java.io.IOException;

import api.pojos.PostClass;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestFullPost {


    public String getUpdatedPayload() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File("../ApiAutomation/src/main/java/api/Json/post.json");

        PostClass postClass = objectMapper.readValue(jsonFile, PostClass.class);
        String payload = objectMapper.writeValueAsString(postClass);
        System.out.println(payload);

        postClass.getData().setYear(2025);
        postClass.getData().setPrice(3000);
        postClass.getData().setCpuModel("Intel Core i9");
        postClass.getData().setHardDiskSize("1TB SSD");
        String updatePayload = objectMapper.writeValueAsString(postClass);

        return updatePayload;

    }

    @Test
    public void testRequest() throws IOException {
        String payload = getUpdatedPayload();
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(payload);
        //send POST request
        Response response = request.post("https://api.restful-api.dev/objects");
        //print response
        System.out.println(response.getBody().asString());
        //print status code
        System.out.println(response.getStatusCode());

        String id = response.jsonPath().getString("id");
        System.out.println(id);

        String createdAt = response.jsonPath().getString("createdAt");
        System.out.println(createdAt);

    }


    public Response getResponse(String url) throws IOException {
        String payload = getUpdatedPayload();
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(payload);
        //send POST request
        Response response = request.post(url);
        return response;
    }

    @Test
    public void testResponse() throws IOException {
        String url = "https://api.restful-api.dev/objects";
        Response response = getResponse(url);
        //print response
        System.out.println(response.getBody().asString());
        //print status code
        System.out.println(response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 200);
        String id = response.jsonPath().getString("id");
        System.out.println(id);

        String createdAt = response.jsonPath().getString("createdAt");
        System.out.println(createdAt);
    }
}


