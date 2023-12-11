package api.base;

import api.pojos.PostClass;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;

public class BaseAPI {

    /**
     * @param jsonFilePath
     * @param year
     * @param price
     * @param modal
     * @param disSize
     * @return
     * @throws IOException
     */
    public String getUpdatedPayload(String jsonFilePath, int year, double price, String modal, String disSize) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File(jsonFilePath);

        //convert json file to object
        PostClass postClass = objectMapper.readValue(jsonFile, PostClass.class);
        String payload = objectMapper.writeValueAsString(postClass);
        // System.out.println(payload);
        postClass.getData().setYear(year);
        postClass.getData().setPrice(price);
        postClass.getData().setCpuModel(modal);
        postClass.getData().setHardDiskSize(disSize);

        //convert object to json string
        String updatePayload = objectMapper.writeValueAsString(postClass);
        return updatePayload;
    }

    /**
     * @param url
     * @param payload
     * @return
     * @throws IOException
     */
    public Response getPostResponse(final String url, final String payload) throws IOException {
        //create request
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(payload);
        //send POST request
        Response response = request.post(url);
        return response;
    }

    /**
     * @param url
     * @param response
     * @param shemaPath
     * @return
     * @throws IOException
     * @throws ProcessingException
     */
    public String getShemaValidationReport(String response, final String shemaPath) throws IOException, ProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode schema = objectMapper.readValue(new File(shemaPath), JsonNode.class);
        JsonNode data = objectMapper.readTree(response);
        JsonSchema factory = JsonSchemaFactory.byDefault().getJsonSchema(schema);
        ProcessingReport report = factory.validate(data);
        return report.toString();
    }

    /**
     * @param response
     * @param shemaPath
     * @throws IOException
     * @throws ProcessingException
     */

    public void ValidateResponseSchema(String response, final String shemaPath) throws IOException, ProcessingException {
        String shcemareport = getShemaValidationReport(response, shemaPath);
        if (shcemareport.contains("SUCCESS")) {
            Assert.assertTrue(true, shcemareport);
        } else if (shcemareport.contains("failure")) {
            Assert.assertTrue(false, shcemareport);
        }
    }
}