pipeline {
    agent any
    tools {
        maven 'maven-3'
    }
    environment {
        SONAR_TOKEN = credentials('sonar-token')
    }
    stages {
        stage('Compile') {
            steps {
                sh 'mvn clean compile'
            }
        }
        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
        }
        stage('SonarQube Scan') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh '''
                    mvn sonar:sonar \
                    -Dsonar.projectKey=end-to-end-devops-project \
                    -Dsonar.login=$SONAR_TOKEN
                    '''
                }
            }
        }
        stage('Quality Gate') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        stage('Package Artifact') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Upload Artifact to Nexus') {
            steps {
                sh 'mvn deploy'
            }
        }
        stage('Docker Build') {
            steps {
                sh "docker build -t yogithak/devops-project-app:${BUILD_NUMBER} ."
            }
        }
        stage('Trivy Security Scan') {
            steps {
                sh "trivy image yogithak/devops-project-app:${BUILD_NUMBER}"
            }
        }
        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-cred',
                    usernameVariable: 'USER',
                    passwordVariable: 'PASS'
                )]) {

                    sh '''
                    docker login -u $USER -p $PASS
                    docker push yogithak/devops-project-app:${BUILD_NUMBER}
                    '''

                }
            }
        }
        stage('Update Kubernetes Manifest') {

            steps {

                sh '''
                git clone https://github.com/yogithak25/devops-project-k8s-manifests.git
                cd devops-project-k8s-manifests

                sed -i "s|image:.*|image: yogithak/devops-project-app:${BUILD_NUMBER}|g" deployment.yaml

                git config user.email "luckychowdary333@gmail.com"
                git config user.name "jenkins"

                git commit -am "Update image version ${BUILD_NUMBER}"

                git push
                '''

            }

        }
    }
}
