pipeline {
	agent any
	stages {
		stage('Test') {
			steps {
				sh 'gradle clean -p hw04'
				sh 'gradle build -p hw04'
			}
		}
	}
}
