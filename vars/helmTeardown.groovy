
def call(List namespaces, List excludes = ['docker-registry']) {

    println "Tearing down charts in namespaces: ${namespaces.join(', ')}."

    namespaces.each { n ->
        charts = sh """
          helm list -n ${n} -q | grep -E -v '${excludes.join('|')}'
        """
        charts.each { c ->
          println "Deleting chart ${c} in namespace ${n}"
          sh """
            helm delete -n ${n} ${c}
          """
        }
    }
}
