# Overview

UX Dual is a comprehensive business management portal designed for commercial establishments. The application provides a multi-role system with dashboard analytics, payment processing, customer management, and real-time data synchronization. Built as a Single Page Application (SPA), it targets Spanish-speaking markets with localized currency formatting and interfaces.

The system supports different user roles (cashier, manager, owner) with role-based access controls and features real-time payment monitoring with automatic polling for updates. The application emphasizes user experience with responsive design and comprehensive error handling.

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

## Project Structure
The project is now organized into two main directories:
- **backend/** - Java Micronaut application with Maven
- **frontend/** - Vue 3 Quasar application with Node.js server

## Local Development Setup
The project includes comprehensive local development setup scripts and documentation:
- **setup-local.bat** - Automated configuration script for Windows
- **setup-local.sh** - Automated configuration script for Unix/Linux/macOS  
- **SETUP-LOCAL.md** - Complete local setup guide
- **backend/README.md** - Detailed backend configuration instructions
- **backend/src/main/resources/db/migration/V2__create_erd_schema.sql** - Database schema based on ERD
- **backend/src/main/resources/db/sample_data.sql** - Sample data for local testing
- **backend/src/main/resources/application-local.yml** - Local configuration with SSL disabled

### Local Database Configuration
- Database: `uxdual_portal`
- User: `uxdual_user`
- Password: `uxdual123`
- Port: `5432` (PostgreSQL default)
- Demo user: `admin` / `password`

## Backend Architecture
- **Framework**: Java 17 with Micronaut Framework for REST API
- **Database**: PostgreSQL with manual schema management (Flyway disabled)
- **Build System**: Maven for dependency management and compilation
- **Authentication**: JWT-based authentication with role-based access control
- **Database Structure**: Multi-tenant with empresa (company) entity as tenant root
- **Schema Management**: ERD-based schema (V2__create_erd_schema.sql) replacing legacy V1 schema

## Frontend Architecture
- **Framework**: Vue 3 with Composition API for reactive component development
- **UI Framework**: Quasar Framework for Material Design components and responsive layouts
- **State Management**: Pinia for centralized store management with separate stores for authentication, dashboard, and payments
- **Routing**: Vue Router 4 with route guards for authentication-based navigation
- **Build System**: Module-based architecture with lazy loading for optimized performance
- **Development Server**: Express.js proxy server for API communication

## Authentication & Authorization
- **Token-based Authentication**: JWT tokens stored in localStorage with automatic header injection
- **Role-based Access Control**: Multi-tier user roles (cashier, manager, owner) with computed permissions
- **Route Protection**: Navigation guards preventing unauthorized access to protected routes
- **Session Management**: Automatic logout on token expiration with redirect to login

## Data Management
- **HTTP Client**: Axios with interceptors for request/response handling and error management
- **Real-time Updates**: Polling mechanism for payment synchronization with cursor-based tracking
- **State Persistence**: LocalStorage for authentication data and user preferences
- **Error Handling**: Centralized error management with user-friendly notifications

## Component Architecture
- **Layout System**: Main layout wrapper with nested route structure for authenticated pages
- **Page Components**: Lazy-loaded page components (Dashboard, Payments, Customers, Login)
- **Service Layer**: Dedicated API service with environment-based configuration
- **Utility Functions**: Modular utilities for authentication, data export, and formatting

## Data Processing & Export
- **Export Functionality**: CSV and JSON export capabilities with customizable column formatting
- **Data Formatting**: Localized currency (ARS) and number formatting for Argentina market
- **Status Management**: Color-coded status system for payment states with localized labels

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