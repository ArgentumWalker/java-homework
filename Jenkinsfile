pipeline {
	agent any
	stages {
		stage('Test') {
			steps {
				sh 'gradle clean'
				sh 'gradle build -p hw06'
			}
		}
	}
	post {
		always {
			sh 'ls -r hw06/build/classes'
			junit 'build/test-results/test/*.html'
		}
	}
}
