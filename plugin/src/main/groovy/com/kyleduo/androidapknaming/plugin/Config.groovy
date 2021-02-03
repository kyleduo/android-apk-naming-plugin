package com.kyleduo.androidapknaming.plugin

class Config {
    String template

    void setTemplate(String template) {
        this.template = template

        println("setter")
    }

    @Override
    String toString() {
        return super.toString()
    }
}
