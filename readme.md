# FileService

简单的文件管理系统  
通过`FileService`来使用以下功能：

* 文件上传（字节流与Base64）
* 上传文件后缀限制（allowed名单与blocked名单）
* 文件下载（字节流与Base64）
* 文件查询（分页、排序、文件名过滤等）
* 文件删除

## 使用

1. 启用服务

    在任意配置类上使用`@EnableFileService`注解启用文件管理服务

2. 使用`FileService`

    在上传、下载文件时，可以使用`FileService`来完成。
    
    ```java
    @RestController
    @RequestMapping("/file")
    @Api(tags = "文件管理")
    public class FileController {
    
        @Autowired
        private FileService fileService;
    
        @PostMapping
        @Operation(summary = "上传文件")
        public int uploadFile(MultipartFile[] files) {
            return fileService.uploadFile(new FileCart("test"), SecurityUtils.getUsername(), files);
        }
    
        @GetMapping
        @Operation(summary = "下载文件")
        public boolean downloadFile(
                @RequestParam String fileName,
                HttpServletResponse response
        ) {
            return fileService.downloadFile(response, new FileCart("test"), fileName);
        }
    
        @GetMapping("/list")
        @Operation(summary = "文件列表")
        public FileInfoPage getFileList(FileQuery fileQuery) {
            return fileService.getFileList(new FileCart("test"), fileQuery);
        }
    
        @DeleteMapping
        @Operation(summary = "删除文件")
        public boolean deleteFile(@RequestParam String filename) {
            return fileService.deleteFile(new FileCart("test"), filename);
        }
    }
    ```

## 引入依赖

1. 添加Jitpack仓库源
    
    maven

    ```xml
    <repositories>
       <repository>
           <id>jitpack.io</id>
           <url>https://jitpack.io</url>
       </repository>
    </repositories>
    ```

2. 添加依赖
    
    maven

    ```xml
    <dependencies>
        <dependency>
            <groupId>com.github.Verlif</groupId>
            <artifactId>file-spring-boot-starter</artifactId>
            <version>2.6.14-0.1</version>
        </dependency>
    </dependencies>
    ```

   __lastVersion__ [![](https://jitpack.io/v/Verlif/file-spring-boot-starter.svg)](https://jitpack.io/#Verlif/file-spring-boot-starter)

## 配置文件

可以在`application.yml`中配置`FileService`的一些参数：

```yaml
station:
  file:
    # 是否允许新上传的文件覆盖已有的同名文件
    cover: false
    path:
      # 文件系统根目录，相对路径或绝对路径
      main: F:/upload
    allowed: jpg, png # 允许上传的文件后置，存在值时只允许设定值上传。忽略大小写。
    blocked: exe # 不允许上传的文件后缀，blocked优先级大于allowed
```

## 说明

* `FileDomain`表示了文件域，也就是文件夹。**不**允许为空。
