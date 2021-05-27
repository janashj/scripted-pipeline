def k8name = "k8-terraform"

def k8template = """
metadata:
  labels:
    agent: ${ k8name }
  name: ${ k8name }
spec:
  containers: 
  - image: janash515/k8tools
    name: k8-terraform """

podTemplate(name: k8name, label: k8name, yaml: k8template, showRawYaml: false) {
  withCredentials([usernamePassword(credentialsId: 'jenkins-aws-access-key', passwordVariable: 'AWS_SECRET_ACCESS_KEY', usernameVariable: 'AWS_ACCESS_KEY_ID')]) {
        withEnv(["AWS_REGION=${aws_region_var}"]) {
            stage("Terrraform Init"){
                sh """
                    bash setenv.sh ${params.environment}.tfvars
                    terraform init
                    terraform plan -var-file dev.tfvars
                """
            }        
            
            if(params.terraform_apply){
                stage("Terraform Apply"){
                    sh """
                        terraform apply -var-file ${params.environment}.tfvars -auto-approve
                    """
                }
            }
            else if(params.terraform_destroy){
                stage("Terraform Destroy"){
                    sh """
                        terraform destroy -var-file ${params.environment}.tfvars -auto-approve
                    """
                }
            }
            else {
                stage("Terraform Plan"){
                    sh """
                        terraform plan -var-file ${environment}.tfvars
                    """
                }
            }
        }        
    }    
}
}