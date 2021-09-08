#!/bin/bash

set -e
cwd=$(pwd)

echo "[SCS BUILD] Start building"

# Build Backend and docker
echo "[SCS BUILD] >>>> Backend building..."
cd scs-backend
mvn --settings $cwd/build-tools/common.settings.xml clean package
echo "[SCS BUILD] >>>> Backend docker image building..."
docker build -t netcracker/scs-backend .
echo "[SCS BUILD] >>>> Backend completed!"
cd $cwd

# Build HaProxy and docker
echo "[SCS BUILD] >>>> HaProxy building..."
cd scs-backend-haproxy
docker build -t netcracker/scs-backend-haproxy .
echo "[SCS BUILD] >>>> HaProxy completed!"
cd $cwd

# Build Coordinator and docker
echo "[SCS BUILD] >>>> Coordinator building..."
cd scs-coordinator
mvn --settings $cwd/build-tools/common.settings.xml clean package
echo "[SCS BUILD] >>>> Coordinator docker image building..."
docker build -t netcracker/scs-coordinator .
echo "[SCS BUILD] >>>> Coordinator completed!"
cd $cwd

# Build Client and docker
echo "[SCS BUILD] >>>> Client building..."
cd scs-client
docker build -t netcracker/scs-client .

echo "[SCS BUILD] ++--------------------------++"
echo "[SCS BUILD] ||        COMPLETED!        ||"
echo "[SCS BUILD] ++--------------------------++"


