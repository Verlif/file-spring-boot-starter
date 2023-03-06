package idea.verlif.spring.file.impl;

import idea.verlif.easy.file.domain.FileHolder;
import idea.verlif.easy.file.page.FilePage;
import idea.verlif.easy.file.page.FileQuery;
import idea.verlif.easy.file.util.File64Util;
import idea.verlif.spring.file.FileConfig;
import idea.verlif.spring.file.FileDomain;
import idea.verlif.spring.file.FileService;
import idea.verlif.spring.file.domain.FileInfo;
import idea.verlif.spring.file.domain.FileInfoPage;
import idea.verlif.spring.file.domain.FileUpload;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Verlif
 * @version 1.0
 */
public class DefaultFileService implements FileService {

    protected final FileConfig pathConfig;

    public DefaultFileService(FileConfig config) {
        this.pathConfig = config;
    }

    /**
     * 获取本地文件夹地址
     *
     * @param fileDomain 文件域
     * @return 本地文件地址
     */
    @Override
    public File getLocalFile(FileDomain fileDomain) {
        String path = fileDomain.getPath();
        if (!path.endsWith(FileConfig.DIR_SPLIT)) {
            path = path + FileConfig.DIR_SPLIT;
        }
        return new File(pathConfig.getMain() + path);
    }

    /**
     * 获取外网可访问的文件地址
     *
     * @param fileDomain 文件域
     * @param fileName   文件名
     * @return 文件地址
     */
    @Override
    public String getRealPath(FileDomain fileDomain, String fileName) {
        String path = fileDomain.getPath();
        if (!path.endsWith(FileConfig.DIR_SPLIT)) {
            path += FileConfig.DIR_SPLIT;
        }
        return pathConfig.getMain() + path + fileName;
    }

    /**
     * 获取文件列表
     *
     * @param query      文件查询条件
     * @param fileDomain 文件域
     * @return 文件列表信息
     */
    @Override
    public FileInfoPage getFileList(FileDomain fileDomain, FileQuery query) {
        return getInfoList(fileDomain, query);
    }

    protected FileInfoPage getInfoList(FileDomain fileDomain, FileQuery query) {
        // 获取文件夹对象
        File file = getLocalFile(fileDomain);
        FileHolder holder = new FileHolder(file);
        // 对不存在的目录进行处理
        FilePage filePage = holder.queryPage(query);
        if (filePage.getFiles() == null) {
            filePage.setFiles(new File[0]);
        }
        return new FileInfoPage(filePage, pathConfig.getMain());
    }

    /**
     * 构建Info对象
     *
     * @param file 文件对象
     * @param cart 文件域
     * @return 文件对应的Info对象
     */
    protected FileInfo buildInfo(FileDomain cart, File file) {
        return new FileInfo(file, cart.getPath());
    }

    @Override
    public int uploadFile(FileDomain fileDomain, MultipartFile... files) throws IOException {
        if (files == null || files.length == 0) {
            return 0;
        }
        File dirFile = getLocalFile(fileDomain);
        // 创建目标文件域
        if (!dirFile.exists()) {
            if (!dirFile.mkdirs()) {
                return 0;
            }
        }
        int count = 0;
        for (MultipartFile file : files) {
            String name = file.getOriginalFilename();
            // 没有文件时跳过
            if (name == null) {
                continue;
            }
            File dir = new File(dirFile, name);
            // 当不允许覆盖且文件已存在时跳过
            if (dir.exists() && pathConfig.isIgnored()) {
                continue;
            }
            file.transferTo(dir);
            count++;
        }
        return count;
    }

    @Override
    public boolean uploadFile(FileDomain fileDomain, MultipartFile file, String filename) throws IOException {
        if (file == null) {
            return false;
        }
        File dir = createFile(fileDomain, filename);
        if (dir == null) {
            return false;
        }
        file.transferTo(dir);
        return true;
    }

