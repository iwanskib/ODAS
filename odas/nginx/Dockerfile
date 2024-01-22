FROM nginx:alpine

RUN rm /etc/nginx/conf.d/default.conf

COPY default.conf /etc/nginx/conf.d/

COPY certs/nginx.crt /etc/nginx/certs/
COPY certs/nginx.key /etc/nginx/certs/
