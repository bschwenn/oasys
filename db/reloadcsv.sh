#!/usr/bin/env bash
dropdb -h 35.237.36.16 -U postgres oasys;
createdb oasys -U postgres -h 35.237.36.16;
psql oasys -af db/create-tables.sql -U postgres -h 35.237.36.16;
psql oasys -af db/loadcsv.sql -U postgres -h 35.237.36.16;
psql oasys -af db/set_serials.sql postgres -h 35.237.36.16;
