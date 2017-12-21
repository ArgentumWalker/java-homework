pipeline {
	agent any
	stages {
		stage('Test') {
			steps {
				cmd 'call hw06/gradlew check'
			}
		}
	}
	post {
		always {
			junit 'hw06/build/reports/**/*.xml'
		}
	}
}
