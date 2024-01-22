# nginx/Dockerfile
FROM nginx:alpine

# Remove the default nginx.conf
RUN rm /etc/nginx/conf.d/default.conf

# Replace with our own nginx.conf
COPY default.conf /etc/nginx/conf.d/

# Copy the SSL certificates
COPY certs/nginx.crt /etc/nginx/certs/
COPY certs/nginx.key /etc/nginx/certs/
