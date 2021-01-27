
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
}
