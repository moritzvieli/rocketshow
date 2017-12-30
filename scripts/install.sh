#!/bin/bash
# 
# Install RocketShow on a Raspian.
# This script needs to be executed as root
# 

# Install all required packages
apt-get update

echo iptables-persistent iptables-persistent/autosave_v4 boolean true | sudo debconf-set-selections
echo iptables-persistent iptables-persistent/autosave_v6 boolean true | sudo debconf-set-selections
apt-get -y install iptables-persistent oracle-java8-jdk omxplayer fbi ola mplayer

# Add the rocketshow user
adduser \
  --system \
  --shell /bin/bash \
  --gecos 'Rocket Show' \
  --group \
  --disabled-password \
  --home /home/rocketshow \
  rocketshow

# Add the user to the required groups
usermod -a -G video rocketshow
usermod -a -G audio rocketshow
usermod -a -G plugdev rocketshow

# Add the sudoers permission (visudo)
insert="rocketshow      ALL=(ALL) NOPASSWD: ALL"
file="/etc/sudoers"

sed -i "s/root\sALL=(ALL:ALL) ALL/root    ALL=(ALL:ALL) ALL\n$insert/" $file

# Create the directory structure
mkdir /opt/rocketshow
mkdir /opt/rocketshow/bin
mkdir /opt/rocketshow/tomcat
mkdir /opt/rocketshow/song
mkdir /opt/rocketshow/setlist
mkdir /opt/rocketshow/log
mkdir /opt/rocketshow/update
mkdir /opt/rocketshow/media
mkdir /opt/rocketshow/media/midi
mkdir /opt/rocketshow/media/audio
mkdir /opt/rocketshow/media/video

# Create the update script
cat <<'EOF' >/opt/rocketshow/update.sh
#!/bin/bash
#

# Set the execution permissions on the downloaded scripts
chmod +x /opt/rocketshow/update/before.sh
chmod +x /opt/rocketshow/update/after.sh

# Execute the before script
/bin/bash /opt/rocketshow/update/before.sh

cp /opt/rocketshow/update/currentversion.xml /opt/rocketshow/currentversion.xml
cp /opt/rocketshow/update/current.war /opt/rocketshow/tomcat/webapps/ROOT.war

# Execute the after script
/bin/bash /opt/rocketshow/update/after.sh

# Cleanup the update folder
rm -rf /opt/rocketshow/update/*
EOF

# Set the user rocketshow as owner and add execution permissions
# on the update script
chown -R rocketshow:rocketshow /opt/rocketshow
chmod +x /opt/rocketshow/update.sh

# Install Tomcat (credits to https://wolfpaulus.com/java/tomcat-jessie/)
mkdir -p ~/tmp
cd ~/tmp
wget http://rocketshow.net/install/tomcat/apache-tomcat-8.5.24.tar.gz
tar xvzf ./apache-tomcat-8.5.24.tar.gz
rm apache-tomcat-8.5.24.tar.gz
mv ./apache-tomcat-8.5.24 ./tomcat
mv tomcat /opt/rocketshow
chown -R rocketshow:rocketshow /opt/rocketshow/tomcat/*
chmod +x /opt/rocketshow/tomcat/bin/*.sh

# Create the init script for tomcat
cat <<'EOF' >/etc/init.d/tomcat
#!/bin/bash
#
### BEGIN INIT INFO
# Provides:        tomcat
# Required-Start:  $network
# Required-Stop:   $network
# Default-Start:   2 3 4 5
# Default-Stop:    0 1 6
# Short-Description: Start/Stop Tomcat server
### END INIT INFO
 
PATH=/sbin:/bin:/usr/sbin:/usr/bin
 
start() {
 /bin/su - rocketshow -c /opt/rocketshow/tomcat/bin/startup.sh
}
 
stop() {
 /bin/su - rocketshow -c /opt/rocketshow/tomcat/bin/shutdown.sh 
}
 
case $1 in
  start|stop) $1;;
  restart) stop; start;;
  *) echo "Run as $0 &lt;start|stop|restart&gt;"; exit 1;;
esac
EOF

chmod 755 /etc/init.d/tomcat
update-rc.d tomcat defaults

# Get Tomcat some memory (add this line at the beginning)
sed -i '2iCATALINA_OPTS="-Djava.awt.headless=true -Dfile.encoding=UTF-8 -server -Xms500m -Xmx500m"\n' /opt/rocketshow/tomcat/bin/catalina.sh

# Speedup tomcat start (add this line at the beginning)
sed -i '2iJAVA_OPTS="-Djava.security.egd=file:/dev/urandom"\n' /opt/rocketshow/tomcat/bin/catalina.sh

# Set default port to 80
iptables -A INPUT -i eth0 -p tcp --dport 80 -j ACCEPT
iptables -A PREROUTING -t nat -p tcp --dport 80 -j REDIRECT --to-port 8080

iptables-save > /etc/iptables/rules.v4

# Download the required binaries
# USB interface reset according to https://raspberrypi.stackexchange.com/questions/9264/how-do-i-reset-a-usb-device-using-a-script
cd /opt/rocketshow/bin
wget http://rocketshow.net/install/bin/usbreset
chmod +x usbreset

# Overclock the raspberry to sustain streams without underruns
# - Set more memory for the GPU to play larger video files with omxplayer
# - Enable turbo-mode by default (boot_delay avoids sdcard corruption with turbo-mode, warranty is void)
# - Overclock the sdcard a little bit to prevent bufferunderruns with ALSA
# - Hide warnings (e.g. temperature icon)
sed -i '1i# ROCKETSHOWSTART\ngpu_mem=256\nforce_turbo=1\nboot_delay=1\ndtparam=sd_overclock=100\navoid_warnings=1\n# ROCKETSHOWEND\n' /boot/config.txt

# Set rocketshows nice priority to 10
sed -i '1irocketshow soft priority 10' /etc/security/limits.conf

# Remove the default webapps
rm -rf /opt/rocketshow/tomcat/webapps/*

# Download current war and versioninfo
cd /opt/rocketshow/tomcat/webapps
wget -O ROOT.war http://www.rocketshow.net/update/current.war
cd /opt/rocketshow
wget http://www.rocketshow.net/update/currentversion.xml
