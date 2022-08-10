package idea.verlif.spring.file;

import idea.verlif.spring.file.impl.DefaultFileService;
import idea.verlif.spring.file.util.FilePathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/9/13 10:16
 */
@Configuration
@ConfigurationProperties(prefix = "station.file")
public class FileConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileConfig.class);

    /**
     * 文件路径分隔符
     */
    public static final String DIR_SPLIT = File.separator;

    /**
     * 是否允许覆盖同名文件
     */
    private boolean cover = false;

    /**
     * 路径配置
     */
    private Path path;

    public FileConfig() {
        path = new Path();
    }

    /**
     * 获取文件根路径
     *
     * @return 文件系统根路径
     */
    public String getMain() {
        return path.getMain();
    }

    @Bean
    @ConditionalOnMissingBean(FileService.class)
    public FileService fileService() {
        return new DefaultFileService(this);
    }

    /**
     * 是否忽略已存在同名文件
     */
    public boolean isIgnored() {
        return !cover;
    }

    public boolean isCover() {
        return cover;
    }

    public void setCover(boolean cover) {
        this.cover = cover;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public static final class Path {

        /**
         * 文件系统根路径
         */
        private String main = DIR_SPLIT + "upload" + DIR_SPLIT;

        public void setMain(String main) {
            String path = FilePathUtil.beautyPath(main);
            if (!path.endsWith(DIR_SPLIT)) {
                this.main = path + DIR_SPLIT;
            } else {
                this.main = path;
            }
            LOGGER.info("File service's main path is based on " + this.main);
        }

        public String getMain() {
            return main;
        }
    }
}
