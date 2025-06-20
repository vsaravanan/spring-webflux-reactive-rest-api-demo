#!/usr/bin/env groovy
node {
  def javaHome = env.JAVA_HOME
  def jenkinsRoot = "${JENKINS_HOME}/workspace"
  def appVer = ''
  def lastCommitMessage = ''

  environment {
    colordust = credentials('colordust')
  }

  try {
    stage('Clean') {
      sh "pwd"
      sh "rm -rf *"
    }

    stage('Environment') {
      echo "JAVA_HOME: ${JAVA_HOME}"

      echo "JOB_NAME : ${JOB_NAME}" // Tuton
      echo "BUILD_URL : ${BUILD_URL}" // https://web.saravanjs.com/job/Tuton/3/
      echo "JENKINS_HOME : ${JENKINS_HOME}" // /var/lib/viswar
      echo "WORKSPACE : ${WORKSPACE}" // /var/lib/viswar/workspace/Tuton
    }

    stage('Checkout') {
      checkout scm

      gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
      echo "gitCommit : ${gitCommit}"

      gitVersion = sh(returnStdout: true, script: 'git rev-list HEAD --count').toString().trim()
      echo "gitVersion: ${gitVersion}"

      lastCommitMessage = sh(returnStdout: true, script: 'git log -1 --format="%s" ').toString().trim()
      appVer = "${JOB_NAME}.${BUILD_ID}.git-${gitVersion}"
      echo "lastCommitMessage: ${lastCommitMessage} "
      echo "appVer: ${appVer}" // demoskills.60.git-47
    }

    stage('Build') {
      sh "mvn clean package install -Dmaven.test.skip=true -T 1C"
    }

//    stage('SonarQube') {
//      catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
//        withSonarQubeEnv() {
//          sh "mvn clean verify sonar:sonar -Dsonar.projectKey=saravanjs-project -Dsonar.projectName='saravanjs-project'"
//        }
//      }
//    }

    stage('SonarQube') {
      try
      {
        withSonarQubeEnv() {
          def sonarServerReachable = sh(script: 'curl -s -o /dev/null -w "%{http_code}" http://sjsdb:9000', returnStatus: true) == 200
          if (sonarServerReachable) {
            echo "SonarQube server is running."
            sh "mvn clean verify sonar:sonar -Dsonar.projectKey=saravanjs-project -Dsonar.projectName='saravanjs-project'"
          } else {
            echo 'SonarQube server is down. Continuing without analysis.'
          }
        }
      } catch (Exception e) {
        echo "SonarQube analysis failed: ${e.message}. Continuing the job."
      }
    }

    stage('Test') {
      sh "mvn test -Dmaven.test.failure.ignore=true "
    }

    stage('Package') {
      sh "cd ${jenkinsRoot}; pwd; tar -czf ${WORKSPACE}.tar.gz ${JOB_NAME}"
    }

    stage('Deploy') {
      sshagent(['ecdsa']) {
        sh 'scp ${WORKSPACE}.tar.gz viswar@sjsapp:/data/tmp'
      }
    }

    stage('Archive') {
      sshagent(['ecdsa']) {
        sh "ssh viswar@sjsapp bash /data/scripts/archive.sh ${JOB_NAME} ${appVer} --error"
      }
    }

    stage('Install') {
      withCredentials([string(credentialsId: 'colordust', variable: 'colordust')]) {
        sshagent(['ecdsa']) {
          sh 'ssh viswar@sjsapp bash /data/scripts/install.sh ${JOB_NAME} ${colordust} --error'
        }
      }
    }

    stage('Email') {
      echo 'MVS job success'
      body = "jenkins job SUCCESS \n job name : ${JOB_NAME} \n Version : ${appVer} \n Jenkins : " +
             "${BUILD_URL} \n  Commit Message : ${lastCommitMessage} "
      emailext body: body,
                subject: "jenkins job ${appVer} was deployed SUCCESS",
                to: 'saravanan.resume@gmail.com',
                from: 'jenkins'
    }
  } catch (Exception error) {
    echo 'MVS failed'
    body = "jenkins job FAILED \n job name : ${JOB_NAME} \n Version : ${appVer} \n Jenkins : " +
           "${BUILD_URL} \n  Commit Message : ${lastCommitMessage} "
    emailext body: body,
              subject: "jenkins job ${appVer} was deployed but FAILED" ,
              to: 'saravanan.resume@gmail.com',
              from: 'jenkins'
  }
  finally {
    junit(testResults: 'target/surefire-reports/*.xml', allowEmptyResults : true)
    archiveArtifacts 'target/*.jar'
  }


}
