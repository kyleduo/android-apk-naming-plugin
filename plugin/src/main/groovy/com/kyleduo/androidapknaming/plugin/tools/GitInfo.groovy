package com.kyleduo.androidapknaming.plugin.tools

class GitInfo {
    String user
    String commitId
    String commitIdShort
    String branch

    GitInfo(String user, String commitId, String commitIdShort, String branch) {
        this.user = user
        this.commitId = commitId
        this.commitIdShort = commitIdShort
        this.branch = branch
    }

    GitInfo() {
        this('', '', '', '')
    }
}