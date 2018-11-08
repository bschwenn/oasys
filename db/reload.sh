#!/usr/bin/env bash
dropdb oasys
createdb oasys
psql oasys -af db/create-tables.sql
psql oasys -af db/load.sql
