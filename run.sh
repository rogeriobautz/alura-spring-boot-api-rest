#!/bin/bash
# This script is used to build and run a Docker container for the Vollmed API project.

mvn package -DskipTests
docker build -t cursos-alura/vollmed-api .
docker compose up