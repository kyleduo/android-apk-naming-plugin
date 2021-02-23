package com.kyleduo.androidapknaming.plugin

class GitInfoLoader {

    GitInfo loadGitInfo() {
        if (!gitAvailable()) {
            return new GitInfo()
        }

        def username = getUsername()
        def commitId = getCommitId()
        def commitIdShort = getCommitIdShort()

        return new GitInfo(username, commitId, commitIdShort)
    }

    private boolean gitAvailable() {
        return execute('git status').isSuccess()
    }

    private String getUsername() {
        return execute('git config user.name').out
    }

    private String getCommitId() {
        return execute('git rev-parse HEAD').out
    }

    private String getCommitIdShort() {
        return execute('git rev-parse --short HEAD').out
    }

    private Output execute(String command) {
        def sout = new StringBuilder(), serr = new StringBuilder()
        def proc = command.execute()
        proc.consumeProcessOutput(sout, serr)
        proc.waitForOrKill(1000)
        return new Output(sout.toString(), serr.toString())
    }


    class Output {
        String out
        String err

        Output(String out, String err) {
            this.out = out
            this.err = err
        }

        boolean isSuccess() {
            return this.err == '' && this.out != ''
        }
    }
}
