package api.base;

import api.pojos.PostClass;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.IOException;

public class BaseAPI {

    public String getUpdatedPayload(String jsonFilePath, int year, double price, String modal, String disSize) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File(jsonFilePath);

        PostClass postClass = objectMapper.readValue(jsonFile, PostClass.class);
        String payload = objectMapper.writeValueAsString(postClass);


        postClass.getData().setYear(year);
        postClass.getData().setPrice(price);
        postClass.getData().setCpuModel(modal);
        postClass.getData().setHardDiskSize(disSize);

        String updatePayload = objectMapper.writeValueAsString(postClass);
        System.out.println(updatePayload);
        return updatePayload;
    }

    public Response getResponse(String url, String payload) throws IOException {
        // String payload = getUpdatedPayload();
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(payload);
        //send POST request
        Response response = request.post(url);
        return response;
    }

}
