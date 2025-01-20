import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.projectFeatures.activeStorage
import jetbrains.buildServer.configs.kotlin.projectFeatures.kubernetesConnection
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2024.12"

project {

    buildType(Build)

    params {
        text("count", "0")
        param("build.triggered.by.schedule", "false")
    }

    features {
        kubernetesConnection {
            id = "PROJECT_EXT_52"
            name = "Kubernetes Connection"
            apiServerUrl = "https://F07A2B3A3BC437F2D7D1AD70F45CF9D0.gr7.us-east-1.eks.amazonaws.com"
            caCertificate = "credentialsJSON:1f29aee9-4778-4db1-8dc0-88d6d9bc917a"
            authStrategy = eks {
                accessId = "AKIA5JH2VERVGSHKPAXS"
                secretKey = "credentialsJSON:ba478e6d-85d2-463e-bac5-e8a1475e6c74"
                clusterName = "tom-teamcity-test"
            }
            param("secure:password", "credentialsJSON:81fa8229-3adf-44c0-8971-764198e2e720")
            param("username", "sunguiguan@163.com")
        }
        activeStorage {
            id = "PROJECT_EXT_57"
            activeStorageID = "DefaultStorage"
        }
    }
}

object Build : BuildType({
    name = "Build"

    type = BuildTypeSettings.Type.COMPOSITE

    vcs {
        root(DslContext.settingsRoot)

        showDependenciesChanges = true
    }

    steps {
        script {
            name = "Test"
            id = "Test"
            scriptContent = """
                # kubectl get po -A
                
                echo test
            """.trimIndent()
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
    }
})
