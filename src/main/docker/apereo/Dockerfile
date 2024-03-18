FROM hotjava/cas:latest
COPY cas.yml /etc/cas/config/
COPY mykeystore.p12 /etc/cas/thekeystore/mykeystore.p12
COPY keystore.jwks /etc/cas/config/keystore.jwks
COPY OIDC-1001.json /etc/cas/services/
#RUN  apt update && apt install -y vim
