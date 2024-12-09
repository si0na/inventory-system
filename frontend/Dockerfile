# Step 1: Build the React App
FROM node:16 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Step 2: Serve the Build with Nginx
FROM nginx:1.23
COPY --from=build /app/build /usr/share/nginx/html

# Add a configuration to forward API requests to the backend
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]



