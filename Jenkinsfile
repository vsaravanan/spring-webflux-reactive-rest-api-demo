
node {

    def javaHome = env.JAVA_HOME
    def jenkinsConfig = env.jenkins_config_home
    def jenkinsRoot = "${JENKINS_HOME}/workspace"
    def appVer = ""
    def lastCommitMessage = ""

    environment {
        colordust = credentials('colordust')
    }

        stage('Clean') {

                sh   " pwd "
                sh   "rm -rf * "

        }

        stage('Environment')
        {


            echo "JAVA_HOME: ${JAVA_HOME}"
            jenkinsConfig="${jenkinsConfig}/${JOB_NAME}"
            echo "JENKINS_CONFIG: ${jenkinsConfig}"

            echo "JOB_NAME : ${JOB_NAME}"                // Tuton
            echo "BUILD_URL : ${BUILD_URL}"              // https://web.saravanjs.com/job/Tuton/3/
            echo "JENKINS_HOME : ${JENKINS_HOME}"        // /var/lib/viswar
            echo "WORKSPACE : ${WORKSPACE}"              // /var/lib/viswar/workspace/Tuton

        }

        stage('Checkout')
        {

            checkout scm

            // git branch: 'master', credentialsId: 'ecdsa', url: 'viswar@sjsapp:/gitrepo/demoskills.git'
            gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()

			echo "gitCommit : ${gitCommit}"

			gitVersion = sh(returnStdout: true, script: 'git rev-list HEAD --count').toString().trim()
			echo "gitVersion: ${gitVersion}"

			lastCommitMessage = sh(returnStdout: true, script: 'git log -1 --format="%s" ').toString().trim()
			appVer = "${JOB_NAME}.${BUILD_ID}.git-${gitVersion}"
			lastCommitMessage = sh(returnStdout: true, script: 'git log -1 --format="%s" | cut -f 2- -d " "').toString().trim()
			echo "lastCommitMessage: ${lastCommitMessage} "

			echo "appVer: ${appVer}" // demoskills.60.git-47


        }


        stage('Build')
        {
            sh " mvn clean package install -T 1C "
        }

        stage('Package')
        {

            sh " cd ${jenkinsRoot}; pwd;   tar -czf ${WORKSPACE}.tar.gz ${JOB_NAME} "

        }

        stage('Deploy')
        {
            sshagent(['ecdsa']) {

                sh ' scp ${WORKSPACE}.tar.gz  viswar@sjsapp:/data/tmp '

            }
        }

        stage('Install')
        {
            withCredentials([string(credentialsId: 'colordust', variable: 'colordust')]) {
                sshagent(['ecdsa']) {
                    sh  ' ssh viswar@sjsapp bash /data/scripts/archive.sh ${JOB_NAME} '
                    sh  ' ssh viswar@sjsapp bash /data/scripts/install.sh ${JOB_NAME} ${colordust} '
                }
            }
        }

        stage('Email')
        {
            body = " job name : ${JOB_NAME} \n Version : ${appVer} \n Jenkins : ${BUILD_URL} \n  Commit Message : ${lastCommitMessage} "
            emailext body: body, subject: "${JOB_NAME} was deployed", to: 'saravanan.resume@gmail.com', from: 'jenkins'


        }


}

