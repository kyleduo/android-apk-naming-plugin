package com.kyleduo.androidapknaming.plugin

class GitInfo {
    String username
    String commitId
    String commitIdShort
    String branch

    GitInfo(String username, String commitId, String commitIdShort, String branch) {
        this.username = username
        this.commitId = commitId
        this.commitIdShort = commitIdShort
        this.branch = branch
    }

    GitInfo() {
        this('', '', '', '')
    }
}