#!/bin/bash
set -e

GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${BLUE}🚀 Starting Coupon API Pipeline...${NC}"

echo -e "\n${BLUE}🧪 Step 1: Running automated tests (Docker)...${NC}"
docker build --target build -t coupon-api-tests .
docker run --rm coupon-api-tests ./mvnw -DtrimStackTrace=false test
if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Tests failed. Exiting pipeline.${NC}"
    exit 1
else
    echo -e "${GREEN}✅ Tests completed successfully.${NC}"
fi

echo -e "\n${BLUE}📦 Step 2: Building and starting application containers...${NC}"
docker-compose up -d --build

echo -e "\n${BLUE}ℹ️ Access info:${NC}"
echo -e "${GREEN}API:${NC} http://localhost:8080/api"
echo -e "${GREEN}Swagger:${NC} http://localhost:8080/api/swagger-ui.html"
echo -e "${GREEN}H2 Console:${NC} http://localhost:8080/api/h2-console"

echo -e "\n${BLUE}📋 App logs:${NC}"
docker-compose logs -f app