def call(Map config = [:]) {
    sh "echo Hello ${config.name}. Today is ${config.dayOfWeek}."
    // Define the file to check
    def file = 'lerna.json'
    
    // Check if the file exists
    if (fileExists(file)) {
        echo "File ${file} exists."
        
        // Get the list of changed packages in the workspaces directory
        def changedPackages = getChangedPackages()
        
        if (changedPackages) {
            echo "Changed packages:"
            changedPackages.each { pkg ->
                echo "${pkg}"
            }
            
            // Build each changed package with Lerna
            for (pkg in changedPackages) {
                echo "Building package: ${pkg}"
                // Cache act weir
                // The local cache artifact in "/var/jenkins_home/workspace/test/node_modules/.cache/nx/8408733849954308460" was not been generated on this machine.
                // As a result, the cache's content integrity cannot be confirmed, which may make cache restoration potentially unsafe.
                // If your machine ID has changed since the artifact was cached, run "nx reset" to fix this issue.
                // Read about the error and how to address it here: https://nx.dev/recipes/troubleshooting/unknown-local-cache
                sh "rm -rf node_modules/.cache/nx"
                sh "npx lerna run build --scope=${pkg}"
            }
        } else {
            echo "No changes detected in the workspaces directory."
        }
    } else {
        echo "File ${file} does not exist."
    }
}

// Method to get the list of changed packages
def getChangedPackages() {
    def command = 'git diff --name-only HEAD HEAD~1 | grep "^packages/" | awk -F/ \'{print $2}\' | sort | uniq'
    def changedPackagesOutput = sh(script: command, returnStdout: true).trim()
    def changedPackagesList = changedPackagesOutput ? changedPackagesOutput.split('\n') : []
    return changedPackagesList
}