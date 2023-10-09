pipeline {
    agent any

    tools {
        maven "3.8.1"
    }

    stages {
        stage('Build') {
            steps {
                git url: 'https://github.com/VadzimBarysenka/RESTAssured.git',
                branch: 'main',
                credentialsId: 'jenkins'

                bat "mvn -Dtest=restfulbooker.** verify"
            }
        }
    }
    post {
      always {
             allure([
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'allure-results']]
       ])
    }
  }
}