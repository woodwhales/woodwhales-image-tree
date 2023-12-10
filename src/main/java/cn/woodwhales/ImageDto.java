package cn.woodwhales;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author woodwhales on 2023-12-09 21:37
 */
@Data
public class ImageDto {

    private String name;
    private String articleUrl;
    private String basePath;
    private List<FileDto> files;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FileDto {
        private String fileName;
        private String url;
    }

}
