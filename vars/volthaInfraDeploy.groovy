// usage
//
// stage('test stage') {
//   steps {
//     volthaDeploy([
//       onosReplica: 3
//     ])
//   }
// }


def call(Map config) {
    // NOTE use params or directule extraHelmFlags??
    def defaultConfig = [
      onosReplica: 1,
      atomixReplica: 1,
      kafkaReplica: 1,
      etcdReplica: 1,
      infraNamespace: "infra",
      workflow: "att",
      extraHelmFlags: "",
    ]

    if (!config) {
        config = [:]
    }

    def cfg = defaultConfig + config

    println "Deploying VOLTHA infra with the following parameters: ${cfg}."

    // TODO support multiple replicas
    sh """
    helm upgrade --install voltha-infra onf/voltha-infra ${cfg.extraHelmFlags} \
          --set global.log_level="DEBUG" \
          -f $WORKSPACE/voltha-helm-charts/examples/${cfg.workflow}-values.yaml
    """
}
