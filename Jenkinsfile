pipeline {
	agent any
	stages {
		stage('Test') {
			steps {
				sh 'hw06/gradlew check'
			}
		}
	}
	post {
		always {
			junit 'hw06/build/reports/**/*.xml'
		}
	}
}
