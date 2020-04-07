def call(credentialsId, gitCommand) {
    def randomUUID = UUID.randomUUID().toString()
    def tmpAskPassScript = pwd(tmp:true) + "/${randomUUID}"
    sh """
        echo '#!/bin/sh
case "\$1" in
Username*) echo \$USERNAME ;;
Password*) echo \$PASSWORD ;;
esac' > ${tmpAskPassScript}
    """
    withCredentials([usernamePassword(credentialsId: credentialsId, passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {            
        sh """
        chmod +x ${tmpAskPassScript}
        GIT_ASKPASS=${tmpAskPassScript} ${gitCommand}
        """
    }
}