# Android Apk Naming Plugin

![version](https://img.shields.io/badge/version-1.0.0-blue?style=flat)

Android Apk Naming Plugin 是一款小巧的Gradle插件，提供了对 .apk 文件进行方面的重命名的功能。

## 集成
将下面这段代码加入到项目根目录下的 build.gradle 文件。

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath "com.kyleduo.android-apk-naming:plugin:$latest_version"
    }
}
```

在Android Application module 中，增加插件声明

```groovy
plugins {
	id 'com.kyleduo.android-apk-naming'
}
```

## 使用

### 快速开始

将下面配置块添加到Application module 的 build.gradle 文件中。使用 template 字段来配置命名的结构。

```groovy
apkNaming {
    // example generated apk name: apk_naming_demo-1.0.0(1)-free-google-debug-2021-02-24-main.apk
    template 'apk_naming_demo-$versionName($versionCode)-$flavor["pay"]-$flavor["vendor"]-$buildType-$date-${git.branch}.apk'
}
```

### 模板变量 & 工具

插件支持在模板中使用一系列的变量和工具：


* **projectName**: 项目名称，同 `project.name`
* **versionCode**: 版本号
* **versionName**: 版本名称
* **buildType**: 构建类型，Android项目默认包含 build 和 release 两个构建类型
* **flavor**: 获取这次构建的 flavor 名称。这个变量是 dimension 到 name 的映射，所以需要指定 dimension 来获取 flavor 名称。比如 paid 这个 dimension 的 flavor 名称是 free，vendor 对应的是 google，那么 `$flavor["paid"]` 将得到 free， `$flavor["vendor"]` 将得到 google。
* **git**: Git 相关的信息
	* **git.user**: git 用户名
	* **git.commitId**: 当前的commit id
	* **git.commitIdShort**: 当前的commit id 的简化版
	* **git.branch**: 当前分支名
* **date**: 日期工具，基于构建的时间
	* **date.format(String)**: 例如： `$date.format("yyyy-MM-dd")`。
	* **date**: 直接使用 `$date` 会使用默认的格式化模板，同： `$date.format("yyyy-MM-dd")`.
	* **date.timestamp** / **date.timestamp()**: 毫秒时间戳
	* **date.timestampInSeconds** / **date.timestampInSeconds(**): 秒时间戳
* **properties**: 获取 Gradle project 的全部属性。非常方便从命令行注入参数并体现在命名中。 例如使用类似的命令构建 `./gradlew :app:assembleDebug -Penv=debug` 你可以通过下面的方式获取 env 属性的值 `$properties["env"]`.

## 更多

### 模板处理

AANP使用 [Velocity](http://velocity.apache.org/) 这个库进行模板渲染，所以模板中支持使用 Velocity 的语法。例如，如果某个字段的值可能为空，默认情况下将直接将模板显示出来，可以使用 Velocity 的 [Quite Reference Notation](http://velocity.apache.org/engine/devel/user-guide.html#quiet-reference-notation) 语法避免该情况：

```groovy
// if foo not exists, the result would be '$properties["foo"]'
template '$properties["foo"]'
// if foo not exists, the result would be ''
template '$!properties["foo"]'
```

## 协议
[MIT](https://opensource.org/licenses/MIT)





