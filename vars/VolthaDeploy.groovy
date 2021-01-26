#!/usr/bin/env groovy

defaultConfig = [
  onosReplica: 1,
  atomixReplica: 1,
  kafkaReplica: 1,
  etcdReplica: 1,
  infraNamespace: "infra",
  volthaNamespace: "voltha",
  workflow: "att",
]

def call(Map config) {
    if (!config) {
        config = [:]
    }

    def cfg = defaultConfig + config

    println "Config: ${cfg}."
}
