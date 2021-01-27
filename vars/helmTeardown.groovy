
def call(List namespaces = ['default'], List excludes = ['docker-registry']) {

    println "Tearing down charts in namespaces: ${namespaces.join(', ')}."

    def exc = excludes.join("|")
    for(int i = 0;i<namespaces.size();i++) {
        def n = namespaces[i]
        sh """
          for hchart in \$(helm list -n ${n} -q | grep -E -v '${exc}');
          do
              echo "Purging chart: \${hchart}"
              helm delete -n ${n} "\${hchart}"
          done
        """
    }

    println "Waiting for pods to be removed from namespaces: ${namespaces.join(', ')}."
    sh """
      PODS=\$(kubectl get pods --all-namespaces --no-headers  | grep -v -E "kube|${exc}" | wc -l)
      while [[ \$PODS != 0 ]]; do
        sleep 5
        PODS=\$(kubectl get pods --all-namespaces --no-headers  | grep -v -E "kube|${exc}" | wc -l)
      done
    """
}
