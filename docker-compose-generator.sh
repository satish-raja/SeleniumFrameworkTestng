#!/bin/bash

# Ensure script execution stops on errors
set -e

# Variables
BROWSER=$1
NODE_COUNT=$2
ACTION=$3
COMPOSE_FILE="docker-compose.yml"
VALID_BROWSERS=("chrome" "firefox")

# Validate input parameters
if [[ -z "$BROWSER" || -z "$NODE_COUNT" || -z "$ACTION" ]]; then
    echo "Usage: $0 <browser> <node_count> <start|stop>"
    exit 1
fi

# Validate browser type
if [[ ! " ${VALID_BROWSERS[@]} " =~ " ${BROWSER} " ]]; then
    echo "Invalid browser: $BROWSER. Supported browsers are: ${VALID_BROWSERS[@]}"
    exit 1
fi

# Function to generate docker-compose.yml dynamically
generate_compose_file() {
    echo "Generating docker-compose.yml for $BROWSER with $NODE_COUNT nodes..."

    cat <<EOF > $COMPOSE_FILE
version: "3.8"

services:
  selenium-hub:
    image: selenium/hub:4.28.1
    container_name: selenium-hub
    ports:
      - "4442:4442"
      - "4443:4443"
      - "4444:4444"
    networks:
      - selenium-network

EOF

    # Add browser nodes dynamically
    for ((i=1; i<=NODE_COUNT; i++)); do
        cat <<EOF >> $COMPOSE_FILE
  selenium-$BROWSER-$i:
    image: selenium/node-$BROWSER:4.28.1
    depends_on:
      - selenium-hub
    environment:
      - SE_EVENT_BUS_HOST=selenium-hub
      - SE_EVENT_BUS_PUBLISH_PORT=4442
      - SE_EVENT_BUS_SUBSCRIBE_PORT=4443
    networks:
      - selenium-network

EOF
    done

    # Define network
    cat <<EOF >> $COMPOSE_FILE
networks:
  selenium-network:
    driver: bridge
EOF

    echo "docker-compose.yml generated successfully!"
}

# Start Selenium Grid
if [[ "$ACTION" == "start" ]]; then
    generate_compose_file
    echo "Starting Selenium Grid..."
    docker compose up -d --remove-orphans
    echo "Selenium Grid started successfully."

# Stop Selenium Grid
elif [[ "$ACTION" == "stop" ]]; then
    echo "Stopping Selenium Grid..."
    docker compose down --remove-orphans
    echo "Selenium Grid stopped."

    # Verify if any containers are still running
    RUNNING_CONTAINERS=$(docker ps --filter "name=selenium" -q)
    if [[ -n "$RUNNING_CONTAINERS" ]]; then
        echo "Warning: Some Selenium containers are still running!"
        docker ps --filter "name=selenium"
    else
        echo "All Selenium containers have been successfully stopped."
    fi

    # Clean up unused images
    docker image prune -f

# Invalid action handling
else
    echo "Invalid action: $ACTION. Use 'start' or 'stop'."
    exit 1
fi
