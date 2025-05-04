# Makefile for standard Java project
# Variables
JAVAC := javac
JAVA := java
JUNIT_CP := lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar
MAIN_CLASS := se.kth.iv1350.pos.startup.Main
TEST_RUNNER := org.junit.runner.JUnitCore

# Directories
SRC_DIR := src
MAIN_SRC_DIR := $(SRC_DIR)/main/java
TEST_SRC_DIR := $(SRC_DIR)/test/java
TARGET_DIR := target
CLASSES_DIR := $(TARGET_DIR)/classes

# ANSI color codes
GREEN := \033[0;32m
YELLOW := \033[1;33m
NC := \033[0m

# Find all Java files
MAIN_JAVA_FILES := $(shell find $(MAIN_SRC_DIR) -name "*.java")
TEST_JAVA_FILES := $(shell find $(TEST_SRC_DIR) -name "*.java")

# Define specific test classes - add new ones as needed
CONTROLLER_TEST := se.kth.iv1350.pos.controller.ControllerTest
SALE_TEST := se.kth.iv1350.pos.model.SaleTest
TEST_CLASSES := $(CONTROLLER_TEST) $(SALE_TEST)

# Goals
.PHONY: all compile run test clean help

all: run

# Create target directory
$(CLASSES_DIR):
	@mkdir -p $(CLASSES_DIR)

# Compile all code (main and test) in one step
compile: $(CLASSES_DIR)
	@echo "$(GREEN)Compiling all code...$(NC)"
	@$(JAVAC) -cp $(JUNIT_CP) -d $(CLASSES_DIR) $(MAIN_JAVA_FILES) $(TEST_JAVA_FILES)
	@echo "$(GREEN)Compilation successful!$(NC)"

# Run the program
run: compile
	@echo "$(GREEN)Running program...$(NC)"
	@$(JAVA) -cp $(CLASSES_DIR) $(MAIN_CLASS)

# Run tests
test: compile
	@echo "$(GREEN)Running tests...$(NC)"
	@$(JAVA) -cp $(CLASSES_DIR):$(JUNIT_CP) $(TEST_RUNNER) $(TEST_CLASSES)

# Clean the project
clean:
	@echo "$(GREEN)Cleaning project...$(NC)"
	@rm -rf $(TARGET_DIR)
	@echo "$(GREEN)Project cleaned!$(NC)"

# Help information
help:
	@echo "$(YELLOW)Available targets:$(NC)"
	@echo "  all      - Compile and run the application"
	@echo "  compile  - Compile all source code (main and test)"
	@echo "  run      - Run the application"
	@echo "  test     - Run JUnit tests"
	@echo "  clean    - Clean the project"
