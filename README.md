# Rocket Show
A tool to automate your DMX and videoshow.

## For developers
### Development
Access the web api with the following url: http://localhost:8080/ROOT/api/status/, after deploying it in your local Apache Tomcat.

During development, the following commands can be used for the Angular 4 web application:
```shell
cd src/main/webapp
ng serve --base-href /
```

The app is then accessible under http://localhost:4200.

### Build
The Java web archive (WAR) can be built using this command:
```shell
mvn install
```

### Deployment
#### Seed directory
The seed directory structure /install/rocketshow can be packed on a mac with this commands (assuming you're currently in the install directory):
```shell
COPYFILE_DISABLE=true tar -c --exclude='.DS_Store' -zf directory.tar.gz rocketshow
```

The resulting directory.tar.gz should be uploaded to rocketshow.net/install.

#### Application
The built application should be uploaded to rocketshow.net/update and be named "current.war". The file "currentversion.xml" can be modified accordingly.

#### Installation on a Raspberry Pi
These commands can be used to install Rocket Show on a Raspberry Pi with Raspbian Lite installed:
```shell
sudo su - root
cd /tmp
wget rocketshow.net/install/script/install.sh
chmod +x install.sh
./install.sh
rm -rf install.sh
```