# Keycloak Authentication Setup Guide

This guide explains how to set up Keycloak for authentication in the Fitness Application.

## Prerequisites

- Docker installed
- Fitness Application microservices running

## Step 1: Start Keycloak with Docker

```bash
docker run -d \
  --name keycloak \
  -p 8180:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  quay.io/keycloak/keycloak:latest \
  start-dev
```

Access Keycloak Admin Console at: http://localhost:8180

## Step 2: Create Realm

1. Log in to Keycloak Admin Console (admin/admin)
2. Click on "Create Realm"
3. Name: `fitness-realm`
4. Click "Create"

## Step 3: Create Client

1. Go to "Clients" → "Create client"
2. Client ID: `fitness-app`
3. Client Protocol: `openid-connect`
4. Click "Next"
5. Enable "Client authentication"
6. Enable "Authorization"
7. Enable "Standard flow", "Direct access grants"
8. Click "Save"

### Configure Client Settings

1. Go to client settings
2. Set "Valid redirect URIs": 
   - `http://localhost:3000/*`
   - `http://localhost:8080/*`
3. Set "Web origins": 
   - `http://localhost:3000`
   - `http://localhost:8080`
4. Click "Save"

### Get Client Secret

1. Go to "Credentials" tab
2. Copy the "Client Secret" (save for later configuration)

## Step 4: Create Roles

1. Go to "Realm roles" → "Create role"
2. Create the following roles:
   - `USER` (default role for regular users)
   - `ADMIN` (for administrators)
   - `TRAINER` (for fitness trainers)

## Step 5: Create Users

1. Go to "Users" → "Add user"
2. Username: `testuser`
3. Email: `test@fitness.com`
4. First Name: `Test`
5. Last Name: `User`
6. Email Verified: `ON`
7. Click "Create"

### Set Password

1. Go to "Credentials" tab
2. Set password: `password123`
3. Temporary: `OFF`
4. Click "Set password"

### Assign Roles

1. Go to "Role mapping" tab
2. Click "Assign role"
3. Select `USER` role
4. Click "Assign"

## Step 6: Configure Services (Already done in docker-compose.yml)

The services are pre-configured to work with Keycloak. The configuration includes:

### API Gateway OAuth2 Configuration

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8180/realms/fitness-realm
```

### Service Configuration

Each service will validate JWT tokens from Keycloak.

## Step 7: Testing Authentication

### Get Access Token

```bash
curl -X POST 'http://localhost:8180/realms/fitness-realm/protocol/openid-connect/token' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'grant_type=password' \
  -d 'client_id=fitness-app' \
  -d 'client_secret=YOUR_CLIENT_SECRET' \
  -d 'username=testuser' \
  -d 'password=password123'
```

Response:
```json
{
  "access_token": "eyJhbGc...",
  "expires_in": 300,
  "refresh_token": "eyJhbGc...",
  "token_type": "Bearer"
}
```

### Use Token in API Requests

```bash
curl -X GET 'http://localhost:8080/api/users/USER_ID' \
  -H 'Authorization: Bearer YOUR_ACCESS_TOKEN'
```

## Step 8: Frontend Integration

The React frontend is configured to use Keycloak for authentication:

```javascript
import Keycloak from 'keycloak-js';

const keycloak = new Keycloak({
  url: 'http://localhost:8180',
  realm: 'fitness-realm',
  clientId: 'fitness-app'
});
```

## Environment Variables

Update `.env` file with:

```env
KEYCLOAK_CLIENT_SECRET=your_client_secret_from_step_3
KEYCLOAK_REALM=fitness-realm
KEYCLOAK_URL=http://localhost:8180
```

## Role-Based Access Control (RBAC)

### User Roles and Permissions

- **USER**: Can access own data, create activities, get workout recommendations
- **TRAINER**: USER + Can view other users' activities
- **ADMIN**: Full access to all resources

### Endpoint Protection

Protected endpoints automatically check JWT claims for roles:

- `/api/users/{userId}` - USER (own data only) or ADMIN
- `/api/workouts/recommend` - Authenticated users
- `/api/activities/**` - Authenticated users
- `/api/admin/**` - ADMIN role only

## Troubleshooting

### Issue: Cannot get token
**Solution**: Verify Keycloak is running and realm/client are configured correctly

### Issue: Token validation fails
**Solution**: Check issuer-uri matches your Keycloak realm URL

### Issue: CORS errors
**Solution**: Verify Web Origins are set correctly in Keycloak client settings

## Production Considerations

1. **Use HTTPS**: Configure SSL/TLS certificates
2. **Strong Secrets**: Generate strong client secrets
3. **Token Expiration**: Configure appropriate token lifetimes
4. **Database**: Use PostgreSQL instead of dev mode
5. **Clustering**: Set up Keycloak cluster for high availability

## Additional Resources

- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Spring Security OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
- [Keycloak Spring Boot Adapter](https://www.keycloak.org/docs/latest/securing_apps/#_spring_boot_adapter)
