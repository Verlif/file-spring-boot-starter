package idea.verlif.file.service.impl;

import idea.verlif.file.service.FileConfig;
import idea.verlif.file.service.FileService;
import idea.verlif.file.service.domain.FileCart;
import idea.verlif.file.service.domain.FileInfo;
import idea.verlif.file.service.domain.FilePage;
import idea.verlif.file.service.domain.FileQuery;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/9/13 10:27
 */
public class DefaultFileService implements FileService {

    protected final FileConfig pathConfig;

    public DefaultFileService(FileConfig config) {
        this.pathConfig = config;
    }

    /**
     * 获取本地文件夹地址
     *
     * @param fileCart 文件域
     * @param type     文件子目录；可为空
     * @return 本地文件地址
     */
    @Override
    public File getLocalFile(FileCart fileCart, String type) {
        String path = fileCart.getArea();
        if (!path.endsWith(FileConfig.DIR_SPLIT)) {
            path = path + FileConfig.DIR_SPLIT;
        }
        return new File(pathConfig.getMain() + path + (type == null ? "" : type));
    }

    /**
     * 获取外网可访问的文件地址
     *
     * @param fileCart 文件域
     * @param type     文件子路径；可为空
     * @param fileName 文件名
     * @return 文件地址
     */
    @Override
    public String getRealPath(FileCart fileCart, String type, String fileName) {
        String path = fileCart.getArea();
        if (!path.endsWith(FileConfig.DIR_SPLIT)) {
            path += FileConfig.DIR_SPLIT;
        }
        return pathConfig.getMain() + path + (type == null ? "" : type) + FileConfig.DIR_SPLIT + fileName;
    }

    /**
     * 获取文件列表
     *
     * @param query    文件查询条件
     * @param fileCart 文件域
     * @param type     文件自目录；可为空
     * @return 文件列表信息
     */
    @Override
    public FilePage getFileList(FileCart fileCart, String type, FileQuery query) {
        return getInfoList(fileCart, type, query);
    }

