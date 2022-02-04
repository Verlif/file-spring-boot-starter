package idea.verlif.file.service;

import idea.verlif.file.service.domain.FileCart;
import idea.verlif.file.service.domain.FilePage;
import idea.verlif.file.service.domain.FileQuery;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * 文件控制器，用于管理实体文件
 *
 * @author Verlif
 * @version 1.0
 * @date 2021/11/10 9:14
 */
public interface FileService {

    /**
     * 获取本地文件夹对象
     *
     * @param fileCart 文件域
     * @param type     子目录
     * @return 文件夹对象
     */
    File getLocalFile(FileCart fileCart, String type);

    /**
     * 获取外网可访问的文件地址
     *
     * @param fileCart 文件域
     * @param type     文件子路径；可为空
     * @param fileName 文件名
     * @return 文件地址
     */
    String getRealPath(FileCart fileCart, String type, String fileName);

    /**
     * 获取文件列表
     *
     * @param query    文件查询条件
     * @param fileCart 文件域
     * @param type     文件自目录；可为空
     * @return 文件列表信息
     */
    FilePage getFileList(FileCart fileCart, String type, FileQuery query);

    /**
     * 上传文件
     *
     * @param fileCart 文件域
     * @param type     文件子路径；可为空
     * @param files    目标文件组
     * @return 上传成功的文件数量
     */
    int uploadFile(FileCart fileCart, String type, MultipartFile... files) throws IOException;

    /**
     * 下载文件
     *
     * @param response 服务器响应对象
     * @param fileCart 目标文件所在文件域
     * @param type     文件子目录；可为空
     * @param fileName 目标文件名
     * @return 是否下载成功
     */
    boolean downloadFile(HttpServletResponse response, FileCart fileCart, String type, String fileName) throws IOException;

    /**
     * 删除文件
     *
     * @param fileCart 目标文件所在文件域
     * @param type     文件子目录；可为空
     * @param fileName 目标文件名
     * @return 删除结果
     */
    boolean deleteFile(FileCart fileCart, String type, String fileName);
}
