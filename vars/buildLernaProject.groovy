def call(Map config = [:]) {
    sh "echo ${config.dayofWeek}"
    // Define the file to check
    def file = 'lerna.json'
    
    // Check if the file exists
    if (fileExists(file)) {
        echo "File ${file} exists."
        
        // Get the list of changed packages in the workspaces directory
        def changedPackages = getChangedPackages()
        
        if (changedPackages) {
            echo "Changed packages:"
            echo "${changedPackages}"
            
            // Build each changed package with Lerna
            changedPackages.split('\n').each { package ->
                echo "Building package: ${package}"
                sh "npx lerna run build --scope=${package}"
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
    def changedPackages = sh(script: command, returnStdout: true).trim()
    return changedPackages
}