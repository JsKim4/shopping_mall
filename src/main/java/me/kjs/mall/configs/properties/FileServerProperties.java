package me.kjs.mall.configs.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.kjs.mall.connect.UploadPath;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("kjs-mall.file-upload")
@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileServerProperties {

    private String uploadPath;

    public String getUploadPath(UploadPath uploadPath) {
        return this.uploadPath + uploadPath.getUrl();
    }
}
