# Point-of-Sale System

## Project Description
This Point-of-Sale (POS) system is a Java application developed for the Object-Oriented Design (IV1350) course. The system handles sales processing, inventory management, and receipt generation following object-oriented design principles.

## Features
- Process sales with multiple items
- Calculate prices including VAT
- Apply customer discounts
- Process payments and calculate change
- Generate receipts
- Update inventory

## System Architecture
The application follows a layered architecture with Model-View-Controller (MVC) pattern:

- **View Layer**: User interface that displays information and captures user input
- **Controller Layer**: Coordinates operations between view and model
- **Model Layer**: Contains core business logic and domain objects
- **Integration Layer**: Handles external system communication (accounting, inventory)
- **Utility Layer**: Provides supporting functionality like Amount class

## Building and Running

### Prerequisites
- JDK 17 or higher
- Make utility

### Commands
To compile the project:
```bash
make compile

To run unit tests:
```bash
make test

To run the application:
```bash
make run

To clean compiled files:
```bash
make clean

For help with available commands:
```bash
make help
