def call(Map config = [:]) {
    loadLinuxScript(name: 'build-lerna.sh')
    sh "./build-lerna.sh"
}
