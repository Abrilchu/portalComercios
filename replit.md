# Overview

UX Dual is a comprehensive business management portal designed for commercial establishments. The application provides a multi-role system with dashboard analytics, payment processing, customer management, and real-time data synchronization. Built as a Single Page Application (SPA), it targets Spanish-speaking markets with localized currency formatting and interfaces.

The system supports different user roles (cashier, manager, owner) with role-based access controls and features real-time payment monitoring with automatic polling for updates. The application emphasizes user experience with responsive design and comprehensive error handling.

## Performance Optimizations Implemented (Aug 2025)
The system has been optimized for high-performance multi-commercial usage with:
- **Database Connection Pooling**: HikariCP with 30 concurrent connections
- **Single Query Dashboard**: Optimized CTE query reducing response time from ~5s to ~1.5s 
- **Microservices Architecture**: Independent scaling of frontend and backend services
- **Error Handling**: Fallback systems for resilient operation

# User Preferences

Preferred communication style: Simple, everyday language.

## Brand Identity
- **Brand**: UX Capital/UX Dual
- **Primary Colors**: 
  - Main Blue: #1C304F
  - Primary Green: #1BD696  
  - Secondary Green: #54F8A6
  - Dark Blue: #0E1B38
  - Light Gray: #EEEEEE
- **Typography**: 
  - Primary: Anek Odia (titles, main headings)
  - Secondary: Montserrat (body text, secondary headings)
- **Philosophy**: Solvencia (solvency), Simpleza (simplicity), Rapidez (speed)
- **Tagline**: "Tus finanzas. Simples"
- **Icon Style**: Ionic icons, minimalist and clean

# System Architecture

## Microservices Architecture
UX Dual Portal implements a complete microservices architecture with two independent services:

### Frontend Microservice (Port 5000)
- **Service**: `ux-dual-frontend` v1.0.0
- **Technology**: Node.js native HTTP server
- **Responsibilities**: 
  - Serve SPA application
  - API proxy to backend
  - CORS handling
  - Health checks
  - Configuration injection
- **Endpoints**: `/health`, `/api/*` (proxy), `/auth/*` (proxy)

### Backend Microservice (Port 8080) 
- **Service**: `ux-dual-backend-api` v1.0.0
- **Technology**: Java 17 + Micronaut Framework
- **Responsibilities**:
  - REST API for business data
  - JWT authentication
  - Database management
  - Business logic
  - Health checks and metrics
- **Endpoints**: `/health`, `/info`, `/metrics`, `/api/*`, `/auth/*`

## Legacy Frontend Components (Embedded in Microservice)
- **Framework**: Vue 3 with Composition API for reactive component development
- **UI Framework**: Quasar Framework for Material Design components and responsive layouts
- **State Management**: Pinia for centralized store management with separate stores for authentication, dashboard, and payments
- **Routing**: Vue Router 4 with route guards for authentication-based navigation
- **Build System**: Module-based architecture with lazy loading for optimized performance

## Authentication & Authorization
- **Token-based Authentication**: JWT tokens stored in localStorage with automatic header injection
- **Role-based Access Control**: Multi-tier user roles (cashier, manager, owner) with computed permissions
- **Route Protection**: Navigation guards preventing unauthorized access to protected routes
- **Session Management**: Automatic logout on token expiration with redirect to login

## Service Communication
- **Inter-Service Communication**: HTTP-based API communication between frontend and backend microservices
- **Service Discovery**: Direct URL configuration (localhost:8080 ↔ localhost:5000)
- **Load Balancing**: Ready for horizontal scaling of individual services
- **Circuit Breaker**: Error handling with graceful degradation when services are unavailable

## Data Management
- **HTTP Client**: Axios with interceptors for request/response handling and error management
- **Real-time Updates**: Polling mechanism for payment synchronization with cursor-based tracking
- **State Persistence**: LocalStorage for authentication data and user preferences
- **Error Handling**: Centralized error management with user-friendly notifications
- **Database Access**: Centralized through backend microservice only

## Component Architecture
- **Layout System**: Main layout wrapper with nested route structure for authenticated pages
- **Page Components**: Lazy-loaded page components (Dashboard, Payments, Customers, Login)
- **Service Layer**: Dedicated API service with environment-based configuration
- **Utility Functions**: Modular utilities for authentication, data export, and formatting

## Data Processing & Export
- **Export Functionality**: CSV and JSON export capabilities with customizable column formatting
- **Data Formatting**: Localized currency (ARS) and number formatting for Argentina market
- **Status Management**: Color-coded status system for payment states with localized labels

## Microservice Configuration Files
- `frontend/microservice-config.js` - Frontend service configuration
- `frontend/microservice-server.js` - Frontend service implementation  
- `src/main/java/com/uxdual/portal/config/MicroserviceConfig.java` - Backend microservice config
- `src/main/java/com/uxdual/portal/controller/MicroserviceController.java` - Backend health/metrics endpoints
- `MICROSERVICES_ARCHITECTURE.md` - Complete architecture documentation

# External Dependencies

## Frontend Libraries
- **Vue 3**: Core reactive framework for component-based architecture
- **Quasar Framework**: Material Design UI components and layout system
- **Vue Router 4**: Client-side routing with history mode
- **Pinia**: State management store
- **Axios**: HTTP client for API communication
- **Chart.js**: Data visualization for dashboard analytics

## Backend Integration
- **REST API**: Backend communication through `/api` endpoints with development/production environment detection
- **Authentication API**: Login endpoint (`/auth/login`) for user authentication
- **Dashboard API**: Statistics endpoint (`/dashboard/stats`) for real-time metrics
- **Payments API**: Payment management and synchronization endpoints

## Development Tools
- **ESLint**: Code quality and consistency enforcement
- **Webpack**: Module bundling through Quasar CLI
- **Material Icons**: Icon library for consistent UI elements

## Browser APIs
- **LocalStorage**: Client-side data persistence for authentication and user data
- **File API**: Download functionality for data export features
- **History API**: Browser navigation management for SPA routing