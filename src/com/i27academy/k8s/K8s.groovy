package com.i27academy.k8s

// all the methods 
class K8s {
    def jenkins
    K8s(jenkins) {
        this.jenkins = jenkins
    }
    
    // Method to authenticate to kubernetes cluster
    def auth_login(){
        jenkins.sh """
        echo "********************* Entering into Kubernetes Authentication/Login Method *********************"
        gcloud compute instances list
        echo "********************* Get the K8S Node *********************"
        gcloud container clusters get-credentials cluster --zone us-central1-f --project fine-climber-412705
        kubectl get nodes
        """
    }

    // Method to deploy the application
    def k8sdeploy(fileName, docker_image, namespace) {
        jenkins.sh """
        echo "********************* Entering into Kubernetes Deployment Method *********************"
        echo "Listing the files in the workspace"
        sed -i "s|DIT|${docker_image}|g" ./.cicd/${fileName}
        kubectl apply -f ./.cicd/${fileName} -n ${namespace}
        """
    }

   // Helm Deployments 
    def k8sHelmChartDeploy(appName, env, helmChartPath, imageTag){
        jenkins.sh """
        echo "********************* Entering into Helm Deployment Method *********************"
        helm version
        helm install ${appName}-${env}-chart -f ./.cicd/helm_values/values_${env}.yaml --set image.key=${imageTag} ${helmChartPath} 
        """
    }

    // git clone 
    def gitClone() {
        jenkins.sh """
        echo "********************* Entering into Git Clone Method *********************"
        git clone -b master https://github.com/i27academy/i27-shared-lib.git
        echo "********************* Listing the files in the workspace *********************"
        ls -la
        """
    }

}

