// loads all the images tagged as citest on a Kind cluster

def call(Map cfg) {
  def defaultConfig = [
    name: "kind-ci"
  ]

  if (!config) {
      config = [:]
  }

  def cfg = defaultConfig + config

  def images = sh "docker images | grep citest"

  println images

  println "Loading image ${image} on Kind cluster ${cfg.name}"


  // for image in \$(docker images -f "reference=*/*/*citest" --format "{{.Repository}}"); do echo "Pushing \$image to nodes"; kind load docker-image \$image:citest --name ${clusterName} --nodes ${clusterName}-worker,${clusterName}-worker2; done
}
