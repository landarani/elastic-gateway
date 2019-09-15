# Service Gateway

## Configuration  
Set the following configuration properties to route your service

#### Service Settings
* service.uri = <service internal url, i.e.: https://d92364e.service.dc1.a9s-elasticsearch-consul:19200>
* service.username = <service username, used in basic authentication>
* service.password = <service password, used in basic authentication>
* service.cacertificate = <optional: if the service is using TLS and certificate signed by a unknown CA, the CA PEM formatted content can be provided in here. i.e. -----BEGIN CERTIFICATE-----\nMIIDxDCCA...-----END CERTIFICATE-----\n

#### Gateway Settings
Basic authentication credentials for the gateway can be provided be the following properties:
* spring.security.user.name=user
* spring.security.user.password=secret