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
import java.util.HashSet;
import java.util.Set;

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

    /**
     * 允许的后缀
     */
    private String allowed;

    private final Set<String> allowedSet;

    /**
     * 不允许的后缀
     */
    private String blocked;

    private final Set<String> blockedSet;

    public FileConfig() {
        path = new Path();
        allowedSet = new HashSet<>();
        blockedSet = new HashSet<>();
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

    public String getAllowed() {
        return allowed;
    }

    public void setAllowed(String allowed) {
        this.allowed = allowed;
        for (String s : allowed.split(",")) {
            allowedSet.add(s.trim().toUpperCase());
        }
    }

    public String getBlocked() {
        return blocked;
    }

    public void setBlocked(String blocked) {
        this.blocked = blocked;
        for (String s : blocked.split(",")) {
            blockedSet.add(s.trim().toUpperCase());
        }
    }

    public boolean isAllowed(String filename) {
        String suffix = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
        if (blockedSet.size() > 0 && blockedSet.contains(suffix)) {
            return false;
        }
        return allowedSet.size() == 0 || allowedSet.contains(suffix);
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
