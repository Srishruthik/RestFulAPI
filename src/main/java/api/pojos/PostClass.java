package api.pojos;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostClass {
    private String name;
    private DataDetails data;
    @Data
    public static class DataDetails {
        private int year;
        private double price;
        @JsonProperty("CPU model")
        private String cpuModel;
        @JsonProperty("Hard disk size")
        private String hardDiskSize;
    }
}
