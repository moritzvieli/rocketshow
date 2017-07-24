#!/bin/bash

###################
# Runnable as root
###################

apt-get update && sudo apt-get install oracle-java8-jdk
apt-get install omxplayer
apt-get -y install fbi

mkdir /opt/rocketshow/
mkdir /opt/rocketshow/midi
mkdir /opt/rocketshow/video
mkdir /opt/rocketshow/img

