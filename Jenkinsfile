pipeline {
      agent {
          kubernetes {
              yaml '''
  apiVersion: v1
  kind: Pod
  spec:
    containers:
      - name: kaniko
        image: gcr.io/kaniko-project/executor:debug
        command:
          - sleep
        args:
          - "9999999"
        volumeMounts:
          - name: docker-config
            mountPath: /kaniko/.docker
      - name: git
        image: alpine/git:latest
        command:
          - sleep
        args:
          - "9999999"
    volumes:
      - name: docker-config
        projected:
          sources:
            - secret:
                name: docker-hub-secret
                items:
                  - key: .dockerconfigjson
                    path: config.json
  '''
          }
      }

      environment {
          DOCKER_IMAGE = 'yooncount/demo-backend'
          GITOPS_REPO = 'github.com/ddihn/cicd-demo.git'
      }

      stages {
          stage('Build & Push Image') {
              steps {
                  container('kaniko') {
                      sh """
                          /kaniko/executor \
                              --context=dir://\${WORKSPACE} \
                              --destination=\${DOCKER_IMAGE}:\${BUILD_NUMBER} \
                              --destination=\${DOCKER_IMAGE}:latest
                      """
                  }
              }
          }

          stage('Update GitOps Repo') {
              steps {
                  container('git') {
                      withCredentials([usernamePassword(
                          credentialsId: 'github-creds',
                          usernameVariable: 'GIT_USER',
                          passwordVariable: 'GIT_TOKEN'
                      )]) {
                          sh """
                              git clone https://\${GIT_USER}:\${GIT_TOKEN}@\${GITOPS_REPO} gitops
                              cd gitops

                              sed -i "s|image: \${DOCKER_IMAGE}:.*|image: \${DOCKER_IMAGE}:\${BUILD_NUMBER}|" deployment.yaml

                              git config user.email "jenkins@ci.com"
                              git config user.name "Jenkins"
                              git add .
                              git commit -m "Update image tag to \${BUILD_NUMBER}"
                              git push origin main
                          """
                      }
                  }
              }
          }
      }
  }

