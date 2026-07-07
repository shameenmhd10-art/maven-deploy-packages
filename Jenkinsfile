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

        stage('Build') {
            steps {
                bat '''
                "%MAVEN_HOME%\\bin\\mvn.cmd" clean compile
                '''
            }
        }

        stage('Unit Test') {
            steps {
                bat '''
                "%MAVEN_HOME%\\bin\\mvn.cmd" test
                '''
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                bat '''
                "%MAVEN_HOME%\\bin\\mvn.cmd" package
                '''
            }
        }

        stage('Deploy to GitHub Packages') {
            steps {
                configFileProvider([configFile(fileId: 'maven-github-settings', variable: 'MAVEN_SETTINGS')]) {
                    withEnv([
                        "GH_USER=${GITHUB_CREDS_USR}",
                        "GH_TOKEN=${GITHUB_CREDS_PSW}"
                    ]) {
                        bat '''
                        "%MAVEN_HOME%\\bin\\mvn.cmd" -s "%MAVEN_SETTINGS%" deploy
                        '''
                    }
                }
            }
        }
    }

    post {
        success {
            echo "✅ Build, Unit Test, Package and Deployment completed successfully."
        }
        failure {
            echo "❌ Pipeline failed."
        }
    }
}
