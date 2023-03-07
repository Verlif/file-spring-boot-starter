package idea.verlif.spring.file;

import idea.verlif.easy.file.page.FileQuery;
import idea.verlif.spring.file.domain.FileInfoPage;
import idea.verlif.spring.file.domain.FileUpload;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * 文件控制器，用于管理实体文件
 *
 * @author Verlif
 * @version 1.0
 */
public interface FileService {

    /**
     * 获取本地文件夹对象
     *
     * @param fileDomain 文件域
     * @return 文件夹对象
     */
    File getLocalFile(FileDomain fileDomain);

    /**
     * 获取外网可访问的文件地址
     *
     * @param fileDomain 文件域
     * @param fileName   文件名
     * @return 文件地址
     */
    String getRealPath(FileDomain fileDomain, String fileName);

    /**
     * 获取文件列表
     *
     * @param query      文件查询条件
     * @param fileDomain 文件域
     * @return 文件列表信息
     */
    FileInfoPage getFileList(FileDomain fileDomain, FileQuery query);

    /**
     * 上传文件
     *
     * @param fileDomain 文件域
     * @param files      目标文件组
     * @return 上传失败的文件列表。当有一个文件被限制上传时，所有文件都无法上传。
     */
    MultipartFile[] uploadFile(FileDomain fileDomain, MultipartFile... files) throws IOException;

    /**
     * 上传文件
     *
     * @param fileDomain 文件域
     * @param file       目标文件组
     * @param filename   储存的文件名
     * @return 上传成功的文件数量
     */
    boolean uploadFile(FileDomain fileDomain, MultipartFile file, String filename) throws IOException;

    /**
     * 上传文件
     *
     * @param fileDomain 文件域
     * @param upload     Base64信息
     * @return 上传成功的文件数量
     */
    boolean uploadFile(FileDomain fileDomain, FileUpload upload, String filename) throws IOException;

    /**
     * 下载文件
     *
     * @param response   服务器响应对象
     * @param fileDomain 目标文件所在文件域
     * @param filename   目标文件名
     * @return 是否下载成功
     */
    boolean downloadFile(HttpServletResponse response, FileDomain fileDomain, String filename) throws IOException;

    /**
     * 删除文件
     *
     * @param fileDomain 目标文件所在文件域
     * @param filename   目标文件名
     * @return 删除结果
     */
    boolean deleteFile(FileDomain fileDomain, String filename);
}
