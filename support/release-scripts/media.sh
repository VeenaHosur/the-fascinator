#!/bin/sh
#
# Undertakes a Maven release of the USQ Media Repository
# Also creates a maven site for the version
#
# Usage: ./media.sh <release-name> <release-version> <new-snapshot-version>
#
# Where version is the SVN tag name that will be used.
#   For example: ./media.sh USQ-Media-Repo 0.1 0.2.-SNAPSHOT 

RELEASE_NAME=$1
VERSION=$2
SNAPSHOT=$3
BASE_URL=https://fascinator.usq.edu.au/svn-auth/adfi/media
TRUNK=$BASE_URL/trunk/
DS=`date +%Y%m%d%H%M%S`
TMP=/tmp/release/$VERSION/$DS
echo "TMP Folder: $TMP"

if [ "$1" = "" ];
then
  echo "Please provide a release name"
  exit
fi

if [ "$2" = "" ];
then
  echo "Please provide a release version"
  exit
fi

if [ "$3" = ""];
then
  echo "Please provide a snapshot version"
  exit
fi

echo Creating tag for Version $1

mkdir -p $TMP
cd $TMP

#Checkout trunk pom.xml
if ! svn co $TRUNK . ; then        
  echo "Unable to checkout pom.xml from $TRUNK";
  exit 1;
fi

#Prepare for release
if ! mvn --batch-mode -e -Dtag=$RELEASE_NAME-$VERSION release:prepare -DreleaseVersion=$VERSION -DdevelopmentVersion=$SNAPSHOT -DautoVersionSubmodules=true; then
  echo "Failed to prepare the release - please check the codebase."
  exit 1;
fi

mkdir /var/www/hudson/$RELEASE_NAME-$VERSION

#Deploy and create maven site for the release
#To disable the maven site creation put -Dgoals=deploy
#if ! mvn release:perform -e -Dgoals=deploy; then
#  echo "Fail to deploy the release and the maven site"
#  exit 1;
#fi

#Now, tidy up
#svn delete $RELEASE -m "Tidying up after release $VERSION"
rm -rf $TMP
