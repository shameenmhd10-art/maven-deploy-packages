pipeline {
    agent any

    environment {
        GITHUB_CREDS = credentials('package')
        JAVA_HOME    = tool 'java'
        MAVEN_HOME   = tool 'maven'
        PATH         = "${JAVA_HOME}\\bin;${env.PATH}"
    }

    stages {

        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Verify Tools') {
            steps {
                bat '''
                java -version
                "%MAVEN_HOME%\\bin\\mvn.cmd" -version
                '''
            }
        }

        stage('Build & Deploy') {
            steps {
                configFileProvider([configFile(fileId: 'maven-github-settings', variable: 'MAVEN_SETTINGS')]) {
                    withEnv([
                        "GH_USER=${GITHUB_CREDS_USR}",
                        "GH_TOKEN=${GITHUB_CREDS_PSW}"
                    ]) {
                        bat '''
                        "%MAVEN_HOME%\\bin\\mvn.cmd" -s "%MAVEN_SETTINGS%" -B clean package
                        "%MAVEN_HOME%\\bin\\mvn.cmd" -s "%MAVEN_SETTINGS%" -B deploy
                        '''
                    }
                }
            }
        }
    }

    post {
        success {
            echo "✅ Build and deployment to GitHub Packages completed successfully."
        }

        failure {
            echo "❌ Pipeline failed. Check the console output for details."
        }
    }
}
