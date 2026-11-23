# Apache + PHP + pdo_mysql sobre Debian.
# (La imagen oficial php:8.2-apache se descarga corrupta en este Docker: binarios vacíos.)
FROM debian:bookworm-slim
RUN apt-get update \
 && apt-get install -y --no-install-recommends apache2 libapache2-mod-php php-mysql \
 && rm -rf /var/lib/apt/lists/* \
 && rm -f /var/www/html/index.html
EXPOSE 80
CMD ["apachectl", "-D", "FOREGROUND"]
