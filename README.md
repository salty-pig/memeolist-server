# MEMEOLIST-SERVER

## RUNNING
 You will need
 * docker
 * docker-compose
 * maven
 * java 8

### Docker startup
 ```bash
cd docker;
docker-compose build;
docker-compose up;
 ```
### Application Deployment
**Note: Docker images need to be running**
```bash
mvn clean install wildfly:deploy
```
**When prompted use the username/password admin/admin**