    @Override
    public boolean uploadFile(FileDomain fileDomain, FileUpload upload, String filename) throws IOException {
        if (upload.getFile() == null) {
            return false;
        }
        File target = createFile(fileDomain, filename);
        if (target == null) {
            return false;
        }
        File64Util.toFile(upload.getFile(), target);
        return true;
    }

    private File createFile(FileDomain fileDomain, String filename) {
        File dirFile = getLocalFile(fileDomain);
        // 创建目标文件域
        if (!dirFile.exists()) {
            if (!dirFile.mkdirs()) {
                return null;
            }
        }
        File target = new File(dirFile, filename);
        // 当不允许覆盖且文件已存在时不保存
        if (target.exists() && pathConfig.isIgnored()) {
            return null;
        }
        return target;
    }

    /**
     * 下载文件
     *
     * @param response   服务器响应对象
     * @param fileDomain 目标文件所在文件域
     * @param filename   目标文件名
     * @return 下载结果
     */
    @Override
    public boolean downloadFile(HttpServletResponse response, FileDomain fileDomain, String filename) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-disposition", "attachment; fileName=" + filename);
        File file = new File(getLocalFile(fileDomain), filename);
        if (file.exists()) {
            try (
                    FileInputStream fis = new FileInputStream(file);
                    OutputStream os = response.getOutputStream()
            ) {
                byte[] b = new byte[1024];
                int length;
                while ((length = fis.read(b)) > 0) {
                    os.write(b, 0, length);
                }
                os.flush();
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param fileDomain 目标文件所在文件域
     * @param fileName   目标文件名
     * @return 删除结果
     */
    @Override
    public boolean deleteFile(FileDomain fileDomain, String fileName) {
        File file;
        if (fileName == null) {
            file = getLocalFile(fileDomain);
        } else {
            file = new File(getLocalFile(fileDomain), fileName);
        }
        if (file.exists()) {
            if (file.isFile()) {
                return file.delete();
            } else {
                return deleteFiles(file);
            }
        } else {
            return true;
        }
    }

    /**
     * 删除文件
     *
     * @param file 目标文件
     */
    protected boolean deleteFiles(File file) {
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteFiles(f);
                }
            }
            return file.delete();
        } else {
            return true;
        }
    }

    /**
     * 备份文件夹下所有文件
     *
     * @param dirPath 文件夹路径
     * @return 是否备份成功
     */
    protected boolean moveToBack(String dirPath) {
        return moveToBack(new File(dirPath));
    }

    /**
     * 备份文件夹下所有文件（只会备份文件，不会备份文件夹）
     *
     * @param dirFile 文件夹对象
     * @return 是否备份成功
     */
    protected boolean moveToBack(File dirFile) {
        if (!dirFile.isDirectory()) {
            return false;
        }
        // 创建备份文件夹
        File backup = getBackFile(dirFile);
        if (!backup.exists()) {
            if (!backup.mkdirs()) {
                return false;
            }
        }
        // 备份文件夹存在则开始备份
        if (backup.exists()) {
            // 获取目标文件夹下的所有文件
            File[] oldFiles = dirFile.listFiles();
            if (oldFiles != null) {
                // 遍历目标文件
                for (File file : oldFiles) {
                    // 对可用的文件对象进行操作
                    if (file.exists() && file.isFile()) {
                        // 生成目标文件的备份文件对象
                        File backFile = new File(backup, file.getName());
                        // 备份已存在时，删除旧备份
                        if (backFile.exists()) {
                            if (backFile.delete()) {
                                if (!file.renameTo(backFile)) {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    protected void deleteBackFile(String path) {
        deleteFiles(getBackFile(path));
    }

    protected void deleteBackFile(File file) {
        deleteFiles(getBackFile(file));
    }

    protected File getBackFile(String dirPath) {
        return new File(dirPath, ".back");
    }

    protected File getBackFile(File dirFile) {
        return new File(dirFile, ".back");
    }
}
