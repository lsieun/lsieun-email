#!/bin/bash
FQCN="lsieun.EMail"
DIR_PATH="$(cd "$(dirname "$0")"; pwd -P)"
CLASS_PATH="${DIR_PATH}/target/classes"

cd ${DIR_PATH}
mvn clean compile

function copy_resource {
  local from_path="${DIR_PATH}/${1}"
  local to_path="${CLASS_PATH}/${1}"

  if [ -e "${from_path}" -a -f "${from_path}" ]
  then
    cp -f "${from_path}" "${to_path}"
  fi
}

copy_resource config.properties
copy_resource logging.properties


nohup java -cp "${CLASS_PATH}" "${FQCN}" > /dev/null 2>&1 &

