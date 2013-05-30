mvn deploy:deploy-file -DgroupId=org.codehaus.griffon \
  -DartifactId=griffon-template \
  -Dversion=1.3.0 \
  -Dpackaging=zip \
  -Dfile=template-1.3.0.zip \
  -DrepositoryId=bintray-nbn-maven-griffon-maven-plugin \
  -Durl=https://api.bintray.com/maven/nbn/maven/griffon-maven-plugin
