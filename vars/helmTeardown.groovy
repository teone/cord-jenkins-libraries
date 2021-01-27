
def call(List namespaces = ['default'], List excludes = ['docker-registry']) {

    println "Tearing down charts in namespaces: ${namespaces.join(', ')}."

    for(int i = 0;i<namespaces.size();i++) {
      def n = namespaces[i]
      charts = sh returnStdout: true, script: """
      helm list -n ${n} -q | grep -E -v '${excludes.join('|')}'
      """
      println charts
      println charts.split()

      for(int j = 0;j<charts.size();j++) {
        def c = charts[j]
        println "Deleting chart ${c} in namespace ${n}"
        sh """
        helm delete -n ${n} ${c}
        """
      }
    }
}
