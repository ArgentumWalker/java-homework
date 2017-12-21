pipeline {
	agent any
	stages {
		stage('Test') {
			steps {
				sh 'gradle build -p hw06'
			}
		}
	}
	post {
		always {
			sh 'ls -r hw06/build/classes/test'
			junit 'build/test-results/test/*.html'
		}
	}
}
