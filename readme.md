# FileService

简单的文件管理系统  
通过`FileService`来使用以下功能：

* 文件上传（字节流与Base64）
* 文件下载（字节流与Base64）
* 文件查询（分页、排序、文件名过滤等）
* 文件删除

## 使用

1. 启用服务

    在任意配置类上使用`@EnableFileService`注解启用文件管理服务

2. 拓展使用

    默认的服务只包括简单的功能，没有包括诸如在线预览url、文件版本管理、备份策略等功能。  
    开发者可以通过自行实现或是继承`DefaultFileService`来进行拓展修改：
    
    ```java
    @Component
    public class FileServiceImpl extends DefaultFileService {
    
        public FileServiceImpl(@Autowired FileConfig config) {
            super(config);
        }
    
        @Override
        protected FileInfo buildInfo(File file) {
            // FileData为业务数据，继承了FileInfo
            return new FileData(file);
        }
    }
    ```
    
    注意，因为`DefaultFileService`实现了`FileService`接口，所以实际上，开发者需要实现的是`FileService`接口。推荐通过继承重写的方式来实现需要的功能。  
    可以通过自动注入来使用`FileConfig`配置

3. 使用`FileService`

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
            return fileService.downloadFile(response, new FileCart("test"), SecurityUtils.getUsername(), fileName);
        }
    
        @GetMapping("/list")
        @Operation(summary = "文件列表")
        public FilePage getFileList(FileQuery fileQuery) {
            return fileService.getFileList(new FileCart("test"), SecurityUtils.getUsername(), fileQuery);
        }
    
        @DeleteMapping
        @Operation(summary = "删除文件")
        public boolean deleteFile(@RequestParam String filename) {
            return fileService.deleteFile(new FileCart("test"), SecurityUtils.getUsername(), filename);
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
               <version>2.6.6-0.1</version>
           </dependency>
       </dependencies>
    ```

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
```

## 说明

* `FileCart`表示了文件域，也就是文件夹。**不**允许为空。
* `type`表示了文件域下的子文件夹。与`FileCart`分开的目的是为了做类型区分。允许为空。