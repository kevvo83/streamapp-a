#!/bin/bash

cat /etc/apt/sources.list | egrep -v 's3|deb-src' | tee /etc/apt/sources.list

echo "deb-src http://security.ubuntu.com/ubuntu/ xenial-security multiverse" >> /etc/apt/sources.list
apt-get -y update
apt-get -y install vim
apt-get -y install git
apt-get -y install make

echo "alias ll='ls -larth'" >> ~/.bashrc
echo 'export PS1="\[\e[1;34m\]\u@\h\[\e[0m\] \[\e[0;32m\]\$PWD>\[\e[0m\] "' >> ~/.bashrc
#echo "export CONFLUENT_HOME=/usr/local/confluent" >> ~/.bashrc
#echo 'export PATH=${CONFLUENT_HOME}/bin:${PATH}' >> ~/.bashrc

/bin/bash

#cd /dev; git clone https://github.com/confluentinc/confluent-cli.git
#cd /dev/confluent-cli; make install

exit 0