    protected FilePage getInfoList(FileCart fileCart, String type, FileQuery query) {
        List<FileInfo> list = new ArrayList<>();
        // 获取文件夹对象
        File file = getLocalFile(fileCart, type);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    // 遍历文件夹内的文件对象，载入列表
                    for (File f : files) {
                        FileInfo info = buildInfo(f);
                        list.add(info);
                    }
                }
            }
        }
        // 开始过滤操作
        List<FileInfo> filterList;
        if (query.getName() != null) {
            filterList = list.stream()
                    .filter(fileInfo -> (fileInfo.getFileName().contains(query.getName())))
                    .sorted((o1, o2) -> (int) (o2.getUpdateTime().getTime() - o1.getUpdateTime().getTime()))
                    .collect(Collectors.toList());
        } else {
            filterList = list.stream()
                    .sorted((o1, o2) -> (int) (o2.getUpdateTime().getTime() - o1.getUpdateTime().getTime()))
                    .collect(Collectors.toList());
        }
        // 排序操作
        if (query.getOrder() != null) {
            Stream<FileInfo> stream = filterList.stream();
            switch (query.getOrder()) {
                case NAME:
                    filterList = stream.sorted((o1, o2) -> query.isAsc() ?
                            o1.getFileName().charAt(0) - o2.getFileName().charAt(0) :
                            o2.getFileName().charAt(0) - o1.getFileName().charAt(0)).collect(Collectors.toList());
                    break;
                case SIZE:
                    filterList = stream.sorted((o1, o2) -> (int) (query.isAsc() ?
                                    o1.getSize() - o2.getSize() :
                                    o2.getSize() - o1.getSize()))
                            .collect(Collectors.toList());
                    break;
                case SUFFIX:
                    filterList = stream.sorted((o1, o2) -> query.isAsc() ?
                                    o1.getSuffix().charAt(0) - o2.getSuffix().charAt(0) :
                                    o2.getSuffix().charAt(0) - o1.getSuffix().charAt(0))
                            .collect(Collectors.toList());
                    break;
                case UPDATE_TIME:
                    filterList = stream.sorted((o1, o2) -> (int) (query.isAsc() ?
                                    o1.getUpdateTime().getTime() - o2.getUpdateTime().getTime() :
                                    o2.getUpdateTime().getTime() - o1.getUpdateTime().getTime()))
                            .collect(Collectors.toList());
                    break;
                default:
            }
        }
        list.clear();
        list.addAll(filterList);
        // 创建分页对象
        return page(list, query);
    }

    /**
     * 构建Info对象
     *
     * @param file 文件对象
     * @return 文件对应的Info对象
     */
    protected FileInfo buildInfo(File file) {
        FileInfo info = new FileInfo();
        String name = file.getName();
        info.setFileName(name);
        info.setUpdateTime(new Date(file.lastModified()));
        info.setSize(file.length());

        int suf = name.lastIndexOf(".");
        if (suf != -1) {
            info.setSuffix(name.substring(suf));
        }
        return info;
    }

    protected FilePage page(List<FileInfo> list, FileQuery query) {
        FilePage page = new FilePage();
        page.setTotal(list.size());
        page.setSize(query.getPage());
        page.setPages(page.getTotal() / page.getSize());
        if (page.getTotal() % page.getSize() > 0) {
            page.setPages(page.getPages() + 1);
        }
        page.setCurrent(query.getNum());
        if (query.getPageHead() > list.size()) {
            page.setInfos(new ArrayList<>());
        } else {
            int end = query.getPageHead() + query.getPage();
            page.setInfos(list.subList(query.getPageHead(), Math.min(end, list.size())));
        }
        return page;
    }

    @Override
    public int uploadFile(FileCart fileCart, String type, MultipartFile... files) throws IOException {
        if (files == null || files.length == 0) {
            return 0;
        }
        File dirFile = getLocalFile(fileCart, type);
        // 创建目标文件域
        if (!dirFile.exists()) {
            if (!dirFile.mkdirs()) {
                return 0;
            }
        }
        int count = 0;
        for (MultipartFile file : files) {
            String name = file.getOriginalFilename();
            if (name == null) {
                continue;
            }
            File dir = new File(dirFile, name);
            file.transferTo(dir);
            count++;
        }
        return count;
    }

    @Override
    public boolean uploadFile(FileCart fileCart, String type, MultipartFile file, String filename) throws IOException {
        if (file == null) {
            return false;
        }
        File dirFile = getLocalFile(fileCart, type);
        // 创建目标文件域
        if (!dirFile.exists()) {
            if (!dirFile.mkdirs()) {
                return false;
            }
        }
        File dir = new File(dirFile, filename);
        file.transferTo(dir);
        return true;
    }

    /**
     * 下载文件
     *
     * @param response 服务器响应对象
     * @param fileCart 目标文件所在文件域
     * @param type     文件子目录；可为空
     * @param fileName 目标文件名
     * @return 下载结果
     */
    @Override
    public boolean downloadFile(HttpServletResponse response, FileCart fileCart, String type, String fileName) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-disposition", "attachment; fileName=" + fileName);
        File file = new File(getLocalFile(fileCart, type), fileName);
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
     * @param fileCart 目标文件所在文件域
     * @param type     文件子目录；可为空
     * @param fileName 目标文件名
     * @return 删除结果
     */
    @Override
    public boolean deleteFile(FileCart fileCart, String type, String fileName) {
        File file = new File(getLocalFile(fileCart, type), fileName);
        if (file.exists() && file.isFile()) {
            return file.delete();
        } else {
            return false;
        }
    }

    /**
     * 删除文件
     *
     * @param file 目标文件
     */
    protected void deleteFiles(File file) {
        if (file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteFiles(f);
                }
            }
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

    protected File getBackFile(String dirPath) {
        return new File(dirPath, ".back");
    }

    protected File getBackFile(File dirFile) {
        return new File(dirFile, ".back");
    }
}
