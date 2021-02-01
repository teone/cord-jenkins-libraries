def call(Map config) {

  def defaultConfig = [
    branc: "master"
    gerritProject: "",
    gerritRefspec: "",
    volthaSystemTestsChange: "",
    volthaHelmChartsChange: "",
  ]

  if (!config) {
      config = [:]
  }

  def cfg = defaultConfig + config

  stage('Download Patch') {
      when {
        expression {
          // We are always downloading those repos, if the patch under test is in one of those
          // just checkout the patch, no need to clone it again
          return "${gerritProject}" != 'voltha-system-tests' && "${gerritProject}" != 'voltha-helm-charts';
        }
      }
      steps {
        checkout([
          $class: 'GitSCM',
          userRemoteConfigs: [[
            url: "https://gerrit.opencord.org/${gerritProject}",
            refspec: "${gerritRefspec}"
          ]],
          branches: [[ name: "${branch}", ]],
          extensions: [
            [$class: 'WipeWorkspace'],
            [$class: 'RelativeTargetDirectory', relativeTargetDir: "${gerritProject}"],
            [$class: 'CloneOption', depth: 0, noTags: false, reference: '', shallow: false],
          ],
        ])
        sh """
          pushd $WORKSPACE/${gerritProject}
          git fetch https://gerrit.opencord.org/${gerritProject} ${gerritRefspec} && git checkout FETCH_HEAD

          echo "Currently on commit: \n"
          git log -1 --oneline
          popd
        """
      }
    }
    stage('Clone voltha-system-tests') {
      steps {
        checkout([
          $class: 'GitSCM',
          userRemoteConfigs: [[
            url: "https://gerrit.opencord.org/voltha-system-tests",
            refspec: "${volthaSystemTestsChange}"
          ]],
          branches: [[ name: "${branch}", ]],
          extensions: [
            [$class: 'WipeWorkspace'],
            [$class: 'RelativeTargetDirectory', relativeTargetDir: "voltha-system-tests"],
            [$class: 'CloneOption', depth: 0, noTags: false, reference: '', shallow: false],
          ],
        ])
        if (volthaSystemTestsChange != '') {
          sh """
          cd $WORKSPACE/voltha-system-tests
          git fetch https://gerrit.opencord.org/voltha-system-tests ${volthaSystemTestsChange} && git checkout FETCH_HEAD
          """
        }
      }
    }
    stage('Clone voltha-helm-charts') {
      steps {
        checkout([
          $class: 'GitSCM',
          userRemoteConfigs: [[
            url: "https://gerrit.opencord.org/voltha-helm-charts",
            refspec: "${volthaHelmChartsChange}"
          ]],
          branches: [[ name: "master", ]],
          extensions: [
            [$class: 'WipeWorkspace'],
            [$class: 'RelativeTargetDirectory', relativeTargetDir: "voltha-helm-charts"],
            [$class: 'CloneOption', depth: 0, noTags: false, reference: '', shallow: false],
          ],
        ])
        script {
          if (volthaHelmChartsChange != '') {
            sh """
            cd $WORKSPACE/voltha-helm-charts
            git fetch https://gerrit.opencord.org/voltha-helm-charts ${volthaHelmChartsChange} && git checkout FETCH_HEAD
            """
          }
        }
      }
    }
}
