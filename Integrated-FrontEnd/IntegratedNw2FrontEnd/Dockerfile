# Use Node.js 20 as base image
FROM node:20 AS build

# Set the working directory inside the container
# WORKDIR /usr/share/nginx/html
WORKDIR /app

# Copy package.json and package-lock.json (if present) to /app
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code to /app
COPY . .

# Build the Vue.js application for production
RUN npm run build

# Use nginx as base image for serving the static files
FROM nginx:alpine

# Remove default nginx website
# RUN rm -rf /usr/share/nginx/html/*

# Copy built files from the previous stage to nginx directory
COPY --from=build /app/dist /usr/share/nginx/html

# Copy custom nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose port 80 to the outside world
EXPOSE 81

# Command to run nginx when the container starts
# CMD ["nginx", "-g", "daemon off;"]
