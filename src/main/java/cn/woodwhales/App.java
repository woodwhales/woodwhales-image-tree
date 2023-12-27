package cn.woodwhales;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Hello world!
 *
 */
public class App {

    private static final String site = "https://image.woodwhales.cn";

    public static void main( String[] args ) throws Exception {
        tree();
    }

    private static Set<String> ignoreSet;

    static {
        ignoreSet = new HashSet<>();
        ignoreSet.add("index.html");
        ignoreSet.add("README.md");
        ignoreSet.add(".history");
        ignoreSet.add(".idea");
        ignoreSet.add("tree.json");
        ignoreSet.add(".git");
        ignoreSet.add("woodwhales-image-tree-1.0.0-jar-with-dependencies.jar");
    }

    public static void tree() throws Exception {
        Path jarPath = Paths.get(App.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        Path jarParentPath = jarPath.getParent();
        File baseFile = jarParentPath.toFile();
        String baseFilePath = baseFile.getAbsolutePath();

        System.out.println("parse start, baseFilePath : " + baseFilePath);

        List<ImageDto> imageDtoList = new ArrayList<>();
        for (File file : baseFile.listFiles()) {
            String fileName = file.getName();
            if (file.isFile() || ignoreSet.contains(fileName)) {
                continue;
            }

            ImageDto imageDto = new ImageDto();
            imageDtoList.add(imageDto);
            imageDto.setName(fileName);
            imageDto.setArticleUrl("https://woodwhales.cn/");
            imageDto.setBasePath(fileName);
            List<ImageDto.FileDto> files = new ArrayList<>();
            imageDto.setFiles(files);
            List<File> subFiles = FileUtil.loopFiles(file);
            if(Objects.nonNull(subFiles) && !subFiles.isEmpty()) {
                for (File subFile : subFiles) {
                    String absolutePath = subFile.getAbsolutePath();
                    String subUrl = StringUtils.replace(StringUtils.substringAfter(absolutePath, baseFilePath), File.separator, "/");
                    files.add(new ImageDto.FileDto(subFile.getName(), site + subUrl));
                }
            }
        }
        FileUtil.writeString(JSON.toJSONString(imageDtoList, JSONWriter.Feature.PrettyFormat), new File(baseFilePath + File.separator + "tree.json"), UTF_8);
        System.out.println("parse end");
    }
}
