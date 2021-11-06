# SMART Authorization Package
Package to simplify SMART Backend Services Authorization

## Features
 * Authorize backend services app with RS384 signed private key
 * Returns an access token that can be presented as a Bearer Token with the FHIR API
 * For more info: http://hl7.org/fhir/uv/bulkdata/authorization/index.html
    
### How do I use this package?
1. Create your app and public-private key pair
2. Enter the base URL and token endpoint in the Authorization action, along with the path to the private key .pem file that was previously created. 

