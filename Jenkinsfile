pipeline {
    agent any

    environment {
        IMAGE_NAME = "yogithak/java-devops-automation-project"
        IMAGE_TAG = "${BUILD_NUMBER}"
        SONAR_TOKEN = credentials('sonar-token')
    }

    stages {

        // -----------------------------
        // BUILD + TEST (MAVEN - DOCKER)
        // -----------------------------
        stage('Build & Test') {
            steps {
                sh '''
                docker run --rm \
                -u root \
                -v "$WORKSPACE":/app \
                -w /app \
                maven:3.9.9-eclipse-temurin-17 \
                mvn clean verify
                '''
            }
        }

        // -----------------------------
        // SONARQUBE SCAN
        // -----------------------------
        stage('SonarQube Scan') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh '''
                    docker run --rm \
                    -v /var/jenkins_home/workspace/java-devops-pipeline:/app \
                    -w /app \
                    maven:3.9.9-eclipse-temurin-17 \
                    mvn clean verify sonar:sonar \
                    -Dsonar.projectKey=java-devops-project \
                    -Dsonar.host.url=$SONAR_HOST_URL \
                    -Dsonar.login=$SONAR_TOKEN \
                    -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    '''
                }
            }
        }

        // -----------------------------
        // QUALITY GATE
        // -----------------------------
        stage('Quality Gate') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        // -----------------------------
        // PACKAGE + DEPLOY TO NEXUS
        // -----------------------------
        stage('Package & Deploy to Nexus') {
            steps {
                sh '''
                docker run --rm \
                -v /var/jenkins_home/workspace/java-devops-pipeline:/app \
                -v /var/jenkins_home/.m2:/root/.m2 \
                -w /app \
                maven:3.9.9-eclipse-temurin-17 \
                mvn clean deploy
                '''
            }
        }

        // -----------------------------
        // DOCKER BUILD
        // -----------------------------
        stage('Docker Build') {
            steps {
                sh '''
                docker build -t $IMAGE_NAME:$IMAGE_TAG .
                '''
            }
        }

        // -----------------------------
        // TRIVY SECURITY SCAN
        // -----------------------------
        stage('Trivy Security Scan') {
            steps {
                sh '''
                docker run --rm \
                -v /var/run/docker.sock:/var/run/docker.sock \
                aquasec/trivy:0.50.0 image \
                $IMAGE_NAME:$IMAGE_TAG
                '''
            }
        }

        // -----------------------------
        // DOCKER PUSH
        // -----------------------------
        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-cred',
                    usernameVariable: 'USER',
                    passwordVariable: 'PASS'
                )]) {

                    sh '''
                    echo $PASS | docker login -u $USER --password-stdin
                    docker push $IMAGE_NAME:$IMAGE_TAG
                    '''
                }
            }
        }

        // -----------------------------
        // UPDATE K8s MANIFEST (GITOPS)
        // -----------------------------
        stage('Update Kubernetes Manifest Repo') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'github-cred',
                    usernameVariable: 'GIT_USER',
                    passwordVariable: 'GIT_PASS'
                )]) {

                    sh '''
                    rm -rf devops-project-k8s-manifests

                    git clone https://$GIT_USER:$GIT_PASS@github.com/yogithak25/devops-project-k8s-manifests.git

                    cd devops-project-k8s-manifests

                    sed -i "s|image:.*|image: $IMAGE_NAME:$IMAGE_TAG|g" deployment.yaml

                    git config user.email "yogithak25@gmail.com"
                    git config user.name "yogithak25"

                    git add deployment.yaml

                    git commit -m "Update image $IMAGE_TAG" || echo "No changes"

                    git push
                    '''
                }
            }
        }
    }

    // -----------------------------
    // POST ACTIONS
    // -----------------------------
    post {
        success {
            echo "✅ Pipeline completed successfully"
        }
        failure {
            echo "❌ Pipeline failed"
        }
    }
}
