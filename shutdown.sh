#!/bin/bash
FQCN="lsieun.EMail"
PROCESS_LINE=$(jps -l | grep ${FQCN} -m -1)
if [ ${#PROCESS_LINE} -gt 0 ]; then
    echo ${PROCESS_LINE}
    PID=${PROCESS_LINE/" ${FQCN}"/}
    echo ${PID}
    kill -9 ${PID}
else
    echo "NO Process"
fi