#!/bin/bash
# Entrypoint propio (el de la imagen oficial llega vacío en este Docker).
# Inicializa MySQL sin contraseña para root, carga /docker-entrypoint-initdb.d/*.sql y arranca mysqld.
set -e
DATADIR=/var/lib/mysql

if [ ! -d "$DATADIR/mysql" ]; then
  mysqld --initialize-insecure --user=mysql --datadir="$DATADIR"
  mysqld --user=mysql --skip-networking --socket=/tmp/init.sock --datadir="$DATADIR" &
  pid=$!
  for i in $(seq 1 60); do
    mysqladmin --socket=/tmp/init.sock -uroot ping --silent 2>/dev/null && break
    sleep 1
  done
  mysql --socket=/tmp/init.sock -uroot <<'SQL'
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '';
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED WITH mysql_native_password BY '';
GRANT ALL ON *.* TO 'root'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;
SQL
  for f in /docker-entrypoint-initdb.d/*.sql; do
    [ -f "$f" ] && mysql --socket=/tmp/init.sock -uroot --default-character-set=utf8mb4 < "$f"
  done
  mysqladmin --socket=/tmp/init.sock -uroot shutdown
  wait "$pid"
fi

exec mysqld --user=mysql
