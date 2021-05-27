def k8name = "k8-tools"

def k8template = """
metadata:
  labels:
    agent: ${ k8name }
  name: ${ k8name }
spec:
  containers: 
  - image: janash515/k8-tools
    name: terraform 
  - image: janash515/k8-tools
    name: kubectl
  - image: janash515/k8-tools
    name: ansible 
  - image: janash515/k8-tools
    name: helm 
    
"""

podTemplate(name: k8name, label: k8name, yaml: k8template, showRawYaml: false) {
   node(k8name) {
      stage("Test") {
        container("k8-tools"){
           sh "terraform version"
        }
     }
   }
}