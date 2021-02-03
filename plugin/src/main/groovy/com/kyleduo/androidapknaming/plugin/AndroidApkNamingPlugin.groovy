package com.kyleduo.androidapknaming.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.gradle.api.Plugin
import org.gradle.api.Project

@SuppressWarnings('unused')
class AndroidApkNamingPlugin implements Plugin<Project> {

    Config config
    String template
    DateProcessor dateProcessor = new DateProcessor()

    void apply(Project project) {
        config = project.extensions.create("apkNaming", Config.class)

        def appExtension = project.extensions.findByType(AppExtension.class)
        project.afterEvaluate {

            if (config == null || config.template == null) {
                throw new IllegalStateException("please config apkNaming.template first")
            }

            template = dateProcessor.process(config.template)
            Velocity.init()

            appExtension.applicationVariants.each {
                handleAppVariant(project, it)
            }
        }
    }

    private void handleAppVariant(Project project, ApplicationVariant appVariant) {
        def params = [
                "projectName"  : project.name,
                "versionCode"  : appVariant.versionCode,
                "versionName"  : appVariant.versionName,
                "applicationId": appVariant.applicationId,
                "buildType"    : appVariant.buildType.name,
        ]

        for (flavor in appVariant.productFlavors) {
            def dimension = flavor.dimension
            dimension = dimension.substring(0, 1).toUpperCase() + dimension.substring(1)
            params.put("flavor" + dimension, flavor.name)
        }

        VelocityContext context = new VelocityContext()
        for (key in params.keySet()) {
            context.put(key, params.get(key))
        }
        StringWriter nameWriter = new StringWriter()
        Velocity.evaluate(context, nameWriter, "renderName", template)

        println(nameWriter.toString())
    }
}