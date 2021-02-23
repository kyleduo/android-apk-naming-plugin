package com.kyleduo.androidapknaming.plugin

class GitInfo {
    String username
    String commitId
    String commitIdShort

    GitInfo(String username, String commitId, String commitIdShort) {
        this.username = username
        this.commitId = commitId
        this.commitIdShort = commitIdShort
    }

    GitInfo() {
        this('', '', '')
    }
}