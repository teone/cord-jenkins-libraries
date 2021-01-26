
def call(Map config) {
    // note that I can't define this outside the function as there's no global scope in Groovy
    def defaultConfig = [
      onosReplica: 1,
      atomixReplica: 1,
      kafkaReplica: 1,
      etcdReplica: 1,
      bbsimReplica: 1,
      infraNamespace: "infra",
      volthaNamespace: "voltha",
      stackName: "voltha"
      workflow: "att",
      extraHelmFlags: "",
    ]

    if (!config) {
        config = [:]
    }

    def cfg = defaultConfig + config

    println "Deploying VOLTHA Stack with the following parameters: ${cfg}."

    sh """
    helm upgrade --install -n ${cfg.volthaNamespace} voltha1 onf/voltha-stack ${extraHelmFlags} \
          --set global.stack_name=${cfg.stackName} \
          --set global.voltha_infra_name=voltha-infra \
          --set global.voltha_infra_namespace=${cfg.infraNamespace} \
    """

    for(int i = 0;i<cfg.bbsimReplica;i++) {
      // TODO differentiate olt_id between different stacks
       sh """
         helm upgrade --install -n ${cfg.volthaNamespace} bbsim${i} onf/bbsim ${extraHelmFlags} \
         --set olt_id="1${i}" \
         -f $WORKSPACE/voltha-helm-charts/examples/${cfg.workflow}-values.yaml
       """
    }
}
