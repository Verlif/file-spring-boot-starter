# FileService

简单的文件管理系统  
通过FileService来使用文件上传、下载、查询、删除等功能

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
        FileInfo info = super.buildInfo(file);
        // FileData为业务数据，继承了FileInfo
        return new FileData(info);
    }
}
```

注意，因为`DefaultFileService`实现了`FileService`接口，所以实际上，开发者需要实现的是`FileService`接口。  
可以通过自动注入来使用`FileConfig`配置

## 引入依赖

1. 添加Jitpack仓库源

> maven
> ```xml
> <repositories>
>    <repository>
>        <id>jitpack.io</id>
>        <url>https://jitpack.io</url>
>    </repository>
> </repositories>
> ```

2. 添加依赖

> maven
> ```xml
>    <dependencies>
>        <dependency>
>            <groupId>com.github.Verlif</groupId>
>            <artifactId>file-spring-boot-starter</artifactId>
>            <version>2.6.3-alpha0.1</version>
>        </dependency>
>    </dependencies>
> ```
