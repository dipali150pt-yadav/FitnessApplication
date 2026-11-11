# Fitness Application - AI-Powered Microservices Platform

A scalable microservices-based fitness platform with secure authentication and AI-powered real-time workout recommendations.

## 🏗️ Architecture

### Microservices
- **Eureka Server** (8761) - Service Discovery
- **Config Server** (8888) - Centralized Configuration Management
- **API Gateway** (8080) - Single entry point with routing
- **User Service** (8081) - User management and authentication
- **Workout Service** (8082) - AI-powered workout recommendations using Google Gemini API
- **Activity Service** (8083) - Activity tracking, calories burned, and duration logging

### Technology Stack
- **Backend**: Spring Boot 3.x, Spring Cloud
- **Frontend**: React
- **Authentication**: Keycloak
- **Messaging**: RabbitMQ
- **Databases**: PostgreSQL (User Service), MySQL (Activity Service)
- **AI**: Google Gemini API
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Containerization**: Docker & Docker Compose

## 🚀 Features

- ✅ Microservices architecture with service discovery
- ✅ Secure authentication and authorization with Keycloak
- ✅ AI-powered workout recommendations
- ✅ Real-time activity tracking
- ✅ Calories burned calculation
- ✅ Workout duration monitoring
- ✅ Async messaging between services
- ✅ Centralized configuration management
- ✅ API Gateway for unified access

## 📋 Prerequisites

- Java 21
- Maven 3.8+
- Docker & Docker Compose
- Node.js 18+ (for React frontend)
- PostgreSQL
- MySQL
- RabbitMQ
- Google Gemini API Key

## 🛠️ Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd FitnessApplication
```

### 2. Set Environment Variables
Create a `.env` file in the root directory:
```env
GEMINI_API_KEY=your_gemini_api_key
KEYCLOAK_CLIENT_SECRET=your_keycloak_secret
POSTGRES_PASSWORD=pass123
MYSQL_PASSWORD=pass123
RABBITMQ_PASSWORD=guest
```

### 3. Start Infrastructure Services
```bash
docker-compose up -d
```

### 4. Build All Services
```bash
mvn clean install
```

### 5. Start Services in Order
```bash
# 1. Start Eureka Server
cd eureka-server && mvn spring-boot:run

# 2. Start Config Server
cd config-server && mvn spring-boot:run

# 3. Start API Gateway
cd api-gateway && mvn spring-boot:run

# 4. Start Microservices
cd user-service && mvn spring-boot:run
cd workout-service && mvn spring-boot:run
cd activity-service && mvn spring-boot:run
```

### 6. Start Frontend
```bash
cd frontend
npm install
npm start
```

## 📡 API Endpoints

### Via API Gateway (http://localhost:8080)

#### User Service
- `POST /api/users/register` - Register new user
- `GET /api/users/{userId}` - Get user profile
- `PUT /api/users/{userId}` - Update user profile

#### Workout Service
- `POST /api/workouts/recommend` - Get AI-powered workout recommendations
- `GET /api/workouts/user/{userId}` - Get user's workout history
- `POST /api/workouts` - Log a workout

#### Activity Service
- `POST /api/activities` - Log an activity
- `GET /api/activities/user/{userId}` - Get user's activities
- `GET /api/activities/stats/{userId}` - Get activity statistics (total calories, duration)

## 🐳 Docker Deployment

### Build and Run with Docker Compose
```bash
docker-compose up --build
```

This will start all services including:
- PostgreSQL (5432)
- MySQL (3306)
- RabbitMQ (5672, 15672)
- Keycloak (8180)
- All microservices
- React frontend

## 📊 Service Ports

| Service | Port |
|---------|------|
| Eureka Server | 8761 |
| Config Server | 8888 |
| API Gateway | 8080 |
| User Service | 8081 |
| Workout Service | 8082 |
| Activity Service | 8083 |
| Keycloak | 8180 |
| PostgreSQL | 5432 |
| MySQL | 3306 |
| RabbitMQ | 5672, 15672 |
| Frontend | 3000 |

## 🔐 Security

- JWT-based authentication via Keycloak
- OAuth2 Resource Server configuration
- Role-based access control (RBAC)
- Secure password encoding with BCrypt

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run specific service tests
cd user-service && mvn test
```

## 📝 Project Structure

```
FitnessApplication/
├── eureka-server/          # Service Discovery
├── config-server/          # Configuration Management
├── api-gateway/            # API Gateway
├── user-service/           # User Management
├── workout-service/        # Workout Recommendations
├── activity-service/       # Activity Tracking
├── frontend/               # React Application
├── docker-compose.yml      # Docker Compose Configuration
├── pom.xml                 # Parent POM
└── README.md
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👥 Authors

- Your Name - Initial work

## 🙏 Acknowledgments

- Spring Cloud for microservices infrastructure
- Google Gemini API for AI-powered recommendations
- Keycloak for authentication
- RabbitMQ for async messaging
