pipeline {
    agent any

    parameters {
        string(name: 'browser', defaultValue: 'chrome', description: 'Browser to run tests')
        string(name: 'env', defaultValue: 'qa', description: 'Test Environment')
        string(name: 'logLevel', defaultValue: 'INFO', description: 'Log Level')
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo 'Checking out code from GitHub...'
                git branch: 'main', url: 'https://github.com/satish-raja/SeleniumFrameworkTestng.git'
            }
        }

        stage('Start Selenium Grid') {
            steps {
                echo 'Starting Selenium Grid with 4 Chrome nodes...'
                bat './docker-compose-generator.sh chrome 4 start' // Use 'sh' instead of 'bat' for Linux
            }
        }

        stage('Run Selenium Tests') {
            steps {
                echo 'Executing Selenium Tests...'
                bat 'mvn clean test -DsuiteXmlFile=TestngRunner/testng-sanitytest.xml -Dbrowser=${browser} -Denv=${env} -DlogLevel=${logLevel}'
            }
        }

        stage('Stop Selenium Grid') {
            steps {
                echo 'Stopping Selenium Grid...'
                bat './docker-compose-generator.sh chrome 4 stop'
            }
        }

        stage('Cleanup Docker Images') {
            steps {
                echo 'Removing unused Docker images...'
                bat 'docker image prune -f'
                bat 'docker rmi selenium/node-chrome:4.28.1 selenium/hub:4.28.1'
            }
        }

        stage('Generate & Serve Allure Report') {
            steps {
                echo 'Generating Allure Report...'
                bat 'allure generate --clean'
                bat 'allure serve'
            }
        }
    }

    post {
        always {
            echo 'Pipeline execution completed.'
        }
        failure {
            echo 'Pipeline failed! Check logs for errors.'
        }
    }
}
