# Android Apk Naming Plugin

![version](https://img.shields.io/badge/version-1.0.0-blue?style=flat)

[中文README](./README_CN.md)

Android Apk Naming Plugin is a Gradle plugin providing convenient way to rename Android .apk file.

## Download 
Add these lines to your root project’s build.gradle file.

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

Apply the plugin in build.gradle of Android application module.

```groovy
plugins {
	id 'com.kyleduo.android-apk-naming'
}
```

## Usage

### Quick start

Add this configuration block to your Android application module’s build.gradle file. You can define the naming pattern by modifying the template field.

```groovy
apkNaming {
    // example generated apk name: apk_naming_demo-1.0.0(1)-free-google-debug-2021-02-24-main.apk
    template 'apk_naming_demo-$versionName($versionCode)-$flavor["pay"]-$flavor["vendor"]-$buildType-$date-${git.branch}.apk'
}
```

### Template variables & tools

This plugin support several variables that you can use in the template.

* **projectName**: Name of the project. Equals to `project.name`
* **versionCode**: Version code of this build.
* **versionName**: Version name of this build.
* **buildType**: BuildType of this build. There are two build types: debug and release by default.
* **flavor**: Flavor name of this build. This variable is a map from flavor dimension to flavor name. For example a build can has two flavor type for each dimensions, let’s say free for paid and google for vendor, and you will get ‘free’ from  `$flavor["paid"]` and ‘google’ from `$flavor["paid"]`.
* **git**: Git related information
	* **git.user**: Username from git config.
	* **git.commitId**: current commit id.
	* **git.commitIdShort**: Current commit id, is short format.
	* **git.branch**: Current git branch name.
* **date**: Tool for retrieve formatted date and time when the build start. 
	* **date.format(String)**: For example: `$date.format("yyyy-MM-dd")`.
	* **date**: Using just `$date` will got the same result as `$date.format("yyyy-MM-dd")`.
	* **date.timestamp** / **date.timestamp()**: Timestamp in milliseconds.
	* **date.timestampInSeconds** / **date.timestampInSeconds(**): Timestamp in seconds.
* **properties**: Access all properties of Gradle project instance. This is useful when you want to inject some params through gradle command line. For example, if you execute the task like this `./gradlew :app:assembleDebug -Penv=debug` and you can get the ‘env’ param using `$properties["env"]`.

## More

### Template processing

AANP uses [Velocity](http://velocity.apache.org/) to render the template. So the template supports Velocity syntax. For example, if a token in the template could be null, you can use the [Quite Reference Notation](http://velocity.apache.org/engine/devel/user-guide.html#quiet-reference-notation) to prevent rendering the raw template token when the reference is null.

```groovy
// if foo not exists, the result would be '$properties["foo"]'
template '$properties["foo"]'
// if foo not exists, the result would be ''
template '$!properties["foo"]'
```

## Licenses
[MIT](https://opensource.org/licenses/MIT)

