#!/bin/sh

SFTP_SERVER=appdeploy-sftp.library.ucla.edu
SFTP_USER=appdeploy
SFTP_PROJECT=${CI_WEBAPP_NAME}
SFTP_TARGET_DIR=${SFTP_PROJECT}
SFTP_FILESPEC=${SFTP_PROJECT}.war

JENKINS_HOST=jenkins-devsupport.library.ucla.edu

# Put in place private key for CI secret encrypted env variable.
# Set required permissions for private key.
mkdir -p -m 0700 ~/.ssh
echo "${CI_WS_SSH_PRIV_KEY}" >~/.ssh/id_rsa
chmod 0600 ~/.ssh/id_rsa

# Add sftp site to known_hosts to avoid permanent hang on first sftp connection
ssh-keyscan appdeploy-sftp.library.ucla.edu >>~/.ssh/known_hosts
chmod 0644 ~/.ssh/known_hosts

# Upload file(s) to sftp site in project-specific directory
# Use sftp's batch mode (-b, with - for stdin) to throw/ignore errors:
## commands starting with - will show error messages, but not cause sftp
## to exit if an error occurs on those commands.  See man sftp for details.
(
  # sftp will exit if cd fails - must have/use build directory
  echo "cd build"
  # Try to create project sub-directory, but don't throw error if it fails (presumably because it already exists)
  echo "-mkdir ${SFTP_TARGET_DIR}"
  # sftp will exit if cd fails
  echo "cd ${SFTP_TARGET_DIR}"
  # sftp will ignore errors if old file can't be removed (presumably doesn't exist)
  echo "-rm ${SFTP_FILESPEC}"
  # sftp will exit if put fails
  echo "put deploy/${SFTP_FILESPEC}"
  # Show a little extra info for logging
  echo "pwd"
  echo "ls -l"
) | sftp -b - ${SFTP_USER}@${SFTP_SERVER}

# Webhook to Jenkins server to initiate deploy
# This doesn't work for some reason, disabling and will switch to manual deployments so this is infrequent
#curl "https://${CI_JENKINS_USER_CREDS}@${JENKINS_HOST}/${CI_JENKINS_JOB_URI}/buildWithParameters?token=${CI_JENKINS_DEPLOY_TOKEN}&TOMCAT_HOST=${CI_JENKINS_TOMCAT_HOST}&WEBAPP_NAME=${SFTP_PROJECT}"
