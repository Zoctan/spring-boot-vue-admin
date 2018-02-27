#!/bin/bash
for i in $(find src/test/resources/dev/sql/*.sql) ; do
  mysql -uroot -proot api_dev < ${i};
done
