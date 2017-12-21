pipeline {
	agent any
	stages {
		stage('Test') {
			steps {
				sh 'hw*/gradlew check'
			}
		}
	}
	post {
		always {
			junit 'hw*/build/reports/**/*.xml'
		}
	}
}
