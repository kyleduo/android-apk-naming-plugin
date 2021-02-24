package com.kyleduo.androidapknaming.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApkVariantOutput
import com.android.build.gradle.api.ApplicationVariant
import com.kyleduo.androidapknaming.plugin.tools.DateTool
import com.kyleduo.androidapknaming.plugin.tools.GitInfo
import com.kyleduo.androidapknaming.plugin.tools.GitInfoLoader
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.gradle.api.Plugin
import org.gradle.api.Project

@SuppressWarnings('unused')
class AndroidApkNamingPlugin implements Plugin<Project> {

    Config config
    String template
    GitInfoLoader gitInfoLoader = new GitInfoLoader()
    private Project project
    private boolean hasCheckTemplate = false
    private GitInfo gitInfo

    void apply(Project project) {
        this.project = project

        // init apkNaming extension
        config = project.extensions.create("apkNaming", Config.class)

        // init velocity
        Velocity.init()

        // prepare git info
        prepareGitInfo()

        // config android gradle plugin, inject naming logic
        def android = project.extensions.findByType(AppExtension)
        android.applicationVariants.all { ApplicationVariant appVariant ->
            if (!tryInitTemplate()) {
                return
            }
            handleAppVariant(appVariant)
        }
    }

    private void prepareGitInfo() {
        gitInfo = gitInfoLoader.loadGitInfo()
    }

    private boolean tryInitTemplate() {
        if (hasCheckTemplate) {
            return template != null
        }
        hasCheckTemplate = true
        if (config == null || config.template == null) {
            project.logger.lifecycle('`apkNaming.template` config not found, skip naming.')
            return false
        }
        template = config.template
        return true
    }

    private void handleAppVariant(ApplicationVariant appVariant) {
        def params = [
                "projectName"     : project.name,
                "versionCode"     : appVariant.versionCode,
                "versionName"     : appVariant.versionName,
                "applicationId"   : appVariant.applicationId,
                "buildType"       : appVariant.buildType.name,
                "timestamp"       : System.currentTimeMillis().toString(),
                "timestampSeconds": System.currentTimeSeconds().toString(),
                "properties"      : project.properties,
                "date"            : new DateTool()
        ]

        def flavorMap = new HashMap<String, String>()
        for (flavor in appVariant.productFlavors) {
            def dimension = flavor.dimension
            if (dimension != null) {
                flavorMap.put(dimension, flavor.name)
            } else {
                project.logger.error("dimension of flavor ${flavor.name} is null. ")
                break
            }
        }
        params.put("flavor", flavorMap)

        if (gitInfo != null) {
            params.put("git", gitInfo)
        }

        project.logger.debug("android-apk-naming params: $params")

        VelocityContext context = new VelocityContext()
        params.each { key, value ->
            context.put(key, value)
        }
        StringWriter nameWriter = new StringWriter()
        Velocity.evaluate(context, nameWriter, "renderName", template)

        def newName = nameWriter.toString()
        if (!newName.endsWith('.apk')) {
            newName += '.apk'
        }

        project.logger.info("apk name changed: $newName")

        appVariant.outputs.each {
            if (it instanceof ApkVariantOutput) {
                it.outputFileName = newName
            }
        }
    }
}