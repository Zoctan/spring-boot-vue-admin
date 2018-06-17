#!/bin/bash
for i in $(find src/test/resources/dev/sql/*.sql) ; do
  mysql -uroot -proot admin_dev < ${i};
done
