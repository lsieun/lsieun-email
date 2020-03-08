#!/bin/bash
# (1) Variable
FQCN="lsieun.EMail"
DIR_PATH="$(cd "$(dirname "$0")"; pwd -P)"
CLASS_PATH="${DIR_PATH}/target/classes"
LIB_PATH="${DIR_PATH}/target/lib"

# (2) Compile
cd ${DIR_PATH}
mvn clean compile

# (3) copy Resource
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

# (4) collect JAR PATH
JAR_PATH=""
cd ${LIB_PATH}
for jar_file in *.jar
do
    JAR_PATH="${LIB_PATH}/${jar_file}:"
done

echo "${JAR_PATH}${CLASS_PATH}"

# (5) Run Program
cd ${DIR_PATH}
nohup java -cp "${JAR_PATH}${CLASS_PATH}" "${FQCN}" > /dev/null 2>&1 &

