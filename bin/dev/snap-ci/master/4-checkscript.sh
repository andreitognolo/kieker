#!/usr/bin/env bash

sudo docker run -v ${SNAP_WORKING_DIR}:/opt/kieker kieker/kieker-build:openjdk6 /bin/bash -c "cd /opt/kieker; ./bin/dev/check-release-archives.sh"

exit $?
