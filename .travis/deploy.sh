#!/usr/bin/env sh

if [ "$TRAVIS_BRANCH" = "master" ] && [ "$TRAVIS_PULL_REQUEST" = "false" ];
then
    mvn --settings=.travis/settings.xml -B release:prepare
	mvn --settings=.travis/settings.xml -B release:perform
fi
