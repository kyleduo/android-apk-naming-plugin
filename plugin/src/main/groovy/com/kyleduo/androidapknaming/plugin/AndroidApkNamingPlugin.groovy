package com.kyleduo.androidapknaming.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidApkNamingPlugin implements Plugin<Project> {
    void apply(Project project) {
        def appExtension = project.extensions.findByType(AppExtension.class)
        project.afterEvaluate {
            appExtension.applicationVariants.each {
                handleAppVariant(it)
            }
        }
    }

    private void handleAppVariant(ApplicationVariant appVariant) {

    }
}